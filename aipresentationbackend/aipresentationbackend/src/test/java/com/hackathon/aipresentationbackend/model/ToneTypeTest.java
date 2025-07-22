package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToneTypeTest {

    @Test
    void testEnumValues() {
        assertEquals("confident", ToneType.CONFIDENT.getValue());
        assertEquals("conversational", ToneType.CONVERSATIONAL.getValue());
        assertEquals("urgent", ToneType.URGENT.getValue());
        assertEquals("empathetic", ToneType.EMPATHETIC.getValue());
    }

    @Test
    void testEnumDescriptions() {
        assertEquals("A strong, assured tone that conveys certainty and authority", 
                ToneType.CONFIDENT.getDescription());
        assertEquals("A natural, friendly tone suitable for casual discussions", 
                ToneType.CONVERSATIONAL.getDescription());
        assertEquals("An energetic, pressing tone that conveys importance and immediacy", 
                ToneType.URGENT.getDescription());
        assertEquals("A warm, understanding tone that shows care and compassion", 
                ToneType.EMPATHETIC.getDescription());
    }

    @Test
    void testFromValueValidCases() {
        assertEquals(ToneType.CONFIDENT, ToneType.fromValue("confident"));
        assertEquals(ToneType.CONVERSATIONAL, ToneType.fromValue("conversational"));
        assertEquals(ToneType.URGENT, ToneType.fromValue("urgent"));
        assertEquals(ToneType.EMPATHETIC, ToneType.fromValue("empathetic"));
    }

    @Test
    void testFromValueCaseInsensitive() {
        assertEquals(ToneType.CONFIDENT, ToneType.fromValue("CONFIDENT"));
        assertEquals(ToneType.CONVERSATIONAL, ToneType.fromValue("Conversational"));
        assertEquals(ToneType.URGENT, ToneType.fromValue("URGENT"));
        assertEquals(ToneType.EMPATHETIC, ToneType.fromValue("Empathetic"));
    }

    @Test
    void testFromValueWithWhitespace() {
        assertEquals(ToneType.CONFIDENT, ToneType.fromValue("  confident  "));
        assertEquals(ToneType.CONVERSATIONAL, ToneType.fromValue(" conversational "));
        assertEquals(ToneType.URGENT, ToneType.fromValue("urgent "));
        assertEquals(ToneType.EMPATHETIC, ToneType.fromValue(" empathetic"));
    }

    @Test
    void testFromValueNull() {
        assertNull(ToneType.fromValue(null));
    }

    @Test
    void testFromValueInvalidThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> ToneType.fromValue("invalid")
        );
        
        assertTrue(exception.getMessage().contains("Invalid tone type: invalid"));
        assertTrue(exception.getMessage().contains("confident, conversational, urgent, empathetic"));
    }

    @Test
    void testFromValueEmptyStringThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> ToneType.fromValue("")
        );
        
        assertTrue(exception.getMessage().contains("Invalid tone type:"));
    }

    @Test
    void testIsValidToneValidCases() {
        assertTrue(ToneType.isValidTone("confident"));
        assertTrue(ToneType.isValidTone("conversational"));
        assertTrue(ToneType.isValidTone("urgent"));
        assertTrue(ToneType.isValidTone("empathetic"));
        assertTrue(ToneType.isValidTone("CONFIDENT"));
        assertTrue(ToneType.isValidTone("  conversational  "));
    }

    @Test
    void testIsValidToneInvalidCases() {
        assertFalse(ToneType.isValidTone("invalid"));
        assertFalse(ToneType.isValidTone(""));
        assertFalse(ToneType.isValidTone("happy"));
        assertFalse(ToneType.isValidTone("sad"));
    }

    @Test
    void testIsValidToneNull() {
        assertTrue(ToneType.isValidTone(null)); // null is valid (means no tone specified)
    }

    @Test
    void testAllEnumValuesHaveValidFromValue() {
        for (ToneType tone : ToneType.values()) {
            assertEquals(tone, ToneType.fromValue(tone.getValue()));
            assertTrue(ToneType.isValidTone(tone.getValue()));
        }
    }
}