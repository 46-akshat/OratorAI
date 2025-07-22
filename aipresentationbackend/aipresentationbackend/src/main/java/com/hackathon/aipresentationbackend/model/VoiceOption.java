package com.hackathon.aipresentationbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Arrays;

/**
 * Model representing available voice options for speech generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceOption {
    
    private String voiceId;
    private String name;
    private String gender;
    private String accent;
    private String description;
    private List<String> supportedTones;
    
    /**
     * Factory method for creating a professional male voice
     */
    public static VoiceOption createProfessionalMale(String voiceId, String name, String accent) {
        return VoiceOption.builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Male")
                .accent(accent)
                .description("Professional male voice suitable for business presentations")
                .supportedTones(Arrays.asList("confident", "conversational", "urgent"))
                .build();
    }
    
    /**
     * Factory method for creating a professional female voice
     */
    public static VoiceOption createProfessionalFemale(String voiceId, String name, String accent) {
        return VoiceOption.builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Female")
                .accent(accent)
                .description("Professional female voice suitable for business presentations")
                .supportedTones(Arrays.asList("confident", "conversational", "empathetic"))
                .build();
    }
    
    /**
     * Factory method for creating a conversational male voice
     */
    public static VoiceOption createConversationalMale(String voiceId, String name, String accent) {
        return VoiceOption.builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Male")
                .accent(accent)
                .description("Friendly male voice perfect for casual presentations")
                .supportedTones(Arrays.asList("conversational", "empathetic", "urgent"))
                .build();
    }
    
    /**
     * Factory method for creating a conversational female voice
     */
    public static VoiceOption createConversationalFemale(String voiceId, String name, String accent) {
        return VoiceOption.builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Female")
                .accent(accent)
                .description("Friendly female voice perfect for casual presentations")
                .supportedTones(Arrays.asList("conversational", "empathetic", "confident"))
                .build();
    }
    
    /**
     * Factory method for creating a versatile voice that supports all tones
     */
    public static VoiceOption createVersatileVoice(String voiceId, String name, String gender, String accent) {
        return VoiceOption.builder()
                .voiceId(voiceId)
                .name(name)
                .gender(gender)
                .accent(accent)
                .description("Versatile voice supporting all emotional tones")
                .supportedTones(Arrays.asList("confident", "conversational", "urgent", "empathetic"))
                .build();
    }
    
    /**
     * Check if this voice supports a specific tone
     */
    public boolean supportsTone(String tone) {
        return supportedTones != null && supportedTones.contains(tone.toLowerCase());
    }
    
    /**
     * Check if this voice supports a specific ToneType
     */
    public boolean supportsTone(ToneType toneType) {
        return toneType != null && supportsTone(toneType.getValue());
    }
    
    /**
     * Get the number of supported tones
     */
    public int getSupportedToneCount() {
        return supportedTones != null ? supportedTones.size() : 0;
    }
    
    /**
     * Check if this is a male voice
     */
    public boolean isMale() {
        return "Male".equalsIgnoreCase(gender);
    }
    
    /**
     * Check if this is a female voice
     */
    public boolean isFemale() {
        return "Female".equalsIgnoreCase(gender);
    }
}