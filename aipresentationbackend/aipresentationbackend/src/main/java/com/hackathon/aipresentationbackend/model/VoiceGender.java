package com.hackathon.aipresentationbackend.model;

/**
 * Enum representing voice gender options for Murf AI
 */
public enum VoiceGender {
    MALE("male", "Male voice"),
    FEMALE("female", "Female voice");
    
    private final String value;
    private final String description;
    
    VoiceGender(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static VoiceGender fromValue(String value) {
        if (value == null) {
            return null;
        }
        
        for (VoiceGender gender : VoiceGender.values()) {
            if (gender.value.equalsIgnoreCase(value.trim())) {
                return gender;
            }
        }
        
        throw new IllegalArgumentException("Invalid voice gender: " + value);
    }
}