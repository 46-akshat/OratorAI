package com.hackathon.aipresentationbackend.model;

import java.util.List;

/**
 * Model representing the analysis of user's speech and voice recommendation
 */
public class VoiceAnalysis {
    private String analysisId;
    private String userSpeechText;
    private String detectedTone;
    private String recommendedTone;
    private MurfVoice recommendedVoice;
    private String analysisReason;
    private List<String> improvementAreas;
    private double confidenceScore;
    private long analysisTimestamp;
    
    public VoiceAnalysis() {}
    
    public VoiceAnalysis(String analysisId, String userSpeechText, String detectedTone, 
                        String recommendedTone, MurfVoice recommendedVoice, String analysisReason,
                        List<String> improvementAreas, double confidenceScore) {
        this.analysisId = analysisId;
        this.userSpeechText = userSpeechText;
        this.detectedTone = detectedTone;
        this.recommendedTone = recommendedTone;
        this.recommendedVoice = recommendedVoice;
        this.analysisReason = analysisReason;
        this.improvementAreas = improvementAreas;
        this.confidenceScore = confidenceScore;
        this.analysisTimestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getAnalysisId() {
        return analysisId;
    }
    
    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }
    
    public String getUserSpeechText() {
        return userSpeechText;
    }
    
    public void setUserSpeechText(String userSpeechText) {
        this.userSpeechText = userSpeechText;
    }
    
    public String getDetectedTone() {
        return detectedTone;
    }
    
    public void setDetectedTone(String detectedTone) {
        this.detectedTone = detectedTone;
    }
    
    public String getRecommendedTone() {
        return recommendedTone;
    }
    
    public void setRecommendedTone(String recommendedTone) {
        this.recommendedTone = recommendedTone;
    }
    
    public MurfVoice getRecommendedVoice() {
        return recommendedVoice;
    }
    
    public void setRecommendedVoice(MurfVoice recommendedVoice) {
        this.recommendedVoice = recommendedVoice;
    }
    
    public String getAnalysisReason() {
        return analysisReason;
    }
    
    public void setAnalysisReason(String analysisReason) {
        this.analysisReason = analysisReason;
    }
    
    public List<String> getImprovementAreas() {
        return improvementAreas;
    }
    
    public void setImprovementAreas(List<String> improvementAreas) {
        this.improvementAreas = improvementAreas;
    }
    
    public double getConfidenceScore() {
        return confidenceScore;
    }
    
    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
    
    public long getAnalysisTimestamp() {
        return analysisTimestamp;
    }
    
    public void setAnalysisTimestamp(long analysisTimestamp) {
        this.analysisTimestamp = analysisTimestamp;
    }
}