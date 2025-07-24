package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.MurfApiException;
import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import com.hackathon.aipresentationbackend.model.ToneRehearsalRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MurfServiceTest {

    @Mock
    private WebClient webClient;

    // Mocks for the fluent API of WebClient
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private MurfService murfService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(murfService, "murfApiKey", "test-api-key");

        // Common mocking setup for the WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
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

        // CORRECTED: Mock the Mono returned by bodyToMono
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseBody));

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

        // CORRECTED: Mock the Mono to return an error
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(exception));

        // Act & Assert
        MurfApiException thrown = assertThrows(MurfApiException.class, () -> {
            murfService.generateSpeech(request);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatusCode());
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

        // CORRECTED: Mock the Mono
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseBody));

        // Act
        SpeechResponse response = murfService.generateToneVariation(request);

        // Assert
        assertNotNull(response);
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(5, response.getDuration());
        assertEquals("en-US-julia", response.getVoiceUsed());
        assertEquals(1.1, response.getSpeedUsed());
    }

    @Test
    void getAvailableVoices_ReturnsVoiceList() {
        // Act
        List<VoiceOption> voices = murfService.getAvailableVoices();

        // Assert
        assertNotNull(voices);
        assertFalse(voices.isEmpty());
        assertTrue(voices.stream().anyMatch(VoiceOption::isMale));
        assertTrue(voices.stream().anyMatch(VoiceOption::isFemale));
    }

    @Test
    void voiceSupportsTone_ReturnsCorrectResult() {
        // Act & Assert
        assertTrue(murfService.voiceSupportsTone("en-US-marcus", "confident"));
        assertFalse(murfService.voiceSupportsTone("en-US-marcus", "empathetic"));
        assertFalse(murfService.voiceSupportsTone("non-existent-voice", "confident"));
    }
}
