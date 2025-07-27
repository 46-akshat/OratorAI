package com.hackathon.aipresentationbackend.model;

import java.util.List;
import java.util.UUID;

/**
 * Represents the detailed voice analysis and recommendation from Gemini.
 */
public class VoiceAnalysis {

    private final String id;
    private final String originalText;
    private final String detectedTone;
    private final String recommendedTone;
    private final MurfVoice recommendedVoice;
    private final String analysisReason;
    private final List<String> improvementAreas;
    private final double confidenceScore;

    public VoiceAnalysis(String id, String originalText, String detectedTone, String recommendedTone,
                         MurfVoice recommendedVoice, String analysisReason, List<String> improvementAreas,
                         double confidenceScore) {
        this.id = id;
        this.originalText = originalText;
        this.detectedTone = detectedTone;
        this.recommendedTone = recommendedTone;
        this.recommendedVoice = recommendedVoice;
        this.analysisReason = analysisReason;
        this.improvementAreas = improvementAreas;
        this.confidenceScore = confidenceScore;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getDetectedTone() {
        return detectedTone;
    }

    public String getRecommendedTone() {
        return recommendedTone;
    }

    public MurfVoice getRecommendedVoice() {
        return recommendedVoice;
    }

    public String getAnalysisReason() {
        return analysisReason;
    }

    public List<String> getImprovementAreas() {
        return improvementAreas;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }
}
