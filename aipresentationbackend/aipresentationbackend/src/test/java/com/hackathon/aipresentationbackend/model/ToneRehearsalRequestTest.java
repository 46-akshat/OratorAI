package com.hackathon.aipresentationbackend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ToneRehearsalRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidToneRehearsalRequest() {
        ToneRehearsalRequest request = new ToneRehearsalRequest(
                "This is a test sentence.", 
                "voice-1", 
                "confident"
        );
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidToneRehearsalRequestWithAllTones() {
        String[] validTones = {"confident", "conversational", "urgent", "empathetic"};
        
        for (String tone : validTones) {
            ToneRehearsalRequest request = new ToneRehearsalRequest(
                    "This is a test sentence.", 
                    "voice-1", 
                    tone
            );
            
            Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
            
            assertTrue(violations.isEmpty(), "Validation should pass for tone: " + tone);
        }
    }

    @Test
    void testBlankSentenceValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("", "voice-1", "confident");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Sentence is required")));
    }

    @Test
    void testNullSentenceValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest(null, "voice-1", "confident");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Sentence is required")));
    }

    @Test
    void testSentenceTooLongValidation() {
        String longSentence = "a".repeat(1001);
        ToneRehearsalRequest request = new ToneRehearsalRequest(longSentence, "voice-1", "confident");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Sentence must not exceed 1000 characters")));
    }

    @Test
    void testBlankVoiceIdValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "", "confident");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Voice ID is required")));
    }

    @Test
    void testNullVoiceIdValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", null, "confident");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Voice ID is required")));
    }

    @Test
    void testBlankToneValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", "");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(2, violations.size()); // Both @NotBlank and custom validation fail
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Tone is required")));
    }

    @Test
    void testNullToneValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", null);
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Tone is required")));
    }

    @Test
    void testInvalidToneValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", "invalid");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("Tone must be one of: confident, conversational, urgent, empathetic")));
    }

    @Test
    void testGetToneType() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", "confident");
        
        assertEquals(ToneType.CONFIDENT, request.getToneType());
    }

    @Test
    void testGetToneTypeWithInvalidTone() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", "invalid");
        
        assertThrows(IllegalArgumentException.class, request::getToneType);
    }

    @Test
    void testGetToneTypeWithNullTone() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", null);
        
        assertNull(request.getToneType());
    }

    @Test
    void testSetToneType() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        request.setToneType(ToneType.EMPATHETIC);
        
        assertEquals("empathetic", request.getTone());
        assertEquals(ToneType.EMPATHETIC, request.getToneType());
    }

    @Test
    void testSetToneTypeNull() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        request.setToneType(null);
        
        assertNull(request.getTone());
    }

    @Test
    void testConstructorWithToneType() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        request.setSentence("Test sentence");
        request.setVoiceId("voice-1");
        request.setToneType(ToneType.URGENT);
        
        assertEquals("Test sentence", request.getSentence());
        assertEquals("voice-1", request.getVoiceId());
        assertEquals("urgent", request.getTone());
        assertEquals(ToneType.URGENT, request.getToneType());
    }

    @Test
    void testConstructorWithNullToneType() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        request.setSentence("Test sentence");
        request.setVoiceId("voice-1");
        request.setToneType(null);
        
        assertEquals("Test sentence", request.getSentence());
        assertEquals("voice-1", request.getVoiceId());
        assertNull(request.getTone());
    }

    @Test
    void testNoArgsConstructor() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        
        assertNull(request.getSentence());
        assertNull(request.getVoiceId());
        assertNull(request.getTone());
    }

    @Test
    void testCaseInsensitiveToneValidation() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("Test sentence", "voice-1", "CONFIDENT");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
        assertEquals(ToneType.CONFIDENT, request.getToneType());
    }

    @Test
    void testMultipleValidationErrors() {
        ToneRehearsalRequest request = new ToneRehearsalRequest("", null, "invalid");
        
        Set<ConstraintViolation<ToneRehearsalRequest>> violations = validator.validate(request);
        
        assertEquals(3, violations.size());
    }

    @Test
    void testSettersAndGetters() {
        ToneRehearsalRequest request = new ToneRehearsalRequest();
        
        request.setSentence("New test sentence");
        request.setVoiceId("voice-2");
        request.setTone("conversational");
        
        assertEquals("New test sentence", request.getSentence());
        assertEquals("voice-2", request.getVoiceId());
        assertEquals("conversational", request.getTone());
        assertEquals(ToneType.CONVERSATIONAL, request.getToneType());
    }
}