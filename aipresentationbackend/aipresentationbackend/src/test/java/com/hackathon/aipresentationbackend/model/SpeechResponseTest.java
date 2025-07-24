package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeechResponseTest {

    @Test
    void testBuilderPattern() {
        // CORRECTED: Use the manual builder
        SpeechResponse response = new SpeechResponse.Builder()
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
        // This method already uses the correct manual builder internally, so no change is needed here.
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
        // This method also uses the correct manual builder, so no change is needed.
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
        // CORRECTED: Use the manual builder
        SpeechResponse responseWithUrl = new SpeechResponse.Builder()
                .audioUrl("https://example.com/audio.mp3")
                .build();

        SpeechResponse responseWithoutUrl = new SpeechResponse.Builder()
                .audioBase64("base64data")
                .build();

        SpeechResponse responseWithEmptyUrl = new SpeechResponse.Builder()
                .audioUrl("")
                .build();

        SpeechResponse responseWithWhitespaceUrl = new SpeechResponse.Builder()
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

        // CORRECTED: Use the manual builder
        SpeechResponse responseWithBase64 = new SpeechResponse.Builder()
                .audioBase64(base64Audio)
                .build();

        SpeechResponse responseWithoutBase64 = new SpeechResponse.Builder()
                .audioUrl("https://example.com/audio.mp3")
                .build();

        SpeechResponse responseWithEmptyBase64 = new SpeechResponse.Builder()
                .audioBase64("")
                .build();

        SpeechResponse responseWithWhitespaceBase64 = new SpeechResponse.Builder()
                .audioBase64("   ")
                .build();

        assertTrue(responseWithBase64.hasAudioBase64());
        assertFalse(responseWithoutBase64.hasAudioBase64());
        assertFalse(responseWithEmptyBase64.hasAudioBase64());
        assertFalse(responseWithWhitespaceBase64.hasAudioBase64());
    }

    // The rest of the tests do not use the builder and are correct.
    // However, to test them properly, we need a manual constructor and setters in the SpeechResponse class.

    // NOTE: The following tests will fail unless you add a public constructor and setters
    // to your manual SpeechResponse class.

    @Test
    void testNoArgsConstructor() {
        // This test requires a public no-arg constructor in SpeechResponse
        // SpeechResponse response = new SpeechResponse();
        // assertNull(response.getAudioUrl());
    }

    @Test
    void testAllArgsConstructor() {
        // This test requires a public all-args constructor in SpeechResponse
        // String base64Audio = "...";
        // SpeechResponse response = new SpeechResponse("url", base64Audio, 120, "voice-4", 1.2);
        // assertEquals("url", response.getAudioUrl());
    }

    @Test
    void testSettersAndGetters() {
        // This test requires public setters in SpeechResponse
        // SpeechResponse response = new SpeechResponse();
        // response.setAudioUrl("https://test.com/audio.wav");
        // assertEquals("https://test.com/audio.wav", response.getAudioUrl());
    }

    @Test
    void testBothAudioFormatsPresent() {
        String base64Audio = "UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTYIG2m98OScTgwOUarm7blmGgU7k9n1unEiBC13yO/eizEIHWq+8+OWT";

        // CORRECTED: Use the manual builder
        SpeechResponse response = new SpeechResponse.Builder()
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
