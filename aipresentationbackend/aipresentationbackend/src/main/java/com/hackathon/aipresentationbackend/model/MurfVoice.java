package com.hackathon.aipresentationbackend.model;

/**
 * Enum representing specific Murf AI voices with their characteristics
 * Based on popular Murf AI voice options
 */
public enum MurfVoice {
    // Male Confident Voices
    MARCUS("marcus", VoiceGender.MALE, VoiceAge.MIDDLE_AGED, ToneType.CONFIDENT, 
           "Deep, authoritative voice perfect for leadership presentations"),
    
    DAVID("david", VoiceGender.MALE, VoiceAge.YOUNG, ToneType.CONFIDENT,
          "Clear, professional voice with confident delivery"),
    
    // Female Confident Voices  
    SARAH("sarah", VoiceGender.FEMALE, VoiceAge.MIDDLE_AGED, ToneType.CONFIDENT,
          "Strong, professional female voice with authority"),
    
    EMMA("emma", VoiceGender.FEMALE, VoiceAge.YOUNG, ToneType.CONFIDENT,
         "Crisp, confident voice ideal for business presentations"),
    
    // Male Conversational Voices
    ALEX("alex", VoiceGender.MALE, VoiceAge.YOUNG, ToneType.CONVERSATIONAL,
         "Friendly, approachable voice for casual presentations"),
    
    JAMES("james", VoiceGender.MALE, VoiceAge.MIDDLE_AGED, ToneType.CONVERSATIONAL,
          "Warm, engaging voice perfect for storytelling"),
    
    // Female Conversational Voices
    LILY("lily", VoiceGender.FEMALE, VoiceAge.YOUNG, ToneType.CONVERSATIONAL,
         "Natural, friendly voice with conversational flow"),
    
    SOPHIA("sophia", VoiceGender.FEMALE, VoiceAge.MIDDLE_AGED, ToneType.CONVERSATIONAL,
           "Warm, relatable voice for engaging presentations"),
    
    // Urgent/Energetic Voices
    RYAN("ryan", VoiceGender.MALE, VoiceAge.YOUNG, ToneType.URGENT,
         "High-energy voice for dynamic presentations"),
    
    MAYA("maya", VoiceGender.FEMALE, VoiceAge.YOUNG, ToneType.URGENT,
         "Energetic, compelling voice for urgent messaging"),
    
    // Empathetic Voices
    MICHAEL("michael", VoiceGender.MALE, VoiceAge.MIDDLE_AGED, ToneType.EMPATHETIC,
            "Gentle, understanding voice for sensitive topics"),
    
    GRACE("grace", VoiceGender.FEMALE, VoiceAge.MIDDLE_AGED, ToneType.EMPATHETIC,
          "Compassionate, caring voice for empathetic delivery");
    
    private final String voiceId;
    private final VoiceGender gender;
    private final VoiceAge age;
    private final ToneType primaryTone;
    private final String description;
    
    MurfVoice(String voiceId, VoiceGender gender, VoiceAge age, ToneType primaryTone, String description) {
        this.voiceId = voiceId;
        this.gender = gender;
        this.age = age;
        this.primaryTone = primaryTone;
        this.description = description;
    }
    
    public String getVoiceId() {
        return voiceId;
    }
    
    public VoiceGender getGender() {
        return gender;
    }
    
    public VoiceAge getAge() {
        return age;
    }
    
    public ToneType getPrimaryTone() {
        return primaryTone;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Find voices by tone type
     */
    public static MurfVoice[] getVoicesByTone(ToneType tone) {
        return java.util.Arrays.stream(MurfVoice.values())
                .filter(voice -> voice.primaryTone == tone)
                .toArray(MurfVoice[]::new);
    }
    
    /**
     * Find voices by gender and tone
     */
    public static MurfVoice[] getVoicesByGenderAndTone(VoiceGender gender, ToneType tone) {
        return java.util.Arrays.stream(MurfVoice.values())
                .filter(voice -> voice.gender == gender && voice.primaryTone == tone)
                .toArray(MurfVoice[]::new);
    }
    
    /**
     * Get voice by ID
     */
    public static MurfVoice fromVoiceId(String voiceId) {
        if (voiceId == null) {
            return null;
        }
        
        for (MurfVoice voice : MurfVoice.values()) {
            if (voice.voiceId.equalsIgnoreCase(voiceId.trim())) {
                return voice;
            }
        }
        
        throw new IllegalArgumentException("Invalid voice ID: " + voiceId);
    }
    
    /**
     * Convert to VoiceOption for compatibility with existing system
     */
    public VoiceOption toVoiceOption() {
        return new VoiceOption.Builder()
                .voiceId(this.voiceId)
                .name(this.voiceId.substring(0, 1).toUpperCase() + this.voiceId.substring(1))
                .gender(this.gender.getValue())
                .accent("American") // Default accent
                .description(this.description)
                .supportedTones(java.util.Arrays.asList(this.primaryTone.getValue()))
                .build();
    }
}