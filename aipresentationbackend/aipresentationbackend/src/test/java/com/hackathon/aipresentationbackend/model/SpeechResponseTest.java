package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeechResponseTest {

    @Test
    void testBuilderPattern() {
        SpeechResponse response = SpeechResponse.builder()
                .audioUrl("https://example.com/audio.mp3")
                .duration(30)
                .voiceUsed("voice-1")
                .speedUsed(1.0)
                .build();
        
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(30, response.getDuration());
        assertEquals("voice-1", response.getVoiceUsed());
        assertEquals(1.0, response.getSpeedUsed());
        assertNull(response.getAudioBase64());
    }

    @Test
    void testWithAudioUrlFactoryMethod() {
        SpeechResponse response = SpeechResponse.withAudioUrl(
                "https://example.com/audio.mp3", 
                45, 
                "voice-2", 
                1.5
        );
        
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(45, response.getDuration());
        assertEquals("voice-2", response.getVoiceUsed());
        assertEquals(1.5, response.getSpeedUsed());
        assertNull(response.getAudioBase64());
    }

    @Test
    void testWithAudioBase64FactoryMethod() {
        String base64Audio = "UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT";
        
        SpeechResponse response = SpeechResponse.withAudioBase64(
                base64Audio, 
                60, 
                "voice-3", 
                0.8
        );
        
        assertEquals(base64Audio, response.getAudioBase64());
        assertEquals(60, response.getDuration());
        assertEquals("voice-3", response.getVoiceUsed());
        assertEquals(0.8, response.getSpeedUsed());
        assertNull(response.getAudioUrl());
    }

    @Test
    void testHasAudioUrlMethod() {
        SpeechResponse responseWithUrl = SpeechResponse.builder()
                .audioUrl("https://example.com/audio.mp3")
                .build();
        
        SpeechResponse responseWithoutUrl = SpeechResponse.builder()
                .audioBase64("base64data")
                .build();
        
        SpeechResponse responseWithEmptyUrl = SpeechResponse.builder()
                .audioUrl("")
                .build();
        
        SpeechResponse responseWithWhitespaceUrl = SpeechResponse.builder()
                .audioUrl("   ")
                .build();
        
        assertTrue(responseWithUrl.hasAudioUrl());
        assertFalse(responseWithoutUrl.hasAudioUrl());
        assertFalse(responseWithEmptyUrl.hasAudioUrl());
        assertFalse(responseWithWhitespaceUrl.hasAudioUrl());
    }

    @Test
    void testHasAudioBase64Method() {
        String base64Audio = "UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT";
        
        SpeechResponse responseWithBase64 = SpeechResponse.builder()
                .audioBase64(base64Audio)
                .build();
        
        SpeechResponse responseWithoutBase64 = SpeechResponse.builder()
                .audioUrl("https://example.com/audio.mp3")
                .build();
        
        SpeechResponse responseWithEmptyBase64 = SpeechResponse.builder()
                .audioBase64("")
                .build();
        
        SpeechResponse responseWithWhitespaceBase64 = SpeechResponse.builder()
                .audioBase64("   ")
                .build();
        
        assertTrue(responseWithBase64.hasAudioBase64());
        assertFalse(responseWithoutBase64.hasAudioBase64());
        assertFalse(responseWithEmptyBase64.hasAudioBase64());
        assertFalse(responseWithWhitespaceBase64.hasAudioBase64());
    }

    @Test
    void testNoArgsConstructor() {
        SpeechResponse response = new SpeechResponse();
        
        assertNull(response.getAudioUrl());
        assertNull(response.getAudioBase64());
        assertNull(response.getDuration());
        assertNull(response.getVoiceUsed());
        assertNull(response.getSpeedUsed());
    }

    @Test
    void testAllArgsConstructor() {
        String base64Audio = "UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT";
        
        SpeechResponse response = new SpeechResponse(
                "https://example.com/audio.mp3",
                base64Audio,
                120,
                "voice-4",
                1.2
        );
        
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(base64Audio, response.getAudioBase64());
        assertEquals(120, response.getDuration());
        assertEquals("voice-4", response.getVoiceUsed());
        assertEquals(1.2, response.getSpeedUsed());
    }

    @Test
    void testSettersAndGetters() {
        SpeechResponse response = new SpeechResponse();
        
        response.setAudioUrl("https://test.com/audio.wav");
        response.setAudioBase64("testbase64");
        response.setDuration(90);
        response.setVoiceUsed("voice-5");
        response.setSpeedUsed(0.9);
        
        assertEquals("https://test.com/audio.wav", response.getAudioUrl());
        assertEquals("testbase64", response.getAudioBase64());
        assertEquals(90, response.getDuration());
        assertEquals("voice-5", response.getVoiceUsed());
        assertEquals(0.9, response.getSpeedUsed());
    }

    @Test
    void testBothAudioFormatsPresent() {
        String base64Audio = "UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT";
        
        SpeechResponse response = SpeechResponse.builder()
                .audioUrl("https://example.com/audio.mp3")
                .audioBase64(base64Audio)
                .duration(75)
                .voiceUsed("voice-6")
                .speedUsed(1.1)
                .build();
        
        assertTrue(response.hasAudioUrl());
        assertTrue(response.hasAudioBase64());
        assertEquals("https://example.com/audio.mp3", response.getAudioUrl());
        assertEquals(base64Audio, response.getAudioBase64());
    }
}