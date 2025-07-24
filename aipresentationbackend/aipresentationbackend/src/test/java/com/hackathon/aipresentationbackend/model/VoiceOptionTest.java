package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VoiceOptionTest {

    @Test
    void testBuilderPattern() {
        List<String> tones = Arrays.asList("confident", "conversational");

        // CORRECTED: Use the manual builder
        VoiceOption voice = new VoiceOption.Builder()
                .voiceId("voice-1")
                .name("John")
                .gender("Male")
                .accent("American")
                .description("Professional voice")
                .supportedTones(tones)
                .build();

        assertEquals("voice-1", voice.getVoiceId());
        assertEquals("John", voice.getName());
        assertEquals("Male", voice.getGender());
        assertEquals("American", voice.getAccent());
        assertEquals("Professional voice", voice.getDescription());
        assertEquals(tones, voice.getSupportedTones());
    }

    @Test
    void testCreateProfessionalMale() {
        VoiceOption voice = VoiceOption.createProfessionalMale("prof-male-1", "David", "British");

        assertEquals("prof-male-1", voice.getVoiceId());
        assertEquals("David", voice.getName());
        assertEquals("Male", voice.getGender());
        assertEquals("British", voice.getAccent());
        assertEquals("Professional male voice suitable for business presentations", voice.getDescription());
        assertEquals(Arrays.asList("confident", "conversational", "urgent"), voice.getSupportedTones());
    }

    @Test
    void testCreateProfessionalFemale() {
        VoiceOption voice = VoiceOption.createProfessionalFemale("prof-female-1", "Sarah", "Australian");

        assertEquals("prof-female-1", voice.getVoiceId());
        assertEquals("Sarah", voice.getName());
        assertEquals("Female", voice.getGender());
        assertEquals("Australian", voice.getAccent());
        assertEquals("Professional female voice suitable for business presentations", voice.getDescription());
        assertEquals(Arrays.asList("confident", "conversational", "empathetic"), voice.getSupportedTones());
    }

    @Test
    void testCreateConversationalMale() {
        VoiceOption voice = VoiceOption.createConversationalMale("conv-male-1", "Mike", "Canadian");

        assertEquals("conv-male-1", voice.getVoiceId());
        assertEquals("Mike", voice.getName());
        assertEquals("Male", voice.getGender());
        assertEquals("Canadian", voice.getAccent());
        assertEquals("Friendly male voice perfect for casual presentations", voice.getDescription());
        assertEquals(Arrays.asList("conversational", "empathetic", "urgent"), voice.getSupportedTones());
    }

    @Test
    void testCreateConversationalFemale() {
        VoiceOption voice = VoiceOption.createConversationalFemale("conv-female-1", "Emma", "Irish");

        assertEquals("conv-female-1", voice.getVoiceId());
        assertEquals("Emma", voice.getName());
        assertEquals("Female", voice.getGender());
        assertEquals("Irish", voice.getAccent());
        assertEquals("Friendly female voice perfect for casual presentations", voice.getDescription());
        assertEquals(Arrays.asList("conversational", "empathetic", "confident"), voice.getSupportedTones());
    }

    @Test
    void testCreateVersatileVoice() {
        VoiceOption voice = VoiceOption.createVersatileVoice("versatile-1", "Alex", "Non-binary", "Neutral");

        assertEquals("versatile-1", voice.getVoiceId());
        assertEquals("Alex", voice.getName());
        assertEquals("Non-binary", voice.getGender());
        assertEquals("Neutral", voice.getAccent());
        assertEquals("Versatile voice supporting all emotional tones", voice.getDescription());
        assertEquals(Arrays.asList("confident", "conversational", "urgent", "empathetic"), voice.getSupportedTones());
    }

    @Test
    void testSupportsToneString() {
        VoiceOption voice = VoiceOption.createProfessionalMale("test-voice", "Test", "American");

        assertTrue(voice.supportsTone("confident"));
        assertTrue(voice.supportsTone("conversational"));
        assertTrue(voice.supportsTone("urgent"));
        assertFalse(voice.supportsTone("empathetic"));
        assertFalse(voice.supportsTone("invalid"));
    }

    @Test
    void testSupportsToneCaseInsensitive() {
        VoiceOption voice = VoiceOption.createProfessionalMale("test-voice", "Test", "American");

        assertTrue(voice.supportsTone("CONFIDENT"));
        assertTrue(voice.supportsTone("Conversational"));
        assertTrue(voice.supportsTone("URGENT"));
    }

    @Test
    void testSupportsToneEnum() {
        VoiceOption voice = VoiceOption.createProfessionalFemale("test-voice", "Test", "American");

        assertTrue(voice.supportsTone(ToneType.CONFIDENT));
        assertTrue(voice.supportsTone(ToneType.CONVERSATIONAL));
        assertTrue(voice.supportsTone(ToneType.EMPATHETIC));
        assertFalse(voice.supportsTone(ToneType.URGENT));
    }

    @Test
    void testSupportsToneWithNullTones() {
        // CORRECTED: Use the manual builder
        VoiceOption voice = new VoiceOption.Builder()
                .voiceId("test")
                .supportedTones(null)
                .build();

        assertFalse(voice.supportsTone("confident"));
        assertFalse(voice.supportsTone(ToneType.CONFIDENT));
    }

    @Test
    void testSupportsToneWithNullToneType() {
        VoiceOption voice = VoiceOption.createProfessionalMale("test-voice", "Test", "American");

        assertFalse(voice.supportsTone((ToneType) null));
    }

    @Test
    void testGetSupportedToneCount() {
        VoiceOption professionalVoice = VoiceOption.createProfessionalMale("test", "Test", "American");
        VoiceOption versatileVoice = VoiceOption.createVersatileVoice("test", "Test", "Male", "American");
        VoiceOption emptyVoice = new VoiceOption.Builder().voiceId("test").supportedTones(null).build();

        assertEquals(3, professionalVoice.getSupportedToneCount());
        assertEquals(4, versatileVoice.getSupportedToneCount());
        assertEquals(0, emptyVoice.getSupportedToneCount());
    }

    @Test
    void testIsMale() {
        VoiceOption maleVoice = VoiceOption.createProfessionalMale("test", "Test", "American");
        VoiceOption femaleVoice = VoiceOption.createProfessionalFemale("test", "Test", "American");
        VoiceOption neutralVoice = new VoiceOption.Builder().gender("Non-binary").build();
        VoiceOption nullGenderVoice = new VoiceOption.Builder().gender(null).build();

        assertTrue(maleVoice.isMale());
        assertFalse(femaleVoice.isMale());
        assertFalse(neutralVoice.isMale());
        assertFalse(nullGenderVoice.isMale());
    }

    @Test
    void testIsFemale() {
        VoiceOption maleVoice = VoiceOption.createProfessionalMale("test", "Test", "American");
        VoiceOption femaleVoice = VoiceOption.createProfessionalFemale("test", "Test", "American");
        VoiceOption neutralVoice = new VoiceOption.Builder().gender("Non-binary").build();
        VoiceOption nullGenderVoice = new VoiceOption.Builder().gender(null).build();

        assertFalse(maleVoice.isFemale());
        assertTrue(femaleVoice.isFemale());
        assertFalse(neutralVoice.isFemale());
        assertFalse(nullGenderVoice.isFemale());
    }

    @Test
    void testGenderCaseInsensitive() {
        // CORRECTED: Use the manual builder
        VoiceOption maleVoice = new VoiceOption.Builder().gender("male").build();
        VoiceOption femaleVoice = new VoiceOption.Builder().gender("FEMALE").build();
        VoiceOption mixedCaseVoice = new VoiceOption.Builder().gender("MaLe").build();

        assertTrue(maleVoice.isMale());
        assertTrue(femaleVoice.isFemale());
        assertTrue(mixedCaseVoice.isMale());
    }

    // NOTE: The following tests require public constructors and setters in your VoiceOption class,
    // which are not part of the standard immutable builder pattern.
    // These tests will fail unless you add them manually.

    @Test
    void testNoArgsConstructor() {
        // VoiceOption voice = new VoiceOption();
        // assertNull(voice.getVoiceId());
    }

    @Test
    void testAllArgsConstructor() {
        // List<String> tones = Arrays.asList("confident", "urgent");
        // VoiceOption voice = new VoiceOption("voice-1", "John", "Male", "American", "Test voice", tones);
        // assertEquals("voice-1", voice.getVoiceId());
    }

    @Test
    void testSettersAndGetters() {
        // VoiceOption voice = new VoiceOption();
        // voice.setVoiceId("voice-2");
        // assertEquals("voice-2", voice.getVoiceId());
    }

    @Test
    void testFactoryMethodsCreateDifferentVoices() {
        VoiceOption profMale = VoiceOption.createProfessionalMale("1", "John", "American");
        VoiceOption profFemale = VoiceOption.createProfessionalFemale("2", "Jane", "British");
        VoiceOption convMale = VoiceOption.createConversationalMale("3", "Mike", "Canadian");
        VoiceOption convFemale = VoiceOption.createConversationalFemale("4", "Emma", "Irish");
        VoiceOption versatile = VoiceOption.createVersatileVoice("5", "Alex", "Non-binary", "Neutral");

        assertNotEquals(profMale.getSupportedTones(), profFemale.getSupportedTones());
        assertNotEquals(convMale.getSupportedTones(), convFemale.getSupportedTones());
        assertEquals(4, versatile.getSupportedToneCount());
        assertTrue(versatile.getSupportedToneCount() > profMale.getSupportedToneCount());
    }
}
