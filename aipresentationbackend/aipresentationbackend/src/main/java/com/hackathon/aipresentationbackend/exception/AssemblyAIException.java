package com.hackathon.aipresentationbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when there is an error with the AssemblyAI API
 */
public class AssemblyAIException extends ResponseStatusException {

    /**
     * Create a new AssemblyAIException with a message and status
     *
     * @param message The error message
     * @param status  The HTTP status code
     */
    public AssemblyAIException(String message, HttpStatus status) {
        super(status, message);
    }

    /**
     * Create a new AssemblyAIException with a message, status, and cause
     *
     * @param message The error message
     * @param status  The HTTP status code
     * @param cause   The cause of the exception
     */
    public AssemblyAIException(String message, HttpStatus status, Throwable cause) {
        super(status, message, cause);
    }



    /**
     * Get a descriptive error code based on the status
     *
     * @return A string error code
     */
    public String getErrorCode() {
        HttpStatusCode status = this.getStatusCode();

        if (status.equals(HttpStatus.UNAUTHORIZED)) {
            return "ASSEMBLY_AI_UNAUTHORIZED";
        } else if (status.equals(HttpStatus.TOO_MANY_REQUESTS)) {
            return "ASSEMBLY_AI_RATE_LIMIT";
        } else if (status.is5xxServerError()) {
            return "ASSEMBLY_AI_SERVER_ERROR";
        } else if (status.is4xxClientError()) {
            return "ASSEMBLY_AI_CLIENT_ERROR";
        } else {
            return "ASSEMBLY_AI_ERROR";
        }
    }
}