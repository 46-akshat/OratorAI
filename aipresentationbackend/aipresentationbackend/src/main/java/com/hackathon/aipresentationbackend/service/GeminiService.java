package com.hackathon.aipresentationbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.aipresentationbackend.exception.GeminiApiException;
import com.hackathon.aipresentationbackend.model.AnalysisResponse;
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

/**
 * Service for interacting with Google's Gemini AI API
 * Handles presentation analysis, feedback generation, and scoring
 */
@Service
public class GeminiService {
    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    private static final String GEMINI_API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private static final String GEMINI_MODEL = "models/gemini-1.5-pro";
    private static final String GENERATE_CONTENT_ENDPOINT = "/generateContent";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Analyze a presentation by comparing original script with spoken transcript
     *
     * @param originalScript The original presentation script
     * @param spokenTranscript The transcript of what was actually spoken
     * @return AnalysisResponse with feedback and score
     * @throws GeminiApiException if the API call fails
     */
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

    /**
     * Generate improvement suggestions based on presentation analysis
     *
     * @param originalScript The original presentation script
     * @param spokenTranscript The transcript of what was actually spoken
     * @return List of improvement suggestions
     * @throws GeminiApiException if the API call fails
     */
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

    /**
     * Calculate a numerical score for presentation delivery
     *
     * @param originalScript The original presentation script
     * @param spokenTranscript The transcript of what was actually spoken
     * @return Score from 1-10
     * @throws GeminiApiException if the API call fails
     */
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

    private Map<String, Object> callGeminiApi(String prompt) {
        log.debug("Calling Gemini API with prompt length: {}", prompt.length());

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));
        content.put("role", "user");
        requestBody.put("contents", List.of(content));

        String apiUrl = GEMINI_API_BASE_URL + "/" + GEMINI_MODEL + GENERATE_CONTENT_ENDPOINT + "?key=" + geminiApiKey;

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
        return "You are an expert presentation coach analyzing a presentation delivery. " +
                "Compare the original script with the spoken transcript and provide detailed feedback. " +
                "Focus on pacing, clarity, filler words, and content accuracy. " +
                "Provide specific positive feedback on strengths and actionable improvement suggestions. " +
                "\n\nOriginal Script:\n" + originalScript +
                "\n\nSpoken Transcript:\n" + spokenTranscript +
                "\n\nPlease provide your analysis in the following JSON format:" +
                "\n{" +
                "\n  \"score\": [numerical score between 1-10]," +
                "\n  \"positiveFeedback\": \"[detailed positive feedback highlighting strengths]\"," +
                "\n  \"improvementPoints\": \"[actionable improvement suggestions]\"" +
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
}