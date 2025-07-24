package com.hackathon.aipresentationbackend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class AssemblyAIExceptionTest {

    @Test
    void constructor_WithMessageAndStatus_SetsMessageAndStatus() {
        // Arrange & Act
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        AssemblyAIException exception = new AssemblyAIException(message, status);
        
        // Assert
        assertEquals(message, exception.getReason());
        assertEquals(status, exception.getStatusCode());
    }
    
    @Test
    void constructor_WithMessageStatusAndCause_SetsMessageStatusAndCause() {
        // Arrange & Act
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Throwable cause = new RuntimeException("Original error");
        AssemblyAIException exception = new AssemblyAIException(message, status, cause);
        
        // Assert
        assertEquals(message, exception.getReason());
        assertEquals(status, exception.getStatusCode());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void getErrorCode_WithUnauthorizedStatus_ReturnsUnauthorizedCode() {
        // Arrange
        AssemblyAIException exception = new AssemblyAIException("Unauthorized", HttpStatus.UNAUTHORIZED);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("ASSEMBLY_AI_UNAUTHORIZED", errorCode);
    }
    
    @Test
    void getErrorCode_WithTooManyRequestsStatus_ReturnsRateLimitCode() {
        // Arrange
        AssemblyAIException exception = new AssemblyAIException("Rate limited", HttpStatus.TOO_MANY_REQUESTS);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("ASSEMBLY_AI_RATE_LIMIT", errorCode);
    }
    
    @Test
    void getErrorCode_With5xxStatus_ReturnsServerErrorCode() {
        // Arrange
        AssemblyAIException exception = new AssemblyAIException("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("ASSEMBLY_AI_SERVER_ERROR", errorCode);
    }
    
    @Test
    void getErrorCode_With4xxStatus_ReturnsClientErrorCode() {
        // Arrange
        AssemblyAIException exception = new AssemblyAIException("Client error", HttpStatus.BAD_REQUEST);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("ASSEMBLY_AI_CLIENT_ERROR", errorCode);
    }
    
    @Test
    void getErrorCode_WithOtherStatus_ReturnsGenericErrorCode() {
        // Arrange
        AssemblyAIException exception = new AssemblyAIException("Other error", HttpStatus.OK);
        
        // Act
        String errorCode = exception.getErrorCode();
        
        // Assert
        assertEquals("ASSEMBLY_AI_ERROR", errorCode);
    }
}