package com.hackathon.aipresentationbackend.model;

/**
 * Enhanced voice recommendation data that extends your existing VoiceOption system
 * This integrates with your existing voice infrastructure
 */
public class VoiceRecommendation {
    private final VoiceOption voiceOption;
    private final ToneType recommendedTone;
    private final String recommendationReason;
    private final double confidenceScore;
    
    public VoiceRecommendation(VoiceOption voiceOption, ToneType recommendedTone, 
                              String recommendationReason, double confidenceScore) {
        this.voiceOption = voiceOption;
        this.recommendedTone = recommendedTone;
        this.recommendationReason = recommendationReason;
        this.confidenceScore = confidenceScore;
    }
    
    // Getters
    public VoiceOption getVoiceOption() { return voiceOption; }
    public ToneType getRecommendedTone() { return recommendedTone; }
    public String getRecommendationReason() { return recommendationReason; }
    public double getConfidenceScore() { return confidenceScore; }
}