package com.hackathon.aipresentationbackend.exception;

import org.springframework.http.ResponseEntity;
import com.hackathon.aipresentationbackend.model.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handler for custom murf ai exception
    @ExceptionHandler(MurfApiException.class)
    public ResponseEntity<ErrorResponse> handleMurfApiException(MurfApiException e){
          ErrorResponse errorResponse=new ErrorResponse();
          errorResponse.setTimestamp(LocalDateTime.now());
          errorResponse.setStatus(e.getStatusCode().value());
          errorResponse.setError("Murf Api Error");
          errorResponse.setErrorCode(e.getErrorCode());
          errorResponse.setMessage(e.getReason());

          return new ResponseEntity<>(errorResponse,e.getStatusCode());
    }
    @ExceptionHandler(GeminiApiException.class)
    public ResponseEntity<ErrorResponse> handleGeminiApiException(GeminiApiException e){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(e.getStatusCode().value());
        errorResponse.setError("Gemini Api Error");
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getReason());

        return new ResponseEntity<>(errorResponse,e.getStatusCode());
    }
    @ExceptionHandler(AssemblyAIException.class)
    public ResponseEntity<ErrorResponse> handleAssemblyApiException(AssemblyAIException e){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(e.getStatusCode().value());
        errorResponse.setError("Assembly Api Error");
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getReason());

        return new ResponseEntity<>(errorResponse,e.getStatusCode());
    }
    @ExceptionHandler(AudioProcessingException.class)
    public ResponseEntity<ErrorResponse> handleAudioProcessingException(AudioProcessingException e){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(e.getStatusCode().value());
        errorResponse.setError("Audio Processing Error");
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getReason());

        return new ResponseEntity<>(errorResponse,e.getStatusCode());
    }




}
