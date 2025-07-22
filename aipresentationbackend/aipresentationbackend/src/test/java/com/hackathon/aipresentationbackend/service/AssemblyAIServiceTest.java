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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssemblyAIServiceTest {

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
    private AssemblyAIService assemblyAIService;
    
    private final String API_KEY = "test-api-key";
    private final String UPLOAD_URL = "https://api.assemblyai.com/v2/upload";
    private final String AUDIO_URL = "https://storage.assemblyai.com/test-audio.mp3";
    private final String TRANSCRIPTION_ID = "test-transcription-id";
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(assemblyAIService, "assemblyApiKey", API_KEY);
    }
    
    @Test
    void uploadAudioFile_Success() throws IOException {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3",
                "test-audio.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
        
        AudioUploadResponse uploadResponse = new AudioUploadResponse(UPLOAD_URL);
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenReturn(Mono.just(uploadResponse));
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq(UPLOAD_URL))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(AUDIO_URL));
        
        // Act
        String result = assemblyAIService.uploadAudioFile(audioFile);
        
        // Assert
        assertEquals(AUDIO_URL, result);
        verify(webClient, times(2)).post();
    }
    
    @Test
    void uploadAudioFile_ApiError() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3",
                "test-audio.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
        
        WebClientResponseException exception = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                HttpHeaders.EMPTY,
                "Invalid API key".getBytes(),
                null
        );
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenThrow(exception);
        
        // Act & Assert
        AssemblyAIException thrown = assertThrows(AssemblyAIException.class, () -> {
            assemblyAIService.uploadAudioFile(audioFile);
        });
        
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
        assertTrue(thrown.getMessage().contains("Failed to upload audio file"));
    }
    
    @Test
    void submitTranscriptionRequest_Success() {
        // Arrange
        TranscriptionRequest request = new TranscriptionRequest(AUDIO_URL);
        
        TranscriptionResponse response = new TranscriptionResponse();
        response.setId(TRANSCRIPTION_ID);
        response.setStatus("queued");
        response.setAudioUrl(AUDIO_URL);
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.CONTENT_TYPE), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(request)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(response));
        
        // Act
        TranscriptionResponse result = assemblyAIService.submitTranscriptionRequest(request);
        
        // Assert
        assertEquals(TRANSCRIPTION_ID, result.getId());
        assertEquals("queued", result.getStatus());
        assertEquals(AUDIO_URL, result.getAudioUrl());
    }
    
    @Test
    void pollTranscriptionStatus_CompletedOnFirstAttempt() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setId(TRANSCRIPTION_ID);
        response.setStatus("completed");
        response.setText("This is the transcribed text.");
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(response));
        
        // Act
        TranscriptionResponse result = assemblyAIService.pollTranscriptionStatus(TRANSCRIPTION_ID);
        
        // Assert
        assertEquals(TRANSCRIPTION_ID, result.getId());
        assertEquals("completed", result.getStatus());
        assertEquals("This is the transcribed text.", result.getText());
    }
    
    @Test
    void pollTranscriptionStatus_CompletedAfterPolling() {
        // Arrange
        TranscriptionResponse processingResponse = new TranscriptionResponse();
        processingResponse.setId(TRANSCRIPTION_ID);
        processingResponse.setStatus("processing");
        
        TranscriptionResponse completedResponse = new TranscriptionResponse();
        completedResponse.setId(TRANSCRIPTION_ID);
        completedResponse.setStatus("completed");
        completedResponse.setText("This is the transcribed text.");
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // First return processing, then completed
        when(responseSpec.bodyToMono(TranscriptionResponse.class))
                .thenReturn(Mono.just(processingResponse))
                .thenReturn(Mono.just(completedResponse));
        
        // Act
        TranscriptionResponse result = assemblyAIService.pollTranscriptionStatus(TRANSCRIPTION_ID);
        
        // Assert
        assertEquals(TRANSCRIPTION_ID, result.getId());
        assertEquals("completed", result.getStatus());
        assertEquals("This is the transcribed text.", result.getText());
        verify(webClient, times(2)).get();
    }
    
    @Test
    void pollTranscriptionStatus_Error() {
        // Arrange
        TranscriptionResponse errorResponse = new TranscriptionResponse();
        errorResponse.setId(TRANSCRIPTION_ID);
        errorResponse.setStatus("error");
        errorResponse.setError("Audio file could not be processed");
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(errorResponse));
        
        // Act & Assert
        AssemblyAIException thrown = assertThrows(AssemblyAIException.class, () -> {
            assemblyAIService.pollTranscriptionStatus(TRANSCRIPTION_ID);
        });
        
        assertTrue(thrown.getMessage().contains("Audio file could not be processed"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }
    
    @Test
    void transcribeAudio_Success() throws IOException {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "test-audio.mp3",
                "test-audio.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
        
        // Mock uploadAudioFile
        AudioUploadResponse uploadResponse = new AudioUploadResponse(UPLOAD_URL);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(contains("/upload"))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AudioUploadResponse.class)).thenReturn(Mono.just(uploadResponse));
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq(UPLOAD_URL))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(AUDIO_URL));
        
        // Mock submitTranscriptionRequest
        TranscriptionResponse queuedResponse = new TranscriptionResponse();
        queuedResponse.setId(TRANSCRIPTION_ID);
        queuedResponse.setStatus("queued");
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(contains("/transcript"))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.CONTENT_TYPE), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(TranscriptionRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(queuedResponse));
        
        // Mock pollTranscriptionStatus
        TranscriptionResponse completedResponse = new TranscriptionResponse();
        completedResponse.setId(TRANSCRIPTION_ID);
        completedResponse.setStatus("completed");
        completedResponse.setText("This is the transcribed text.");
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(completedResponse));
        
        // Act
        String result = assemblyAIService.transcribeAudio(audioFile);
        
        // Assert
        assertEquals("This is the transcribed text.", result);
    }
    
    @Test
    void getTranscription_Success() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setId(TRANSCRIPTION_ID);
        response.setStatus("completed");
        response.setText("This is the transcribed text.");
        
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), eq(API_KEY))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TranscriptionResponse.class)).thenReturn(Mono.just(response));
        
        // Act
        TranscriptionResponse result = assemblyAIService.getTranscription(TRANSCRIPTION_ID);
        
        // Assert
        assertEquals(TRANSCRIPTION_ID, result.getId());
        assertEquals("completed", result.getStatus());
        assertEquals("This is the transcribed text.", result.getText());
    }
}