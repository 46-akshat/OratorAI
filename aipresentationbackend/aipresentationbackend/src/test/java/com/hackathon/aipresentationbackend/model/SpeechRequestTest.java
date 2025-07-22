package com.hackathon.aipresentationbackend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpeechRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidSpeechRequest() {
        SpeechRequest request = new SpeechRequest("Hello world", "voice-1", 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidSpeechRequestWithTone() {
        SpeechRequest request = new SpeechRequest("Hello world", "voice-1", 1.5, "confident");
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankTextValidation() {
        SpeechRequest request = new SpeechRequest("", "voice-1", 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Text is required")));
    }

    @Test
    void testNullTextValidation() {
        SpeechRequest request = new SpeechRequest(null, "voice-1", 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Text is required")));
    }

    @Test
    void testTextTooLongValidation() {
        String longText = "a".repeat(10001);
        SpeechRequest request = new SpeechRequest(longText, "voice-1", 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Text must not exceed 10000 characters")));
    }

    @Test
    void testBlankVoiceIdValidation() {
        SpeechRequest request = new SpeechRequest("Hello world", "", 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Voice ID is required")));
    }

    @Test
    void testNullVoiceIdValidation() {
        SpeechRequest request = new SpeechRequest("Hello world", null, 1.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Voice ID is required")));
    }

    @Test
    void testSpeedTooLowValidation() {
        SpeechRequest request = new SpeechRequest("Hello world", "voice-1", 0.4, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Speed must be at least 0.5")));
    }

    @Test
    void testSpeedTooHighValidation() {
        SpeechRequest request = new SpeechRequest("Hello world", "voice-1", 2.1, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Speed must not exceed 2.0")));
    }

    @Test
    void testToneTooLongValidation() {
        String longTone = "a".repeat(51);
        SpeechRequest request = new SpeechRequest("Hello world", "voice-1", 1.0, longTone);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Tone must not exceed 50 characters")));
    }

    @Test
    void testDefaultSpeedValue() {
        SpeechRequest request = new SpeechRequest();
        
        assertEquals(1.0, request.getSpeed());
    }

    @Test
    void testMultipleValidationErrors() {
        SpeechRequest request = new SpeechRequest("", null, 3.0, null);
        
        Set<ConstraintViolation<SpeechRequest>> violations = validator.validate(request);
        
        assertEquals(3, violations.size());
    }
}