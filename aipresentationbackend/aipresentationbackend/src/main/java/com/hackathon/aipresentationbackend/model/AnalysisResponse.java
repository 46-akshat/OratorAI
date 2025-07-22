package com.hackathon.aipresentationbackend.model;

public class AnalysisResponse {

    // --- Fields ---
    // FIX 2: Renamed fields to use standard camelCase.
    private final int score;
    private final String positiveFeedback;
    private final String improvementPoints;
    private final String audioUrl;

    // --- Private Constructor ---
    // FIX 1: The constructor now correctly accepts a Builder object.
    // It's private to force the use of the builder.
    private AnalysisResponse(Builder builder) {
        this.score = builder.score;
        this.positiveFeedback = builder.positiveFeedback;
        this.improvementPoints = builder.improvementPoints;
        this.audioUrl = builder.audioUrl;
    }

    // --- Getters ---
    // FIX 2: Getter names updated to match camelCase fields.
    public int getScore() {
        return score;
    }

    public String getPositiveFeedback() {
        return positiveFeedback;
    }

    public String getImprovementPoints() {
        return improvementPoints;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    // --- Static nested Builder class ---
    public static class Builder {
        // FIX 2: Renamed fields to use standard camelCase.
        private int score;
        private String positiveFeedback;
        private String improvementPoints;
        private String audioUrl;

        // --- Builder methods for chaining ---
        // FIX 2: Method names updated for consistency.
        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder positiveFeedback(String positiveFeedback) {
            this.positiveFeedback = positiveFeedback;
            return this;
        }

        public Builder improvementPoints(String improvementPoints) {
            this.improvementPoints = improvementPoints;
            return this;
        }

        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        // --- Final build method ---
        // This now correctly calls the private constructor.
        public AnalysisResponse build() {
            return new AnalysisResponse(this);
        }
    }
}