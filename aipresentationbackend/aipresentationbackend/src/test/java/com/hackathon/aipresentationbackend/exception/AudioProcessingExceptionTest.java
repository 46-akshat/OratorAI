package com.hackathon.aipresentationbackend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class AudioProcessingExceptionTest {

    @Test
    void constructor_WithMessageAndStatus_SetsProperties() {
        // Arrange & Act
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        AudioProcessingException exception = new AudioProcessingException(message, status);
        
        // Assert
        assertEquals(message, exception.getReason());
        assertEquals(status, exception.getStatusCode());
    }
    
    @Test
    void constructor_WithMessageStatusAndCause_SetsProperties() {
        // Arrange & Act
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Throwable cause = new RuntimeException("Original cause");
        AudioProcessingException exception = new AudioProcessingException(message, status, cause);
        
        // Assert
        assertEquals(message, exception.getReason());
        assertEquals(status, exception.getStatusCode());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void getErrorCode_UnsupportedMediaType_ReturnsCorrectCode() {
        // Arrange
        AudioProcessingException exception = new AudioProcessingException(
                "Unsupported format", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("AUDIO_UNSUPPORTED_FORMAT", errorCode);
    }
    
    @Test
    void getErrorCode_PayloadTooLarge_ReturnsCorrectCode() {
        // Arrange
        AudioProcessingException exception = new AudioProcessingException(
                "File too large", HttpStatus.PAYLOAD_TOO_LARGE);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("AUDIO_FILE_TOO_LARGE", errorCode);
    }
    
    @Test
    void getErrorCode_BadRequest_ReturnsCorrectCode() {
        // Arrange
        AudioProcessingException exception = new AudioProcessingException(
                "Invalid request", HttpStatus.BAD_REQUEST);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("AUDIO_PROCESSING_ERROR", errorCode);
    }
    
    @Test
    void getErrorCode_InternalServerError_ReturnsCorrectCode() {
        // Arrange
        AudioProcessingException exception = new AudioProcessingException(
                "System error", HttpStatus.INTERNAL_SERVER_ERROR);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("AUDIO_SYSTEM_ERROR", errorCode);
    }
    
    @Test
    void getErrorCode_OtherStatus_ReturnsDefaultCode() {
        // Arrange
        AudioProcessingException exception = new AudioProcessingException(
                "Other error", HttpStatus.SERVICE_UNAVAILABLE);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("AUDIO_PROCESSING_ERROR", errorCode);
    }
}