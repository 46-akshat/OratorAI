//package com.hackathon.aipresentationbackend.service;
//
//import com.hackathon.aipresentationbackend.exception.AssemblyAIException;
//import com.hackathon.aipresentationbackend.model.AudioUploadResponse;
//import com.hackathon.aipresentationbackend.model.TranscriptionRequest;
//import com.hackathon.aipresentationbackend.model.TranscriptionResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//import reactor.util.retry.Retry;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.TimeoutException;
//
//@Service
//public class AssemblyAIService {
//
//    private static final Logger log = LoggerFactory.getLogger(AssemblyAIService.class);
//    private static final String ASSEMBLY_AI_BASE_URL = "https://api.assemblyai.com/v2";
//    private static final String UPLOAD_ENDPOINT = "/upload";
//    private static final String TRANSCRIPT_ENDPOINT = "/transcript";
//
//    private static final Duration POLLING_INITIAL_DELAY = Duration.ofSeconds(3);
//    private static final Duration POLLING_INTERVAL = Duration.ofSeconds(3);
//    private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5);
//
//    private final WebClient webClient;
//
//    @Value("${assemblyai.api.key}")
//    private String assemblyApiKey;
//
//    public AssemblyAIService(WebClient webClient) {
//        this.webClient = webClient;
//    }
//
//    public String transcribeAudio(MultipartFile audioFile) throws IOException {
//        log.info("Starting transcription process for file: {}", audioFile.getOriginalFilename());
//        try {
//            String transcriptionText = uploadAudioFile(audioFile)
//                    .flatMap(this::submitTranscriptionRequest)
//                    .flatMap(this::pollUntilComplete)
//                    .map(finalResponse -> { // <<--- THE FIX IS HERE
//                        if (finalResponse.hasFailed()) {
//                            throw new AssemblyAIException("Transcription failed: " + finalResponse.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
//                        }
//                        // This now correctly returns the text (a String) from the object
//                        return finalResponse.getText();
//                    })
//                    .block(PROCESS_TIMEOUT);
//
//            if (transcriptionText == null) {
//                throw new AssemblyAIException("Transcription process timed out or returned no text.", HttpStatus.REQUEST_TIMEOUT);
//            }
//            return transcriptionText;
//
//        } catch (WebClientResponseException e) {
//            throw new AssemblyAIException("AssemblyAI API Error", HttpStatus.valueOf(e.getStatusCode().value()), e);
//        } catch (Exception e) {
//            if (e.getCause() instanceof AssemblyAIException) {
//                throw (AssemblyAIException) e.getCause();
//            }
//            throw new AssemblyAIException("An unexpected error occurred during transcription.", HttpStatus.INTERNAL_SERVER_ERROR, e);
//        }
//    }
//
//    private Mono<String> uploadAudioFile(MultipartFile audioFile) {
//        return Mono.fromCallable(audioFile::getBytes)
//                .flatMap(fileBytes -> webClient.post()
//                        .uri(ASSEMBLY_AI_BASE_URL + UPLOAD_ENDPOINT)
//                        .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
//                        .bodyValue(fileBytes)
//                        .retrieve()
//                        .bodyToMono(AudioUploadResponse.class)
//                        .map(AudioUploadResponse::getUploadUrl)
//                )
//                .doOnSuccess(url -> log.info("Successfully uploaded audio file. URL: {}", url))
//                .doOnError(e -> log.error("Failed to upload audio file", e));
//    }
//
//    private Mono<TranscriptionResponse> submitTranscriptionRequest(String audioUrl) {
//        TranscriptionRequest request = new TranscriptionRequest(audioUrl);
//        return webClient.post()
//                .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT)
//                .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(TranscriptionResponse.class)
//                .doOnSuccess(res -> log.info("Submitted transcription request. ID: {}", res.getId()));
//    }
//
//    private Mono<TranscriptionResponse> pollUntilComplete(TranscriptionResponse initialResponse) {
//        if (isTerminalStatus(initialResponse)) {
//            return Mono.just(initialResponse);
//        }
//
//        return getTranscription(initialResponse.getId())
//                .delayElement(POLLING_INITIAL_DELAY)
//                .expand(response -> {
//                    if (isTerminalStatus(response)) {
//                        log.info("Polling complete. Final status: {}", response.getStatus());
//                        return Mono.empty();
//                    }
//                    return Mono.delay(POLLING_INTERVAL).flatMap(d -> getTranscription(response.getId()));
//                })
//                .filter(this::isTerminalStatus)
//                .next();
//    }
//
//    private boolean isTerminalStatus(TranscriptionResponse transcriptionResponse) {
//        String status = transcriptionResponse.getStatus(); // Get the status from the object
//        return "completed".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status);
//    }
//
//    private Mono<TranscriptionResponse> getTranscription(String transcriptionId) {
//        return webClient.get()
//                .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT + "/" + transcriptionId)
//                .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
//                .retrieve()
//                .bodyToMono(TranscriptionResponse.class)
//                .doOnNext(res -> log.debug("Polling status for {}: {}", transcriptionId, res.getStatus()))
//                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).filter(this::isRetryableException));
//    }
//
////    private boolean isTerminalStatus(String status) {
////        return "completed".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status);
////    }
//
//    private boolean isRetryableException(Throwable throwable) {
//        if (throwable instanceof WebClientResponseException) {
//            HttpStatus status = HttpStatus.valueOf(((WebClientResponseException) throwable).getStatusCode().value());
//            return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
//        }
//        return throwable instanceof TimeoutException || throwable instanceof IOException;
//    }
//}
package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.AssemblyAIException;
import com.hackathon.aipresentationbackend.model.AudioUploadResponse;
import com.hackathon.aipresentationbackend.model.TranscriptionRequest;
import com.hackathon.aipresentationbackend.model.TranscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

@Service
public class AssemblyAIService {

    private static final Logger log = LoggerFactory.getLogger(AssemblyAIService.class);
    private static final String ASSEMBLY_AI_BASE_URL = "https://api.assemblyai.com/v2";
    private static final String UPLOAD_ENDPOINT = "/upload";
    private static final String TRANSCRIPT_ENDPOINT = "/transcript";

    private static final Duration POLLING_INTERVAL = Duration.ofSeconds(3);
    private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5);

    private final WebClient webClient;

    @Value("${assemblyai.api.key}")
    private String assemblyApiKey;

    public AssemblyAIService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Public method to handle MultipartFile uploads.
     */
    public String transcribeAudio(MultipartFile audioFile) throws IOException {
        log.info("Starting transcription process for file: {}", audioFile.getOriginalFilename());
        return executeTranscription(audioFile.getBytes());
    }

    /**
     * Public method to handle Base64 encoded audio strings.
     */
    public String transcribeAudio(String base64Audio) {
        log.info("Starting transcription process for Base64 audio string.");
        byte[] audioData = Base64.getDecoder().decode(base64Audio);
        return executeTranscription(audioData);
    }

    /**
     * Private helper method containing the core transcription logic.
     * This is called by both public transcribeAudio methods.
     */
    private String executeTranscription(byte[] audioData) {
        try {
            String transcriptionText = uploadAudio(audioData)
                    .flatMap(this::submitTranscriptionRequest)
                    .flatMap(this::pollUntilComplete)
                    .map(finalResponse -> {
                        if (finalResponse.hasFailed()) {
                            throw new AssemblyAIException("Transcription failed: " + finalResponse.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        return finalResponse.getText();
                    })
                    .block(PROCESS_TIMEOUT);

            if (transcriptionText == null) {
                throw new AssemblyAIException("Transcription process timed out or returned no text.", HttpStatus.REQUEST_TIMEOUT);
            }
            return transcriptionText;

        } catch (Exception e) {
            log.error("An unexpected error occurred during transcription", e);
            if (e.getCause() instanceof AssemblyAIException) {
                throw (AssemblyAIException) e.getCause();
            }
            throw new AssemblyAIException("An unexpected error occurred during transcription.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Mono<String> uploadAudio(byte[] audioData) {
        return webClient.post()
                .uri(ASSEMBLY_AI_BASE_URL + UPLOAD_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                .bodyValue(audioData)
                .retrieve()
                .bodyToMono(AudioUploadResponse.class)
                .map(AudioUploadResponse::getUploadUrl);
    }

    private Mono<TranscriptionResponse> submitTranscriptionRequest(String audioUrl) {
        // Configure transcription to capture filler words and improve speech analysis
        TranscriptionRequest request = new TranscriptionRequest.Builder()
                .audioUrl(audioUrl)
                .languageCode("en_us")
                .punctuate(true)
                .formatText(false)  // Keep raw text for better filler word detection
                .filterProfanity(false)  // CRITICAL: Don't filter out filler words like "um", "uh"
                .disfluencies(true)  // ENABLE: Detect filler words and speech disfluencies
                .build();
                
        log.info("Submitting transcription request with filler word detection enabled");
        return webClient.post()
                .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TranscriptionResponse.class)
                .doOnSuccess(res -> log.info("Submitted transcription request. ID: {}", res.getId()));
    }

    private Mono<TranscriptionResponse> pollUntilComplete(TranscriptionResponse initialResponse) {
        if (isTerminalStatus(initialResponse)) {
            return Mono.just(initialResponse);
        }

        return getTranscription(initialResponse.getId())
                .expand(response -> {
                    if (isTerminalStatus(response)) {
                        log.info("Polling complete. Final status: {}", response.getStatus());
                        return Mono.empty();
                    }
                    return Mono.delay(POLLING_INTERVAL).flatMap(d -> getTranscription(response.getId()));
                })
                .filter(this::isTerminalStatus)
                .next();
    }

    private Mono<TranscriptionResponse> getTranscription(String transcriptionId) {
        return webClient.get()
                .uri(ASSEMBLY_AI_BASE_URL + TRANSCRIPT_ENDPOINT + "/" + transcriptionId)
                .header(HttpHeaders.AUTHORIZATION, assemblyApiKey)
                .retrieve()
                .bodyToMono(TranscriptionResponse.class)
                .doOnNext(res -> log.debug("Polling status for {}: {}", transcriptionId, res.getStatus()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).filter(this::isRetryableException));
    }

    private boolean isTerminalStatus(TranscriptionResponse response) {
        if (response == null || response.getStatus() == null) {
            return false;
        }
        String status = response.getStatus();
        return "completed".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status);
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            HttpStatus status = HttpStatus.valueOf(((WebClientResponseException) throwable).getStatusCode().value());
            return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
        }
        return throwable instanceof TimeoutException || throwable instanceof IOException;
    }
}

