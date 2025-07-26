package com.hackathon.aipresentationbackend.model;

/**
 * Enum representing voice age categories for Murf AI
 */
public enum VoiceAge {
    YOUNG("young", "Youthful, energetic voice (20s-30s)"),
    MIDDLE_AGED("middle_aged", "Mature, professional voice (30s-50s)"),
    SENIOR("senior", "Experienced, authoritative voice (50s+)");
    
    private final String value;
    private final String description;
    
    VoiceAge(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static VoiceAge fromValue(String value) {
        if (value == null) {
            return null;
        }
        
        for (VoiceAge age : VoiceAge.values()) {
            if (age.value.equalsIgnoreCase(value.trim())) {
                return age;
            }
        }
        
        throw new IllegalArgumentException("Invalid voice age: " + value);
    }
}