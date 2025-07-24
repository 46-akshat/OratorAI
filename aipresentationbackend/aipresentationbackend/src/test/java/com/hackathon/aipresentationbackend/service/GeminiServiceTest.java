package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.GeminiApiException;
import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeminiServiceTest {

    @Mock
    private WebClient webClient;
    
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;
    
    @InjectMocks
    private GeminiService geminiService;
    
    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(geminiService, "geminiApiKey", "test-api-key");
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
    }
    
    @Test
    public void testAnalyzePresentation_Success() {
        // Prepare mock response
        Map<String, Object> responseMap = createSuccessfulAnalysisResponse();
        
        // Configure mocks
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));
        
        // Call the service
        AnalysisResponse response = geminiService.analyzePresentation(
                "This is the original script.", 
                "This is what was actually spoken.");
        
        // Verify the response
        assertNotNull(response);
        assertEquals(8, response.getScore());
        assertEquals("Your delivery was clear and confident", response.getPositiveFeedback());
        assertEquals("Try to maintain a more consistent pace", response.getImprovementPoints());
        
        // Verify the API was called
        verify(webClient).post();
        verify(requestBodySpec).bodyValue(any());
    }
    
    @Test
    public void testAnalyzePresentation_ApiError() {
        // Configure mocks to throw an exception
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(
                Mono.error(new WebClientResponseException(
                        HttpStatus.UNAUTHORIZED.value(), 
                        "Unauthorized", 
                        null, null, null)));
        
        // Call the service and verify exception
        GeminiApiException exception = assertThrows(GeminiApiException.class, () -> {
            geminiService.analyzePresentation(
                    "This is the original script.", 
                    "This is what was actually spoken.");
        });
        
        // Verify the exception details
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Failed to analyze presentation"));
        assertEquals("GEMINI_API_UNAUTHORIZED", exception.getErrorCode());
    }
    
    @Test
    public void testCalculateDeliveryScore_Success() {
        // Prepare mock response
        Map<String, Object> responseMap = createSuccessfulScoreResponse();
        
        // Configure mocks
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));
        
        // Call the service
        int score = geminiService.calculateDeliveryScore(
                "This is the original script.", 
                "This is what was actually spoken.");
        
        // Verify the response
        assertEquals(7, score);
        
        // Verify the API was called
        verify(webClient).post();
        verify(requestBodySpec).bodyValue(any());
    }
    
    @Test
    public void testGenerateImprovementSuggestions_Success() {
        // Prepare mock response
        Map<String, Object> responseMap = createSuccessfulSuggestionsResponse();
        
        // Configure mocks
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));
        
        // Call the service
        List<String> suggestions = geminiService.generateImprovementSuggestions(
                "This is the original script.", 
                "This is what was actually spoken.");
        
        // Verify the response
        assertNotNull(suggestions);
        assertEquals(3, suggestions.size());
        assertEquals("Practice maintaining a consistent pace", suggestions.get(0));
        assertEquals("Reduce filler words like 'um' and 'uh'", suggestions.get(1));
        assertEquals("Emphasize key points with vocal variety", suggestions.get(2));
        
        // Verify the API was called
        verify(webClient).post();
        verify(requestBodySpec).bodyValue(any());
    }
    
    /**
     * Create a mock successful response for analysis
     */
    private Map<String, Object> createSuccessfulAnalysisResponse() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> candidates = new ArrayList<>();
        Map<String, Object> candidate = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();
        
        part.put("text", "{\n  \"score\": 8,\n  \"positiveFeedback\": \"Your delivery was clear and confident\",\n  \"improvementPoints\": \"Try to maintain a more consistent pace\"\n}");
        parts.add(part);
        
        content.put("parts", parts);
        candidate.put("content", content);
        candidates.add(candidate);
        
        responseMap.put("candidates", candidates);
        
        return responseMap;
    }
    
    /**
     * Create a mock successful response for score
     */
    private Map<String, Object> createSuccessfulScoreResponse() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> candidates = new ArrayList<>();
        Map<String, Object> candidate = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();
        
        part.put("text", "{\n  \"score\": 7\n}");
        parts.add(part);
        
        content.put("parts", parts);
        candidate.put("content", content);
        candidates.add(candidate);
        
        responseMap.put("candidates", candidates);
        
        return responseMap;
    }
    
    /**
     * Create a mock successful response for suggestions
     */
    private Map<String, Object> createSuccessfulSuggestionsResponse() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> candidates = new ArrayList<>();
        Map<String, Object> candidate = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();
        
        part.put("text", "[\n  \"Practice maintaining a consistent pace\",\n  \"Reduce filler words like 'um' and 'uh'\",\n  \"Emphasize key points with vocal variety\"\n]");
        parts.add(part);
        
        content.put("parts", parts);
        candidate.put("content", content);
        candidates.add(candidate);
        
        responseMap.put("candidates", candidates);
        
        return responseMap;
    }
}