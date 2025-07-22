package com.hackathon.aipresentationbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when there is an error processing audio files
 */
public class AudioProcessingException extends ResponseStatusException {
    
    /**
     * Create a new AudioProcessingException with a message and status
     * 
     * @param message The error message
     * @param status The HTTP status code
     */
    public AudioProcessingException(String message, HttpStatus status) {
        super(status, message);
    }
    
    /**
     * Create a new AudioProcessingException with a message, status, and cause
     * 
     * @param message The error message
     * @param status The HTTP status code
     * @param cause The cause of the exception
     */
    public AudioProcessingException(String message, HttpStatus status, Throwable cause) {
        super(status, message, cause);
    }
    
    /**
     * Get a descriptive error code based on the status
     * 
     * @return A string error code
     */
    public String getErrorCode() {
        HttpStatus status = (HttpStatus) this.getStatusCode();
        
        if (status == HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
            return "AUDIO_UNSUPPORTED_FORMAT";
        } else if (status == HttpStatus.PAYLOAD_TOO_LARGE) {
            return "AUDIO_FILE_TOO_LARGE";
        } else if (status == HttpStatus.BAD_REQUEST) {
            return "AUDIO_PROCESSING_ERROR";
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            return "AUDIO_SYSTEM_ERROR";
        } else {
            return "AUDIO_PROCESSING_ERROR";
        }
    }
}