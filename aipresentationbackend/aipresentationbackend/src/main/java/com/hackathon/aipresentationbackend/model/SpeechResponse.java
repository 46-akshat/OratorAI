package com.hackathon.aipresentationbackend.model;

/**
 * Response model for text-to-speech generation
 * This version uses a correct, manually implemented Builder pattern.
 */
public class SpeechResponse {

    // Fields are final because the object should be immutable after creation
    private final String audioUrl;
    private final String audioBase64;
    private final Integer duration;
    private final String voiceUsed;
    private final Double speedUsed;

    // The constructor is private to force the use of the Builder
    private SpeechResponse(Builder builder) {
        this.audioUrl = builder.audioUrl;
        this.audioBase64 = builder.audioBase64;
        this.duration = builder.duration;
        this.voiceUsed = builder.voiceUsed;
        this.speedUsed = builder.speedUsed;
    }

    // --- Getters for all fields ---

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getAudioBase64() {
        return audioBase64;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getVoiceUsed() {
        return voiceUsed;
    }

    public Double getSpeedUsed() {
        return speedUsed;
    }

    // --- Static nested Builder class ---
    public static class Builder {
        private String audioUrl;
        private String audioBase64;
        private Integer duration;
        private String voiceUsed;
        private Double speedUsed;

        // CORRECT: Each method now accepts a parameter
        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public Builder audioBase64(String audioBase64) {
            this.audioBase64 = audioBase64;
            return this;
        }

        public Builder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder voiceUsed(String voiceUsed) {
            this.voiceUsed = voiceUsed;
            return this;
        }

        public Builder speedUsed(Double speedUsed) {
            this.speedUsed = speedUsed;
            return this;
        }

        // CORRECT: This now calls the private constructor
        public SpeechResponse build() {
            return new SpeechResponse(this);
        }
    }

    // --- Your helpful static factory methods can now use the manual builder ---

    public static SpeechResponse withAudioUrl(String audioUrl, Integer duration, String voiceUsed, Double speedUsed) {
        return new SpeechResponse.Builder()
                .audioUrl(audioUrl)
                .duration(duration)
                .voiceUsed(voiceUsed)
                .speedUsed(speedUsed)
                .build();
    }

    public static SpeechResponse withAudioBase64(String audioBase64, Integer duration, String voiceUsed, Double speedUsed) {
        return new SpeechResponse.Builder()
                .audioBase64(audioBase64)
                .duration(duration)
                .voiceUsed(voiceUsed)
                .speedUsed(speedUsed)
                .build();
    }

    // --- Your helper methods remain the same ---

    public boolean hasAudioUrl() {
        return audioUrl != null && !audioUrl.trim().isEmpty();
    }

    public boolean hasAudioBase64() {
        return audioBase64 != null && !audioBase64.trim().isEmpty();
    }
}
