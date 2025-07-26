package com.hackathon.aipresentationbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.aipresentationbackend.exception.GeminiApiException;
import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import com.hackathon.aipresentationbackend.model.VoiceAnalysis;
import com.hackathon.aipresentationbackend.model.MurfVoice;
import com.hackathon.aipresentationbackend.model.ToneType;
import com.hackathon.aipresentationbackend.model.VoiceGender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.UUID;
import java.util.Arrays;

@Service
public class GeminiService {
    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    private static final String GEMINI_API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private static final String GEMINI_MODEL = "models/gemini-1.5-flash-latest"; // Updated to a valid, recent model
    private static final String GENERATE_CONTENT_ENDPOINT = ":generateContent";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public AnalysisResponse analyzePresentation(String originalScript, String spokenTranscript) {
        log.info("Analyzing presentation with original script length: {}, spoken transcript length: {}",
                originalScript.length(), spokenTranscript.length());

        try {
            String prompt = createAnalysisPrompt(originalScript, spokenTranscript);
            Map<String, Object> response = callGeminiApi(prompt);
            return parseAnalysisResponse(response);
        } catch (WebClientResponseException e) {
            log.error("Gemini API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeminiApiException("Failed to analyze presentation",
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error analyzing presentation: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to analyze presentation",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public List<String> generateImprovementSuggestions(String originalScript, String spokenTranscript) {
        log.info("Generating improvement suggestions for presentation");
        try {
            String prompt = createImprovementSuggestionsPrompt(originalScript, spokenTranscript);
            Map<String, Object> response = callGeminiApi(prompt);
            return parseImprovementSuggestions(response);
        } catch (WebClientResponseException e) {
            log.error("Gemini API error when generating suggestions: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeminiApiException("Failed to generate improvement suggestions",
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error generating improvement suggestions: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to generate improvement suggestions",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public int calculateDeliveryScore(String originalScript, String spokenTranscript) {
        log.info("Calculating delivery score for presentation");
        try {
            String prompt = createScoringPrompt(originalScript, spokenTranscript);
            Map<String, Object> response = callGeminiApi(prompt);
            return parseDeliveryScore(response);
        } catch (WebClientResponseException e) {
            log.error("Gemini API error when calculating score: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeminiApiException("Failed to calculate delivery score",
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error calculating delivery score: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to calculate delivery score",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Analyze user's speech and recommend optimal voice for improvement
     */
    public VoiceAnalysis analyzeVoiceAndRecommend(String speechText, String presentationType,
                                                  String targetAudience, VoiceGender preferredGender) {
        log.info("Analyzing voice for speech text length: {}, presentation type: {}, target audience: {}",
                speechText.length(), presentationType, targetAudience);

        try {
            String prompt = createVoiceAnalysisPrompt(speechText, presentationType, targetAudience, preferredGender);
            Map<String, Object> response = callGeminiApi(prompt);
            return parseVoiceAnalysisResponse(response, speechText);
        } catch (WebClientResponseException e) {
            log.error("Gemini API error during voice analysis: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeminiApiException("Failed to analyze voice",
                    HttpStatus.valueOf(e.getStatusCode().value()), e);
        } catch (Exception e) {
            log.error("Error analyzing voice: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to analyze voice",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Map<String, Object> callGeminiApi(String prompt) {
        log.debug("Calling Gemini API with prompt length: {}", prompt.length());

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));
        content.put("role", "user");
        requestBody.put("contents", List.of(content));

        String apiUrl = GEMINI_API_BASE_URL + "/" + GEMINI_MODEL + GENERATE_CONTENT_ENDPOINT + "?key=" + geminiApiKey;
        log.info("Calling Gemini API at URL: {}", apiUrl);

        try {
            return webClient.post()
                    .uri(apiUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                            .filter(this::isRetryableException)
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                    new GeminiApiException("Gemini API call failed after retries.",
                                            HttpStatus.SERVICE_UNAVAILABLE, retrySignal.failure())))
                    .timeout(Duration.ofSeconds(30), Mono.error(new TimeoutException("Gemini API call timed out after 30 seconds.")))
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Gemini API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GeminiApiException("Failed to call Gemini API", HttpStatus.valueOf(e.getStatusCode().value()), e);
        }
    }

    private String createAnalysisPrompt(String originalScript, String spokenTranscript) {
        return "You are an expert presentation coach. Your task is to analyze a presentation delivery by comparing the original script with the spoken transcript. " +
                "Focus on content accuracy, but also identify potential areas for improvement in delivery style, such as the use of filler words (e.g., 'um', 'ah', 'like'), pacing, and clarity. " +
                "\n\n" +
                "Original Script:\n" + originalScript +
                "\n\n" +
                "Spoken Transcript:\n" + spokenTranscript +
                "\n\n" +
                "Based on your analysis, you MUST provide feedback in the following JSON format. " +
                "Provide one concise sentence of positive feedback. Even if the delivery was excellent, you MUST provide at least one specific, actionable improvement suggestion. Do not leave any field empty." +
                "\n{" +
                "\n  \"score\": [numerical score between 1-10]," +
                "\n  \"positiveFeedback\": \"[one sentence of positive feedback highlighting a strength]\"," +
                "\n  \"improvementPoints\": \"[one actionable improvement suggestion]\"" +
                "\n}";
    }

    private String createImprovementSuggestionsPrompt(String originalScript, String spokenTranscript) {
        return "You are an expert presentation coach. " +
                "Compare the original script with the spoken transcript and provide 3-5 specific, " +
                "actionable suggestions for improvement. Focus on pacing, clarity, filler words, " +
                "and content accuracy. Make each suggestion concrete and implementable. " +
                "\n\nOriginal Script:\n" + originalScript +
                "\n\nSpoken Transcript:\n" + spokenTranscript +
                "\n\nPlease provide your suggestions in a JSON array format:" +
                "\n[" +
                "\n  \"[First improvement suggestion]\"," +
                "\n  \"[Second improvement suggestion]\"," +
                "\n  \"[Third improvement suggestion]\"" +
                "\n]";
    }

    private String createScoringPrompt(String originalScript, String spokenTranscript) {
        return "You are an expert presentation coach. " +
                "Compare the original script with the spoken transcript and provide a numerical score " +
                "between 1 and 10, where 1 is poor and 10 is excellent. " +
                "Consider pacing, clarity, filler words, content accuracy, and overall delivery. " +
                "\n\nOriginal Script:\n" + originalScript +
                "\n\nSpoken Transcript:\n" + spokenTranscript +
                "\n\nPlease provide only a numerical score between 1 and 10 in the following format:" +
                "\n{" +
                "\n  \"score\": [numerical score]" +
                "\n}";
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("candidates")) {
            throw new GeminiApiException("Invalid response from Gemini API: missing 'candidates'", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates.isEmpty()) {
            if (response.containsKey("promptFeedback")) {
                log.warn("Gemini response blocked. Feedback: {}", response.get("promptFeedback"));
                throw new GeminiApiException("Request blocked due to safety settings.", HttpStatus.BAD_REQUEST);
            }
            throw new GeminiApiException("No content returned from Gemini API.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }

    private String cleanJsonString(String rawText) {
        int startIndex = rawText.indexOf('{');
        int endIndex = rawText.lastIndexOf('}');

        if (startIndex == -1 || endIndex == -1) {
            startIndex = rawText.indexOf('[');
            endIndex = rawText.lastIndexOf(']');
        }

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return rawText.substring(startIndex, endIndex + 1);
        }

        log.warn("Could not find a valid JSON object or array in the response text: {}", rawText);
        throw new GeminiApiException("Malformed JSON content in Gemini response", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private AnalysisResponse parseAnalysisResponse(Map<String, Object> response) {
        try {
            String text = extractTextFromResponse(response);
            String jsonContent = cleanJsonString(text);
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            return new AnalysisResponse.Builder()
                    .score(rootNode.path("score").asInt(0))
                    .positiveFeedback(rootNode.path("positiveFeedback").asText())
                    .improvementPoints(rootNode.path("improvementPoints").asText())
                    .build();
        } catch (Exception e) {
            log.error("Error parsing Gemini API analysis response: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to parse analysis response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private List<String> parseImprovementSuggestions(Map<String, Object> response) {
        try {
            String text = extractTextFromResponse(response);
            String jsonContent = cleanJsonString(text);
            return objectMapper.readValue(jsonContent, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("Error parsing improvement suggestions: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to parse improvement suggestions", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private int parseDeliveryScore(Map<String, Object> response) {
        try {
            String text = extractTextFromResponse(response);
            String jsonContent = cleanJsonString(text);
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            int score = rootNode.path("score").asInt(0);
            return Math.max(1, Math.min(10, score));
        } catch (Exception e) {
            log.error("Error parsing delivery score: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to parse delivery score", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            HttpStatus status = HttpStatus.valueOf(((WebClientResponseException) throwable).getStatusCode().value());
            return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
        }
        return throwable instanceof TimeoutException;
    }

    private String createVoiceAnalysisPrompt(String speechText, String presentationType,
                                             String targetAudience, VoiceGender preferredGender) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an expert speech coach and voice analyst. Analyze the following speech text and recommend the most suitable voice characteristics for optimal delivery improvement.\n\n");

        prompt.append("Speech Text:\n").append(speechText).append("\n\n");
        prompt.append("Presentation Type: ").append(presentationType != null ? presentationType : "general").append("\n");
        prompt.append("Target Audience: ").append(targetAudience != null ? targetAudience : "general").append("\n");
        if (preferredGender != null) {
            prompt.append("Preferred Gender: ").append(preferredGender.getValue()).append("\n");
        }

        prompt.append("\nAvailable Voice Tones:\n");
        prompt.append("- CONFIDENT: Strong, assured tone for authority and leadership\n");
        prompt.append("- CONVERSATIONAL: Natural, friendly tone for casual engagement\n");
        prompt.append("- URGENT: Energetic, pressing tone for importance and immediacy\n");
        prompt.append("- EMPATHETIC: Warm, understanding tone for care and compassion\n\n");

        prompt.append("Available Voice Options (with their primary tones):\n");
        for (MurfVoice voice : MurfVoice.values()) {
            prompt.append("- ").append(voice.getVoiceId().toUpperCase())
                    .append(" (").append(voice.getGender().getValue())
                    .append(", ").append(voice.getAge().getValue())
                    .append(", ").append(voice.getPrimaryTone().getValue())
                    .append("): ").append(voice.getDescription()).append("\n");
        }

        prompt.append("\nBased on your analysis, provide your recommendation in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"detectedTone\": \"[current tone you detect in the speech - one of: confident, conversational, urgent, empathetic, neutral, hesitant, monotone]\",\n");
        prompt.append("  \"recommendedTone\": \"[recommended tone from: confident, conversational, urgent, empathetic]\",\n");
        prompt.append("  \"recommendedVoiceId\": \"[voice ID from the available options above]\",\n");
        prompt.append("  \"analysisReason\": \"[2-3 sentences explaining why this voice would help improve the delivery]\",\n");
        prompt.append("  \"improvementAreas\": [\"[area 1]\", \"[area 2]\", \"[area 3]\"],\n");
        prompt.append("  \"confidenceScore\": [0.0-1.0 confidence in recommendation]\n");
        prompt.append("}\n\n");
        prompt.append("Consider factors like: speech content complexity, emotional tone, target audience appropriateness, and areas where the speaker could improve their delivery impact.");

        return prompt.toString();
    }

    private VoiceAnalysis parseVoiceAnalysisResponse(Map<String, Object> response, String speechText) {
        try {
            String text = extractTextFromResponse(response);
            String jsonContent = cleanJsonString(text);
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            String detectedTone = rootNode.path("detectedTone").asText();
            String recommendedToneStr = rootNode.path("recommendedTone").asText();
            String recommendedVoiceId = rootNode.path("recommendedVoiceId").asText();
            String analysisReason = rootNode.path("analysisReason").asText();
            double confidenceScore = rootNode.path("confidenceScore").asDouble(0.8);

            // Parse improvement areas
            List<String> improvementAreas = objectMapper.convertValue(
                    rootNode.path("improvementAreas"),
                    new TypeReference<List<String>>() {}
            );

            // Convert recommended tone to enum
            ToneType recommendedTone;
            try {
                recommendedTone = ToneType.fromValue(recommendedToneStr);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid recommended tone '{}', defaulting to CONVERSATIONAL", recommendedToneStr);
                recommendedTone = ToneType.CONVERSATIONAL;
            }

            // Find recommended voice
            MurfVoice recommendedVoice;
            try {
                recommendedVoice = MurfVoice.fromVoiceId(recommendedVoiceId.toLowerCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid voice ID '{}', selecting default voice for tone {}", recommendedVoiceId, recommendedTone);
                MurfVoice[] voicesForTone = MurfVoice.getVoicesByTone(recommendedTone);
                recommendedVoice = voicesForTone.length > 0 ? voicesForTone[0] : MurfVoice.ALEX;
            }

            return new VoiceAnalysis(
                    UUID.randomUUID().toString(),
                    speechText,
                    detectedTone,
                    recommendedTone.getValue(),
                    recommendedVoice,
                    analysisReason,
                    improvementAreas,
                    confidenceScore
            );

        } catch (Exception e) {
            log.error("Error parsing voice analysis response: {}", e.getMessage(), e);
            throw new GeminiApiException("Failed to parse voice analysis response",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
