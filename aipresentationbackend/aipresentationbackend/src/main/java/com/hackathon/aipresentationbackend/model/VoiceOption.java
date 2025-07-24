package com.hackathon.aipresentationbackend.model;

import java.util.List;
import java.util.Arrays;

/**
 * Model representing available voice options for speech generation.
 * This version uses a correct, manually implemented Builder pattern.
 */
public class VoiceOption {

    private final String voiceId;
    private final String name;
    private final String gender;
    private final String accent;
    private final String description;
    private final List<String> supportedTones;

    // Private constructor to force the use of the builder
    private VoiceOption(Builder builder) {
        this.voiceId = builder.voiceId;
        this.name = builder.name;
        this.gender = builder.gender;
        this.accent = builder.accent;
        this.description = builder.description;
        this.supportedTones = builder.supportedTones;
    }

    // --- Getters ---
    public String getVoiceId() { return voiceId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getAccent() { return accent; }
    public String getDescription() { return description; }
    public List<String> getSupportedTones() { return supportedTones; }

    // --- Static nested Builder class ---
    public static class Builder {
        private String voiceId;
        private String name;
        private String gender;
        private String accent;
        private String description;
        private List<String> supportedTones;

        public Builder voiceId(String voiceId) {
            this.voiceId = voiceId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder accent(String accent) {
            this.accent = accent;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder supportedTones(List<String> supportedTones) {
            this.supportedTones = supportedTones;
            return this;
        }

        public VoiceOption build() {
            return new VoiceOption(this);
        }
    }

    // --- Factory methods now use the manual builder ---
    public static VoiceOption createProfessionalMale(String voiceId, String name, String accent) {
        return new VoiceOption.Builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Male")
                .accent(accent)
                .description("Professional male voice suitable for business presentations")
                .supportedTones(Arrays.asList("confident", "conversational", "urgent"))
                .build();
    }

    public static VoiceOption createProfessionalFemale(String voiceId, String name, String accent) {
        return new VoiceOption.Builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Female")
                .accent(accent)
                .description("Professional female voice suitable for business presentations")
                .supportedTones(Arrays.asList("confident", "conversational", "empathetic"))
                .build();
    }

    public static VoiceOption createConversationalMale(String voiceId, String name, String accent) {
        return new VoiceOption.Builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Male")
                .accent(accent)
                .description("Friendly male voice perfect for casual presentations")
                .supportedTones(Arrays.asList("conversational", "empathetic", "urgent"))
                .build();
    }

    public static VoiceOption createConversationalFemale(String voiceId, String name, String accent) {
        return new VoiceOption.Builder()
                .voiceId(voiceId)
                .name(name)
                .gender("Female")
                .accent(accent)
                .description("Friendly female voice perfect for casual presentations")
                .supportedTones(Arrays.asList("conversational", "empathetic", "confident"))
                .build();
    }

    public static VoiceOption createVersatileVoice(String voiceId, String name, String gender, String accent) {
        return new VoiceOption.Builder()
                .voiceId(voiceId)
                .name(name)
                .gender(gender)
                .accent(accent)
                .description("Versatile voice supporting all emotional tones")
                .supportedTones(Arrays.asList("confident", "conversational", "urgent", "empathetic"))
                .build();
    }

    // --- Helper methods remain the same ---
    public boolean supportsTone(String tone) {
        return supportedTones != null && supportedTones.contains(tone.toLowerCase());
    }

    public boolean supportsTone(ToneType toneType) {
        return toneType != null && supportsTone(toneType.getValue());
    }

    public int getSupportedToneCount() {
        return supportedTones != null ? supportedTones.size() : 0;
    }

    public boolean isMale() {
        return "Male".equalsIgnoreCase(gender);
    }

    public boolean isFemale() {
        return "Female".equalsIgnoreCase(gender);
    }
}
