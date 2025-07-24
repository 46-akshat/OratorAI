package com.hackathon.aipresentationbackend.controller;

import com.hackathon.aipresentationbackend.model.*;
import com.hackathon.aipresentationbackend.service.VoiceRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for voice recommendation and analysis endpoints
 */
@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins = "*")
public class VoiceRecommendationController {
    
    private static final Logger log = LoggerFactory.getLogger(VoiceRecommendationController.class);
    
    private final VoiceRecommendationService voiceRecommendationService;
    
    @Autowired
    public VoiceRecommendationController(VoiceRecommendationService voiceRecommendationService) {
        this.voiceRecommendationService = voiceRecommendationService;
    }
    
    /**
     * Main endpoint: Analyze speech and generate recommended voice audio
     */
    @PostMapping("/recommend")
    public ResponseEntity<VoiceRecommendationResponse> recommendVoice(@RequestBody VoiceRecommendationRequest request) {
        log.info("Received voice recommendation request");
        
        // Input validation
        if (request.getSpeechText() == null || request.getSpeechText().trim().isEmpty()) {
            if (request.getAudioBase64() == null || request.getAudioBase64().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(VoiceRecommendationResponse.error("Please provide either speech text or audio data"));
            }
        }
        
        if (request.getSpeechText() != null && request.getSpeechText().trim().length() < 20) {
            return ResponseEntity.badRequest()
                    .body(VoiceRecommendationResponse.error("Speech text is too short. Please provide at least 20 characters for better analysis."));
        }
        
        try {
            VoiceRecommendationResponse response = voiceRecommendationService.processVoiceRecommendation(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("Error in voice recommendation endpoint: {}", e.getMessage(), e);
            String userFriendlyMessage = getUserFriendlyErrorMessage(e);
            return ResponseEntity.internalServerError()
                    .body(VoiceRecommendationResponse.error(userFriendlyMessage));
        }
    }
    
    private String getUserFriendlyErrorMessage(Exception e) {
        if (e.getMessage().contains("API key")) {
            return "Service configuration issue. Please contact support.";
        } else if (e.getMessage().contains("timeout") || e.getMessage().contains("connection")) {
            return "Service temporarily unavailable. Please try again in a moment.";
        } else if (e.getMessage().contains("rate limit") || e.getMessage().contains("quota")) {
            return "Service is busy. Please try again in a few minutes.";
        } else {
            return "We're experiencing technical difficulties. Please try again later.";
        }
    }
    
    /**
     * Get voice recommendation without generating audio
     */
    @PostMapping("/analyze")
    public ResponseEntity<VoiceAnalysis> analyzeVoice(@RequestBody VoiceRecommendationRequest request) {
        log.info("Received voice analysis request");
        
        try {
            if (request.getSpeechText() == null || request.getSpeechText().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            VoiceAnalysis analysis = voiceRecommendationService.getVoiceRecommendation(
                request.getSpeechText(),
                request.getPresentationType(),
                request.getTargetAudience(),
                request.getPreferredGender()
            );
            
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            log.error("Error in voice analysis endpoint: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Generate audio for a specific voice (testing purposes)
     */
    @PostMapping("/generate/{voiceId}")
    public ResponseEntity<String> generateAudioForVoice(@PathVariable String voiceId, 
                                                       @RequestBody String text) {
        log.info("Generating audio for voice: {}", voiceId);
        
        try {
            MurfVoice voice = MurfVoice.fromVoiceId(voiceId.toLowerCase());
            String audioUrl = voiceRecommendationService.generateAudioForVoice(text, voice);
            return ResponseEntity.ok(audioUrl);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid voice ID: {}", voiceId);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error generating audio for voice {}: {}", voiceId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get all available voices
     */
    @GetMapping("/voices")
    public ResponseEntity<List<MurfVoice>> getAvailableVoices() {
        log.info("Fetching available voices");
        return ResponseEntity.ok(Arrays.asList(MurfVoice.values()));
    }
    
    /**
     * Get voices by tone
     */
    @GetMapping("/voices/tone/{tone}")
    public ResponseEntity<List<MurfVoice>> getVoicesByTone(@PathVariable String tone) {
        log.info("Fetching voices for tone: {}", tone);
        
        try {
            ToneType toneType = ToneType.fromValue(tone);
            MurfVoice[] voices = MurfVoice.getVoicesByTone(toneType);
            return ResponseEntity.ok(Arrays.asList(voices));
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid tone type: {}", tone);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get available tone types
     */
    @GetMapping("/tones")
    public ResponseEntity<List<ToneType>> getAvailableTones() {
        log.info("Fetching available tone types");
        return ResponseEntity.ok(Arrays.asList(ToneType.values()));
    }
}