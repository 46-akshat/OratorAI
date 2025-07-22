package com.hackathon.aipresentationbackend.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for text-to-speech generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechRequest {
    
    @NotBlank(message = "Text is required")
    @Size(max = 10000, message = "Text must not exceed 10000 characters")
    private String text;
    
    @NotBlank(message = "Voice ID is required")
    private String voiceId;
    
    @DecimalMin(value = "0.5", message = "Speed must be at least 0.5")
    @DecimalMax(value = "2.0", message = "Speed must not exceed 2.0")
    private Double speed = 1.0; // Default speed
    
    @Size(max = 50, message = "Tone must not exceed 50 characters")
    private String tone; // Optional for tone rehearsal
}