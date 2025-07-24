package com.hackathon.aipresentationbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Request model for text-to-speech generation
 */
@Data
// @NoArgsConstructor and @AllArgsConstructor are removed because we are defining them manually.
public class SpeechRequest {

    @NotBlank(message = "Text is required")
    @Size(max = 10000, message = "Text must not exceed 10000 characters")
    private String text;

    @NotBlank(message = "Voice ID is required")
    private String voiceId;

    public String getText() {
        return text;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public Double getSpeed() {
        return speed;
    }

    public String getTone() {
        return tone;
    }

    @DecimalMin(value = "0.5", message = "Speed must be at least 0.5")
    @DecimalMax(value = "2.0", message = "Speed must not exceed 2.0")
    private Double speed = 1.0; // Default speed

    @Size(max = 50, message = "Tone must not exceed 50 characters")
    private String tone; // Optional for tone rehearsal

    /**
     * No-argument constructor.
     * Required by your test and by frameworks like Jackson for deserialization.
     */
    public SpeechRequest() {
        // The default speed value is already set on the field declaration.
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    /**
     * All-arguments constructor.
     * Used by your test and for creating instances directly.
     */
    public SpeechRequest(String text, String voiceId, Double speed, String tone) {
        this.text = text;
        this.voiceId = voiceId;
        this.speed = speed;
        this.tone = tone;
    }
}
