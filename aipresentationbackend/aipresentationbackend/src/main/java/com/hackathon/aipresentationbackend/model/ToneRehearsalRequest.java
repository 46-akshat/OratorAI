package com.hackathon.aipresentationbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Request model for emotional tone variations in speech generation
 */
@Data
// @NoArgsConstructor and @AllArgsConstructor are removed because we are defining them manually.
public class ToneRehearsalRequest {

    @NotBlank(message = "Sentence is required")
    @Size(max = 1000, message = "Sentence must not exceed 1000 characters")
    private String sentence;

    @NotBlank(message = "Voice ID is required")
    private String voiceId;

    @NotBlank(message = "Tone is required")
    private String tone;

    /**
     * No-argument constructor.
     * Required by your test and by frameworks like Jackson.
     */
    public ToneRehearsalRequest() {
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    /**
     * All-arguments constructor.
     * Used by your test and for creating instances directly.
     */
    public ToneRehearsalRequest(String sentence, String voiceId, String tone) {
        this.sentence = sentence;
        this.voiceId = voiceId;
        this.tone = tone;
    }

    /**
     * Get the tone as ToneType enum
     */
    public ToneType getToneType() {
        return ToneType.fromValue(this.tone);
    }

    /**
     * Set the tone using ToneType enum
     */
    public void setToneType(ToneType toneType) {
        this.tone = toneType != null ? toneType.getValue() : null;
    }

    /**
     * Validate that the tone is a supported type
     */
    @AssertTrue(message = "Tone must be one of: confident, conversational, urgent, empathetic")
    public boolean isToneValid() {
        return tone == null || ToneType.isValidTone(tone);
    }
}
