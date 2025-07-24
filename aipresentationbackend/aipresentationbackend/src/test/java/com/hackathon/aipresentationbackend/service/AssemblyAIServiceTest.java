package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.AssemblyAIException;
import com.hackathon.aipresentationbackend.model.AudioUploadResponse;
import com.hackathon.aipresentationbackend.model.TranscriptionRequest;
import com.hackathon.aipresentationbackend.model.TranscriptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssemblyAIServiceTest {

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
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private AssemblyAIService assemblyAIService;

    private final String API_KEY = "test-api-key";
    private final String UPLOAD_URL = "https://some-upload-url.com/audio.mp3";
    private final String TRANSCRIPTION_ID = "test-transcription-id";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(assemblyAIService, "assemblyApiKey", API_KEY);

        // Common mocking setup for the WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void transcribeAudio_Success() throws IOException {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3", "test audio content".getBytes());

        // 1. Mock the Upload Response
        AudioUploadResponse uploadResponse = new AudioUploadResponse(UPLOAD_URL);
        when(requestBodySpec.bodyValue(any(byte[].class))).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenReturn(Mono.just(uploadResponse));

        // 2. Mock the Transcription Submission Response
        TranscriptionResponse initialTranscriptResponse = new TranscriptionResponse();
        initialTranscriptResponse.setId(TRANSCRIPTION_ID);
        initialTranscriptResponse.setStatus("queued");

        // 3. Mock the Final Polling Response
        TranscriptionResponse finalTranscriptResponse = new TranscriptionResponse();
        finalTranscriptResponse.setId(TRANSCRIPTION_ID);
        finalTranscriptResponse.setStatus("completed");
        finalTranscriptResponse.setText("This is the final transcript.");

        // Chain the responses for the /transcript endpoint
        when(responseSpec.bodyToMono(TranscriptionResponse.class))
                .thenReturn(Mono.just(initialTranscriptResponse)) // First call (submission)
                .thenReturn(Mono.just(finalTranscriptResponse));  // Second call (polling)

        // Act
        String result = assemblyAIService.transcribeAudio(audioFile);

        // Assert
        assertNotNull(result);
        assertEquals("This is the final transcript.", result);
    }

    @Test
    void transcribeAudio_UploadFails() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3", "test audio content".getBytes());

        // Mock the upload step to return an error
        when(requestBodySpec.bodyValue(any(byte[].class))).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenReturn(Mono.error(new RuntimeException("Upload failed")));

        // Act & Assert
        AssemblyAIException thrown = assertThrows(AssemblyAIException.class, () -> {
            assemblyAIService.transcribeAudio(audioFile);
        });

        assertTrue(thrown.getMessage().contains("An unexpected error occurred during transcription."));
    }

    @Test
    void transcribeAudio_PollingFailsWithErrorStatus() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3", "test audio content".getBytes());

        // Mock successful upload
        AudioUploadResponse uploadResponse = new AudioUploadResponse(UPLOAD_URL);
        when(requestBodySpec.bodyValue(any(byte[].class))).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenReturn(Mono.just(uploadResponse));

        // Mock successful submission
        TranscriptionResponse initialTranscriptResponse = new TranscriptionResponse();
        initialTranscriptResponse.setId(TRANSCRIPTION_ID);
        initialTranscriptResponse.setStatus("queued");

        // Mock polling returns an error status
        TranscriptionResponse errorResponse = new TranscriptionResponse();
        errorResponse.setId(TRANSCRIPTION_ID);
        errorResponse.setStatus("error");
        errorResponse.setError("Invalid audio file.");

        when(responseSpec.bodyToMono(TranscriptionResponse.class))
                .thenReturn(Mono.just(initialTranscriptResponse)) // Submission
                .thenReturn(Mono.just(errorResponse));          // Polling result

        // Act & Assert
        AssemblyAIException thrown = assertThrows(AssemblyAIException.class, () -> {
            assemblyAIService.transcribeAudio(audioFile);
        });

        assertTrue(thrown.getMessage().contains("Transcription failed: Invalid audio file."));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());
    }
}
