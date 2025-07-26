package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.MurfApiException;
import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import com.hackathon.aipresentationbackend.model.ToneRehearsalRequest;
import com.hackathon.aipresentationbackend.model.ToneType;
import com.hackathon.aipresentationbackend.model.VoiceOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class MurfService {
    private static final Logger log = LoggerFactory.getLogger(MurfService.class);
    private static final String MURF_API_BASE_URL = "https://api.murf.ai/v1";
    private static final String SPEECH_ENDPOINT = "/speech/generate";
    private static final String VOICES_ENDPOINT = "/voices";

    private final WebClient webClient;

    @Value("${murf.api.key}")
    private String murfApiKey;

    public MurfService(WebClient webClient) {
        this.webClient = webClient;
    }

    public SpeechResponse generateSpeech(SpeechRequest request) {
        log.info("Generating speech with text length: {}, voice: {}, speed: {}",
                request.getText().length(), request.getVoiceId(), request.getSpeed());

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("text", request.getText());
            requestBody.put("voiceId", request.getVoiceId());
            requestBody.put("speed", request.getSpeed());

            if (request.getTone() != null && !request.getTone().isEmpty()) {
                requestBody.put("tone", request.getTone());
            }

            Map response = webClient.post()
                    .uri(MURF_API_BASE_URL + SPEECH_ENDPOINT)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("api-key", murfApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                            .filter(this::isRetryableException)
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                    new MurfApiException("Failed to generate speech after retries",
                                            HttpStatus.INTERNAL_SERVER_ERROR, retrySignal.failure())))
                    .timeout(Duration.ofSeconds(30))
                    .doOnError(TimeoutException.class, e ->
                            log.error("Timeout while calling Murf API: {}", e.getMessage()))
                    .block();

            if (response == null) {
                throw new MurfApiException("Null response from Murf API", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String audioUrl = (String) response.get("audioFile");
            Integer duration = response.get("duration") != null ?
                    ((Number) response.get("duration")).intValue() : null;

            return SpeechResponse.withAudioUrl(
                    audioUrl,
                    duration,
                    request.getVoiceId(),
                    request.getSpeed()
            );

        } catch (WebClientResponseException e) {
            log.error("Murf API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new MurfApiException("Failed to generate speech: " + e.getMessage(),
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error generating speech: {}", e.getMessage(), e);
            throw new MurfApiException("Failed to generate speech: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public SpeechResponse generateToneVariation(ToneRehearsalRequest request) {
        log.info("Generating tone variation: {}, voice: {}, tone: {}",
                request.getSentence().length(), request.getVoiceId(), request.getTone());

        SpeechRequest speechRequest = new SpeechRequest(
                request.getSentence(),
                request.getVoiceId(),
                1.0, // Default speed
                request.getTone()
        );

        ToneType toneType = request.getToneType();
        if (toneType != null) {
            switch (toneType) {
                case CONFIDENT:
                    speechRequest.setSpeed(1.1);
                    break;
                case URGENT:
                    speechRequest.setSpeed(1.2);
                    break;
                case EMPATHETIC:
                    speechRequest.setSpeed(0.9);
                    break;
                case CONVERSATIONAL:
                default:
                    speechRequest.setSpeed(1.0);
                    break;
            }
        }

        return generateSpeech(speechRequest);
    }

    public List<VoiceOption> getAvailableVoices() {
        log.info("Fetching available voices from Murf API");

        try {
            // For the hackathon, we return a predefined list.
            // In a real application, you would make a WebClient call here.
            return getPredefinedVoices();
        } catch (WebClientResponseException e) {
            log.error("Murf API error when fetching voices: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new MurfApiException("Failed to fetch available voices: " + e.getMessage(),
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error fetching available voices: {}", e.getMessage(), e);
            throw new MurfApiException("Failed to fetch available voices: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public boolean voiceSupportsTone(String voiceId, String tone) {
        List<VoiceOption> voices = getAvailableVoices();
        return voices.stream()
                .filter(voice -> voice.getVoiceId().equals(voiceId))
                .anyMatch(voice -> voice.supportsTone(tone));
    }

    private List<VoiceOption> getPredefinedVoices() {
        List<VoiceOption> voices = new ArrayList<>();
        
        // Use only known working voice IDs - we'll use en-US-julia as base and create variations
        // In a real implementation, you would get these from Murf AI's actual voice list
        voices.add(VoiceOption.createProfessionalMale("en-US-julia", "Marcus", "American"));
        voices.add(VoiceOption.createProfessionalFemale("en-US-julia", "Julia", "American"));
        voices.add(VoiceOption.createProfessionalMale("en-US-julia", "Thomas", "British"));
        voices.add(VoiceOption.createProfessionalFemale("en-US-julia", "Emily", "British"));
        voices.add(VoiceOption.createConversationalMale("en-US-julia", "Alex", "American"));
        voices.add(VoiceOption.createConversationalFemale("en-US-julia", "Sophia", "American"));
        return voices;
    }

    /**
     * Generate speech using MurfRequest model (for voice recommendation system)
     */
    public String generateSpeech(com.hackathon.aipresentationbackend.model.MurfRequest request) {
        log.info("Generating speech with MurfRequest - text length: {}, voice: {}, tone: {}",
                request.getText().length(), request.getVoiceId(), request.getTone());

        try {
            SpeechRequest speechRequest = new SpeechRequest(
                request.getText(),
                request.getVoiceId(),
                request.getSpeed() != null ? request.getSpeed() : 1.0,
                request.getTone()
            );

            SpeechResponse response = generateSpeech(speechRequest);
            return response.getAudioUrl();

        } catch (Exception e) {
            log.error("Error generating speech with MurfRequest: {}", e.getMessage(), e);
            throw new MurfApiException("Failed to generate speech: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            HttpStatus status = HttpStatus.valueOf(((WebClientResponseException) throwable).getStatusCode().value());

            return status.is5xxServerError() ||
                    status == HttpStatus.TOO_MANY_REQUESTS ||
                    status == HttpStatus.REQUEST_TIMEOUT;
        }

        return throwable instanceof java.net.ConnectException ||
                throwable instanceof java.net.SocketTimeoutException ||
                throwable instanceof TimeoutException;
    }
}