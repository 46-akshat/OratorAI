package com.hackathon.aipresentationbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for text-to-speech generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeechResponse {
    
    private String audioUrl;
    private String audioBase64; // Alternative delivery method
    private Integer duration; // Duration in seconds
    private String voiceUsed;
    private Double speedUsed;
    
    /**
     * Builder method for creating response with audio URL
     */
    public static SpeechResponse withAudioUrl(String audioUrl, Integer duration, String voiceUsed, Double speedUsed) {
        return SpeechResponse.builder()
                .audioUrl(audioUrl)
                .duration(duration)
                .voiceUsed(voiceUsed)
                .speedUsed(speedUsed)
                .build();
    }
    
    /**
     * Builder method for creating response with base64 audio
     */
    public static SpeechResponse withAudioBase64(String audioBase64, Integer duration, String voiceUsed, Double speedUsed) {
        return SpeechResponse.builder()
                .audioBase64(audioBase64)
                .duration(duration)
                .voiceUsed(voiceUsed)
                .speedUsed(speedUsed)
                .build();
    }
    
    /**
     * Check if response contains audio URL
     */
    public boolean hasAudioUrl() {
        return audioUrl != null && !audioUrl.trim().isEmpty();
    }
    
    /**
     * Check if response contains base64 audio
     */
    public boolean hasAudioBase64() {
        return audioBase64 != null && !audioBase64.trim().isEmpty();
    }
}