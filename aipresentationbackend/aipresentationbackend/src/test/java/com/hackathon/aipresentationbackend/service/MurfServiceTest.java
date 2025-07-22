package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.MurfApiException;
import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import com.hackathon.aipresentationbackend.model.ToneRehearsalRequest;
import com.hackathon.aipresentationbackend.model.ToneType;
import com.hackathon.aipresentationbackend.model.VoiceOption;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MurfServiceTest {

    @Mock
    private WebClient webClient;
    
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;
    
    @InjectMocks
    private MurfService murfService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(murfService, "murfApiKey", "test-api-key");
    }
    
    @Test
    void generateSpeech_Success() {
        // Arrange
        SpeechRequest request = new SpeechRequest(
                "This is a test speech",
                "en-US-julia",
                1.0,
                null
        );
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("audioFile", "https://example.com/audio.mp3");
        responseBody.put("duration", 10);
        
        // Mock WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseBody));
        when(responseSpec.retryWhen(any())).thenReturn(responseSpec);
        when(responseSpec.timeout(any())).thenReturn(responseSpec);
        when(responseSpec.doOnError(any(), any())).thenReturn(responseSpec);
        when(responseSpec.block()).thenReturn(responseBody);
        
        // Act
        SpeechResponse response = murfService.generateSpeech(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(10, response.getDuration());
        assertEquals("en-US-julia", response.getVoiceUsed());
        assertEquals(1.0, response.getSpeedUsed());
        assertTrue(response.hasAudioUrl());
        
        // Verify WebClient interactions
        verify(webClient).post();
        verify(requestBodyUriSpec).uri("https://api.murf.ai/v1/speech/generate");
        verify(requestBodySpec, times(3)).header(anyString(), anyString());
        verify(requestBodySpec).bodyValue(any());
        verify(requestHeadersSpec).retrieve();
    }
    
    @Test
    void generateSpeech_ApiError() {
        // Arrange
        SpeechRequest request = new SpeechRequest(
                "This is a test speech",
                "en-US-julia",
                1.0,
                null
        );
        
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                null,
                null,
                null
        );
        
        // Mock WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(exception));
        when(responseSpec.retryWhen(any())).thenReturn(responseSpec);
        when(responseSpec.timeout(any())).thenReturn(responseSpec);
        when(responseSpec.doOnError(any(), any())).thenReturn(responseSpec);
        when(responseSpec.block()).thenThrow(exception);
        
        // Act & Assert
        MurfApiException thrown = assertThrows(MurfApiException.class, () -> {
            murfService.generateSpeech(request);
        });
        
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
        assertTrue(thrown.getMessage().contains("Failed to generate speech"));
    }
    
    @Test
    void generateToneVariation_Success() {
        // Arrange
        ToneRehearsalRequest request = new ToneRehearsalRequest(
                "This is a test sentence",
                "en-US-julia",
                "confident"
        );
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("audioFile", "https://example.com/audio.mp3");
        responseBody.put("duration", 5);
        
        // Mock WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseBody));
        when(responseSpec.retryWhen(any())).thenReturn(responseSpec);
        when(responseSpec.timeout(any())).thenReturn(responseSpec);
        when(responseSpec.doOnError(any(), any())).thenReturn(responseSpec);
        when(responseSpec.block()).thenReturn(responseBody);
        
        // Act
        SpeechResponse response = murfService.generateToneVariation(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(5, response.getDuration());
        assertEquals("en-US-julia", response.getVoiceUsed());
        // Speed should be adjusted for confident tone
        assertEquals(1.1, response.getSpeedUsed());
    }
    
    @Test
    void getAvailableVoices_ReturnsVoiceList() {
        // Act
        List<VoiceOption> voices = murfService.getAvailableVoices();
        
        // Assert
        assertNotNull(voices);
        assertFalse(voices.isEmpty());
        
        // Check for expected voice types
        boolean hasMaleVoice = voices.stream().anyMatch(VoiceOption::isMale);
        boolean hasFemaleVoice = voices.stream().anyMatch(VoiceOption::isFemale);
        boolean hasVersatileVoice = voices.stream().anyMatch(v -> v.getSupportedToneCount() >= 4);
        
        assertTrue(hasMaleVoice, "Should have at least one male voice");
        assertTrue(hasFemaleVoice, "Should have at least one female voice");
        assertTrue(hasVersatileVoice, "Should have at least one versatile voice");
    }
    
    @Test
    void voiceSupportsTone_ReturnsCorrectResult() {
        // Act & Assert
        assertTrue(murfService.voiceSupportsTone("en-US-pro1", "confident"), 
                "Versatile voice should support confident tone");
        assertTrue(murfService.voiceSupportsTone("en-US-pro1", "empathetic"), 
                "Versatile voice should support empathetic tone");
        
        // Test with a voice that doesn't exist
        assertFalse(murfService.voiceSupportsTone("non-existent-voice", "confident"), 
                "Non-existent voice should not support any tone");
    }
}