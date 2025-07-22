package com.hackathon.aipresentationbackend.model;

/**
 * Enum representing supported emotional tone types for speech generation
 */
public enum ToneType {
    CONFIDENT("confident", "A strong, assured tone that conveys certainty and authority"),
    CONVERSATIONAL("conversational", "A natural, friendly tone suitable for casual discussions"),
    URGENT("urgent", "An energetic, pressing tone that conveys importance and immediacy"),
    EMPATHETIC("empathetic", "A warm, understanding tone that shows care and compassion");
    
    private final String value;
    private final String description;
    
    ToneType(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get ToneType from string value
     */
    public static ToneType fromValue(String value) {
        if (value == null) {
            return null;
        }
        
        for (ToneType tone : ToneType.values()) {
            if (tone.value.equalsIgnoreCase(value.trim())) {
                return tone;
            }
        }
        
        throw new IllegalArgumentException("Invalid tone type: " + value + 
                ". Supported tones are: confident, conversational, urgent, empathetic");
    }
    
    /**
     * Check if a string value is a valid tone type
     */
    public static boolean isValidTone(String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}