package com.hackathon.aipresentationbackend.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for emotional tone variations in speech generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToneRehearsalRequest {
    
    @NotBlank(message = "Sentence is required")
    @Size(max = 1000, message = "Sentence must not exceed 1000 characters")
    private String sentence;
    
    @NotBlank(message = "Voice ID is required")
    private String voiceId;
    
    @NotBlank(message = "Tone is required")
    private String tone;
    
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