package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Mock service for development and demo purposes
 * Activated when mock.mode=true in application properties
 */
@Service
@ConditionalOnProperty(name = "mock.mode", havingValue = "true", matchIfMissing = false)
public class MockVoiceRecommendationService {
    
    private static final Logger log = LoggerFactory.getLogger(MockVoiceRecommendationService.class);
    
    public VoiceRecommendationResponse processVoiceRecommendation(VoiceRecommendationRequest request) {
        log.info("Processing voice recommendation in MOCK mode");
        
        try {
            // Simulate processing delay
            Thread.sleep(2000);
            
            String speechText = request.getSpeechText();
            if (speechText == null || speechText.trim().isEmpty()) {
                speechText = "Sample speech text for demonstration";
            }
            
            // Create mock analysis based on speech content
            VoiceAnalysis analysis = createMockAnalysis(speechText, request);
            
            // Mock audio URL (you could use a real sample audio file)
            String mockAudioUrl = "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav";
            
            return VoiceRecommendationResponse.success(analysis, mockAudioUrl);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return VoiceRecommendationResponse.error("Processing interrupted");
        } catch (Exception e) {
            log.error("Error in mock voice recommendation: {}", e.getMessage(), e);
            return VoiceRecommendationResponse.error("Mock service error: " + e.getMessage());
        }
    }
    
    private VoiceAnalysis createMockAnalysis(String speechText, VoiceRecommendationRequest request) {
        // Simple analysis based on keywords and context
        String detectedTone = detectToneFromText(speechText);
        ToneType recommendedTone = getRecommendedTone(speechText, request.getPresentationType());
        MurfVoice recommendedVoice = selectMockVoice(recommendedTone, request.getPreferredGender());
        
        List<String> improvementAreas = generateMockImprovementAreas(speechText, detectedTone);
        String analysisReason = generateMockAnalysisReason(detectedTone, recommendedTone, recommendedVoice);
        
        return new VoiceAnalysis(
            UUID.randomUUID().toString(),
            speechText,
            detectedTone,
            recommendedTone.getValue(),
            recommendedVoice,
            analysisReason,
            improvementAreas,
            0.85 // Mock confidence score
        );
    }
    
    private String detectToneFromText(String text) {
        String lowerText = text.toLowerCase();
        
        if (lowerText.contains("excited") || lowerText.contains("amazing") || lowerText.contains("incredible")) {
            return "enthusiastic";
        } else if (lowerText.contains("um") || lowerText.contains("uh") || lowerText.contains("maybe")) {
            return "hesitant";
        } else if (lowerText.contains("results") || lowerText.contains("growth") || lowerText.contains("achieved")) {
            return "confident";
        } else if (lowerText.contains("understand") || lowerText.contains("support") || lowerText.contains("together")) {
            return "empathetic";
        } else {
            return "neutral";
        }
    }
    
    private ToneType getRecommendedTone(String speechText, String presentationType) {
        if ("business".equals(presentationType) || "sales".equals(presentationType)) {
            return ToneType.CONFIDENT;
        } else if ("casual".equals(presentationType)) {
            return ToneType.CONVERSATIONAL;
        } else if ("motivational".equals(presentationType)) {
            return ToneType.URGENT;
        } else if (speechText.toLowerCase().contains("support") || speechText.toLowerCase().contains("understand")) {
            return ToneType.EMPATHETIC;
        } else {
            return ToneType.CONVERSATIONAL;
        }
    }
    
    private MurfVoice selectMockVoice(ToneType recommendedTone, VoiceGender preferredGender) {
        MurfVoice[] voicesForTone = MurfVoice.getVoicesByTone(recommendedTone);
        
        if (preferredGender != null) {
            for (MurfVoice voice : voicesForTone) {
                if (voice.getGender() == preferredGender) {
                    return voice;
                }
            }
        }
        
        return voicesForTone.length > 0 ? voicesForTone[0] : MurfVoice.ALEX;
    }
    
    private List<String> generateMockImprovementAreas(String speechText, String detectedTone) {
        if ("hesitant".equals(detectedTone)) {
            return Arrays.asList(
                "Reduce filler words like 'um' and 'uh'",
                "Speak with more conviction and authority",
                "Practice pausing instead of using filler words"
            );
        } else if ("neutral".equals(detectedTone)) {
            return Arrays.asList(
                "Add more energy and enthusiasm to your delivery",
                "Use vocal variety to emphasize key points",
                "Connect emotionally with your audience"
            );
        } else {
            return Arrays.asList(
                "Maintain consistent pacing throughout",
                "Use strategic pauses for emphasis",
                "Project confidence in your body language"
            );
        }
    }
    
    private String generateMockAnalysisReason(String detectedTone, ToneType recommendedTone, MurfVoice recommendedVoice) {
        return String.format(
            "Your speech shows a %s tone, but for maximum impact, a %s delivery would be more effective. " +
            "The %s voice demonstrates the ideal %s characteristics that will help you connect better with your audience and convey your message with greater authority.",
            detectedTone,
            recommendedTone.getValue(),
            recommendedVoice.getVoiceId().toUpperCase(),
            recommendedTone.getValue()
        );
    }
}