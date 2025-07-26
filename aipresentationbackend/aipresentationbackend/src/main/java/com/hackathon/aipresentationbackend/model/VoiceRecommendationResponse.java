package com.hackathon.aipresentationbackend.model;

/**
 * Response model for voice recommendation API
 */
public class VoiceRecommendationResponse {
    private VoiceAnalysis analysis;
    private String generatedAudioUrl;
    private String generatedAudioBase64;
    private boolean success;
    private String errorMessage;
    
    public VoiceRecommendationResponse() {}
    
    public VoiceRecommendationResponse(VoiceAnalysis analysis, String generatedAudioUrl, boolean success) {
        this.analysis = analysis;
        this.generatedAudioUrl = generatedAudioUrl;
        this.success = success;
    }
    
    // Static factory methods
    public static VoiceRecommendationResponse success(VoiceAnalysis analysis, String audioUrl) {
        return new VoiceRecommendationResponse(analysis, audioUrl, true);
    }
    
    public static VoiceRecommendationResponse error(String errorMessage) {
        VoiceRecommendationResponse response = new VoiceRecommendationResponse();
        response.success = false;
        response.errorMessage = errorMessage;
        return response;
    }
    
    // Getters and Setters
    public VoiceAnalysis getAnalysis() {
        return analysis;
    }
    
    public void setAnalysis(VoiceAnalysis analysis) {
        this.analysis = analysis;
    }
    
    public String getGeneratedAudioUrl() {
        return generatedAudioUrl;
    }
    
    public void setGeneratedAudioUrl(String generatedAudioUrl) {
        this.generatedAudioUrl = generatedAudioUrl;
    }
    
    public String getGeneratedAudioBase64() {
        return generatedAudioBase64;
    }
    
    public void setGeneratedAudioBase64(String generatedAudioBase64) {
        this.generatedAudioBase64 = generatedAudioBase64;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}