    package com.hackathon.aipresentationbackend.service;



    import com.hackathon.aipresentationbackend.exception.AssemblyAIException;
    import com.hackathon.aipresentationbackend.model.AudioUploadResponse;
    import com.hackathon.aipresentationbackend.model.TranscriptionRequest;
    import com.hackathon.aipresentationbackend.model.TranscriptionResponse;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.core.io.ByteArrayResource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.reactive.function.BodyInserters;
    import org.springframework.web.reactive.function.client.WebClient;
    import org.springframework.web.reactive.function.client.WebClientResponseException;
    import reactor.core.publisher.Mono;
    import reactor.util.retry.Retry;

    import java.io.IOException;
    import java.time.Duration;
    import java.util.Map;
    import java.util.concurrent.TimeoutException;

    /**
     * Service for interacting with AssemblyAI API using a reactive approach.
     * Handles audio transcription and file uploads.
     */
    @Service
    public class AssemblyAIService {

        private static final Logger log = LoggerFactory.getLogger(AssemblyAIService.class);
        private static final String ASSEMBLY_AI_BASE_URL = "https://api.assemblyai.com/v2";
        private static final String UPLOAD_ENDPOINT = "/upload";
        private static final String TRANSCRIPT_ENDPOINT = "/transcript";

        // Configuration for the polling mechanism
        private static final Duration POLLING_INITIAL_DELAY = Duration.ofSeconds(3);
        private static final Duration POLLING_INTERVAL = Duration.ofSeconds(3);
        private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5); // Overall timeout for the entire process

        private final WebClient webClient;

        @Value("${assemblyai.api.key}")
        private String assemblyApiKey;

        public AssemblyAIService(WebClient webClient) {
            this.webClient = webClient;
        }

        /**
         * Orchestrates the entire transcription process: upload, submit, and poll.
         * This method is blocking and suitable for use in a traditional Spring MVC controller.
         * It chains reactive methods internally and blocks only at the end to get the final result.
         *
         * @param audioFile The audio file to transcribe.
         * @return The transcription text.
         * @throws AssemblyAIException if any step in the process fails.
         * @throws IOException if the audio file cannot be read.
         */
        public String transcribeAudio(MultipartFile audioFile) throws IOException {
            log.info("Starting transcription process for file: {}", audioFile.getOriginalFilename());
            try {
                // This is a reactive chain that executes the entire workflow.
                // .block() is called at the end to wait for the final result.
                String transcriptionText = uploadAudioFile(audioFile)
                        .flatMap(this::submitTranscriptionRequest)
                        .flatMap(this::pollUntilComplete)
                        .map(finalResponse -> { // Transform the final successful response to a String
                            if ("error".equalsIgnoreCase(finalResponse.getStatus())) {
                                // This exception will be caught by the outer try-catch block
                                throw new AssemblyAIException("Transcription failed: " + finalResponse.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            return finalResponse.getText();
                        })
                        .block(PROCESS_TIMEOUT); // Block here to get the final result, with a timeout.

                if (transcriptionText == null) {
                    // This can happen on timeout or if the stream completes empty.
                    throw new AssemblyAIException("Transcription process timed out or returned no text.", HttpStatus.REQUEST_TIMEOUT);
                }
                return transcriptionText;

            } catch (WebClientResponseException e) {
                // Handle API errors from WebClient
                throw new AssemblyAIException("AssemblyAI API Error", HttpStatus.valueOf(e.getStatusCode().value()), e);
            } catch (Exception e) {
                // Catch exceptions thrown from within the reactive chain (like in .map())
                if (e.getCause() instanceof AssemblyAIException) {
                    throw (AssemblyAIException) e.getCause();
                }
                throw new AssemblyAIException("An unexpected error occurred during transcription.", HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
        }

        /**
         * Uploads an audio file to AssemblyAI. This is a two-step process:
         * 1. POST to /upload to get a temporary, pre-signed upload URL.
         * 2. PUT the audio file to that pre-signed URL.
         *
         * @param audioFile The audio file to upload.
         * @return A Mono emitting the URL of the uploaded file, to be used for transcription.
         */
        private Mono<String> uploadAudioFile(MultipartFile audioFile) {
            // Step 1: Get a pre-signed URL from AssemblyAI.
            Mono<String> getUploadUrlMono = webClient.post()
                    .uri(ASSEMBLY_AI_BASE_URL + UPLOAD_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                    .bodyValue(Map.of()) // AssemblyAI requires an empty JSON object body.
                    .retrieve()
                    .bodyToMono(AudioUploadResponse.class)
                    .map(AudioUploadResponse::getUploadUrl);

            // Step 2: Use the pre-signed URL to upload the file with a PUT request.
            return getUploadUrlMono.flatMap(uploadUrl -> {
                try {
                    // Read the file into a resource
                    ByteArrayResource resource = new ByteArrayResource(audioFile.getBytes());
                    return webClient.put()
                            .uri(uploadUrl) // Use the pre-signed URL
                            .header(HttpHeaders.CONTENT_TYPE, audioFile.getContentType()) // Set the content type of the file
                            .body(BodyInserters.fromResource(resource))
                            .retrieve()
                            .bodyToMono(Void.class) // We don't need the response body from the PUT call
                            .thenReturn(uploadUrl); // Return the URL for the next step in the chain
                } catch (IOException e) {
                    return Mono.error(new AssemblyAIException("Failed to read audio file for upload.", HttpStatus.INTERNAL_SERVER_ERROR, e));
                }
            }).doOnSuccess(url -> log.info("Successfully uploaded audio file. URL: {}", url));
        }

        /**
         * Submits the audio URL for transcription.
         *
         * @param audioUrl The URL of the uploaded audio file.
         * @return A Mono emitting the initial transcription response.
         */
        private Mono<TranscriptionResponse> submitTranscriptionRequest(String audioUrl) {
            TranscriptionRequest request = new TranscriptionRequest.Builder().audioUrl(audioUrl).build();
            return webClient.post()
                    .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TranscriptionResponse.class)
                    .doOnSuccess(res -> log.info("Submitted transcription request. ID: {}", res.getId()));
        }

        /**
         * Polls for the transcription result until it is 'completed' or 'error'.
         * Uses the 'expand' operator for efficient, non-blocking polling.
         *
         * @param initialResponse The response from the initial submission.
         * @return A Mono emitting the final transcription response.
         */
        private Mono<TranscriptionResponse> pollUntilComplete(TranscriptionResponse initialResponse) {
            // If the initial status is already terminal, return it immediately.
            if (isTerminalStatus(initialResponse.getStatus())) {
                return Mono.just(initialResponse);
            }

            // Use the 'expand' operator to recursively poll.
            return getTranscription(initialResponse.getId())
                    .delayElement(POLLING_INITIAL_DELAY) // Wait before the first poll
                    .expand(response -> {
                        if (isTerminalStatus(response.getStatus())) {
                            log.info("Polling complete. Final status: {}", response.getStatus());
                            return Mono.empty(); // Stop polling.
                        }
                        // Wait for the defined interval, then poll again.
                        return Mono.delay(POLLING_INTERVAL).flatMap(d -> getTranscription(response.getId()));
                    })
                    .filter(this::isTerminalStatus) // Only emit the final, terminal response.
                    .next(); // Take the first terminal response and complete the stream.
        }

        /**
         * Fetches a transcription result by its ID.
         *
         * @param transcriptionId The ID of the transcription.
         * @return A Mono emitting the transcription response.
         */
        private Mono<TranscriptionResponse> getTranscription(String transcriptionId) {
            return webClient.get()
                    .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT + "/" + transcriptionId)
                    .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                    .retrieve()
                    .bodyToMono(TranscriptionResponse.class)
                    .doOnNext(res -> log.debug("Polling status for {}: {}", transcriptionId, res.getStatus()))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).filter(this::isRetryableException));
        }

        /**
         * Checks if a transcription status is terminal ('completed' or 'error').
         */
        private boolean isTerminalStatus(String status) {
            return "completed".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status);
        }

        /**
         * Determines if an exception is retryable.
         *
         * @param throwable The exception to check.
         * @return true if the exception is retryable, false otherwise.
         */
        private boolean isRetryableException(Throwable throwable) {
            if (throwable instanceof WebClientResponseException) {
                // Convert HttpStatusCode to HttpStatus for comparison
                HttpStatus status = HttpStatus.valueOf(((WebClientResponseException) throwable).getStatusCode().value());
                return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
            }
            return throwable instanceof TimeoutException || throwable instanceof IOException;
        }
    }