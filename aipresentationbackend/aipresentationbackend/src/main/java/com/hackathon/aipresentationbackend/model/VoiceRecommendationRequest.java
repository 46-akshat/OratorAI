package com.hackathon.aipresentationbackend.model;

/**
 * Request model for voice recommendation API
 */
public class VoiceRecommendationRequest {
    private String audioBase64;
    private String speechText;
    private String presentationType; // business, casual, educational, etc.
    private String targetAudience;   // professional, students, general, etc.
    private VoiceGender preferredGender; // optional preference
    
    public VoiceRecommendationRequest() {}
    
    public VoiceRecommendationRequest(String audioBase64, String speechText, String presentationType, String targetAudience) {
        this.audioBase64 = audioBase64;
        this.speechText = speechText;
        this.presentationType = presentationType;
        this.targetAudience = targetAudience;
    }
    
    // Getters and Setters
    public String getAudioBase64() {
        return audioBase64;
    }
    
    public void setAudioBase64(String audioBase64) {
        this.audioBase64 = audioBase64;
    }
    
    public String getSpeechText() {
        return speechText;
    }
    
    public void setSpeechText(String speechText) {
        this.speechText = speechText;
    }
    
    public String getPresentationType() {
        return presentationType;
    }
    
    public void setPresentationType(String presentationType) {
        this.presentationType = presentationType;
    }
    
    public String getTargetAudience() {
        return targetAudience;
    }
    
    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }
    
    public VoiceGender getPreferredGender() {
        return preferredGender;
    }
    
    public void setPreferredGender(VoiceGender preferredGender) {
        this.preferredGender = preferredGender;
    }
}