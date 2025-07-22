package com.hackathon.aipresentationbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode; // Add this import
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when there is an error with the Murf API
 */
public class MurfApiException extends ResponseStatusException {

    /**
     * Create a new MurfApiException with a message and status
     * * @param message The error message
     * @param status The HTTP status code
     */
    public MurfApiException(String message, HttpStatus status) {
        super(status, message);
    }

    /**
     * Create a new MurfApiException with a message, status, and cause
     * * @param message The error message
     * @param status The HTTP status code
     * @param cause The cause of the exception
     */
    public MurfApiException(String message, HttpStatus status, Throwable cause) {
        super(status, message, cause);
    }

    /**
     * Get a descriptive error code based on the status
     * * @return A string error code
     */
    public String getErrorCode() {
        // Use the modern getStatusCode() method on the current instance
        HttpStatusCode status = this.getStatusCode();

        if (status.equals(HttpStatus.UNAUTHORIZED)) {
            return "MURF_API_UNAUTHORIZED";
        } else if (status.equals(HttpStatus.TOO_MANY_REQUESTS)) {
            return "MURF_API_RATE_LIMIT";
        } else if (status.is5xxServerError()) {
            return "MURF_API_SERVER_ERROR";
        } else if (status.is4xxClientError()) {
            return "MURF_API_CLIENT_ERROR";
        } else {
            return "MURF_API_ERROR";
        }
    }
}