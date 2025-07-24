package com.hackathon.aipresentationbackend.model;

public class AnalysisResponse {

    // --- Fields ---
    private final int score;
    private final String positiveFeedback;
    private final String improvementPoints;
    private final String audioUrl;
    private final String spokenTranscript;

    // --- Private Constructor ---
    // This constructor now correctly accepts only a Builder object.
    private AnalysisResponse(Builder builder) {
        this.score = builder.score;
        this.positiveFeedback = builder.positiveFeedback;
        this.improvementPoints = builder.improvementPoints;
        this.audioUrl = builder.audioUrl;
        this.spokenTranscript = builder.spokenTranscript; // Gets the transcript from the builder
    }

    // --- Getters ---
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

    public String getSpokenTranscript() {
        return spokenTranscript;
    }

    // --- Static nested Builder class ---
    public static class Builder {
        private int score;
        private String positiveFeedback;
        private String improvementPoints;
        private String audioUrl;
        private String spokenTranscript;

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

        public Builder spokenTranscript(String spokenTranscript) {
            this.spokenTranscript = spokenTranscript;
            return this;
        }

        // --- Final build method ---
        // This now correctly calls the private constructor.
        public AnalysisResponse build() {
            return new AnalysisResponse(this);
        }
    }
}
