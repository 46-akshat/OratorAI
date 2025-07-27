package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service that integrates voice recommendation with your existing analysis workflow
 * This extends your current AnalysisResponse with voice recommendations
 */
@Service
public class VoiceAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(VoiceAnalysisService.class);

    private final MurfService murfService;

    @Autowired
    public VoiceAnalysisService(MurfService murfService) {
        this.murfService = murfService;
    }

    /**
     * Analyze speech text and recommend optimal voice for improvement
     * This integrates with your existing VoiceOption system
     */
    public VoiceRecommendation analyzeAndRecommendVoice(String speechText, String detectedTone) {
        log.info("Analyzing speech for voice recommendation, detected tone: {}", detectedTone);

        try {
            // Get available voices from your existing system
            List<VoiceOption> availableVoices = murfService.getAvailableVoices();
            log.info("Available voices: {}", availableVoices.stream().map(VoiceOption::getName).toList());

            // Determine recommended tone based on detected issues
            ToneType recommendedTone = determineRecommendedTone(speechText, detectedTone);
            log.info("Recommended tone: {}", recommendedTone);

            // Select best voice from available options
            VoiceOption bestVoice = selectBestVoice(availableVoices, recommendedTone, speechText);
            log.info("Selected voice: {} for tone: {}", bestVoice.getName(), recommendedTone);

            // Generate recommendation reason
            String reason = generateRecommendationReason(detectedTone, recommendedTone, bestVoice);

            // Calculate confidence score
            double confidence = calculateConfidenceScore(speechText, detectedTone, recommendedTone);

            return new VoiceRecommendation(bestVoice, recommendedTone, reason, confidence);

        } catch (Exception e) {
            log.error("Error in voice analysis: {}", e.getMessage(), e);
            // Return default recommendation
            return getDefaultRecommendation();
        }
    }

    /**
     * Determine what tone would help improve the speech
     */
    private ToneType determineRecommendedTone(String speechText, String detectedTone) {
        String lowerText = speechText.toLowerCase();
        String lowerDetectedTone = detectedTone.toLowerCase();

        // If speech has hesitation markers, recommend confidence
        if (lowerText.contains("um") || lowerText.contains("uh") ||
                lowerDetectedTone.contains("hesitant") || lowerDetectedTone.contains("uncertain")) {
            return ToneType.CONFIDENT;
        }

        // If speech is about results/business, recommend confidence
        if (lowerText.contains("results") || lowerText.contains("growth") ||
                lowerText.contains("achievement") || lowerText.contains("success")) {
            return ToneType.CONFIDENT;
        }

        // If speech is about support/help, recommend empathy
        if (lowerText.contains("support") || lowerText.contains("help") ||
                lowerText.contains("understand") || lowerText.contains("together")) {
            return ToneType.EMPATHETIC;
        }

        // If speech has urgency markers, recommend urgent tone
        if (lowerText.contains("urgent") || lowerText.contains("immediately") ||
                lowerText.contains("quickly") || lowerText.contains("deadline")) {
            return ToneType.URGENT;
        }

        // Default to conversational for most cases
        return ToneType.CONVERSATIONAL;
    }

    /**
     * Select the best voice from available options with intelligent selection
     */
    private VoiceOption selectBestVoice(List<VoiceOption> availableVoices, ToneType recommendedTone, String speechText) {
        // Filter voices that support the recommended tone
        List<VoiceOption> suitableVoices = availableVoices.stream()
                .filter(voice -> voice.supportsTone(recommendedTone))
                .toList();

        if (suitableVoices.isEmpty()) {
            // If no voices support the tone, return the first available voice
            return availableVoices.isEmpty() ? getDefaultVoiceOption() : availableVoices.get(0);
        }

        // Smart voice selection based on content and tone
        String lowerText = speechText.toLowerCase();

        // For confident tone - prefer variety based on content
        if (recommendedTone == ToneType.CONFIDENT) {
            // For business/professional content
            if (lowerText.contains("business") || lowerText.contains("professional") ||
                    lowerText.contains("quarterly") || lowerText.contains("results")) {
                // Alternate between Marcus and Julia for business
                return selectByNamePreference(suitableVoices, Arrays.asList("Julia", "Marcus", "Emily"));
            }
            // For presentations with authority needs
            if (lowerText.contains("team") || lowerText.contains("everyone")) {
                return selectByNamePreference(suitableVoices, Arrays.asList("Marcus", "Julia"));
            }
        }

        // For conversational tone - prefer friendly voices
        if (recommendedTone == ToneType.CONVERSATIONAL) {
            return selectByNamePreference(suitableVoices, Arrays.asList("Alex", "Sophia", "Julia"));
        }

        // For urgent tone - prefer energetic voices
        if (recommendedTone == ToneType.URGENT) {
            return selectByNamePreference(suitableVoices, Arrays.asList("Marcus", "Emily", "Thomas"));
        }

        // For empathetic tone - prefer warm voices
        if (recommendedTone == ToneType.EMPATHETIC) {
            return selectByNamePreference(suitableVoices, Arrays.asList("Sophia", "Julia", "Alex"));
        }

        // Default: rotate through available voices to avoid always picking the same one
        return selectRandomVoice(suitableVoices);
    }

    /**
     * Select voice by name preference order
     */
    private VoiceOption selectByNamePreference(List<VoiceOption> voices, List<String> preferredNames) {
        for (String preferredName : preferredNames) {
            VoiceOption preferred = voices.stream()
                    .filter(voice -> voice.getName().equalsIgnoreCase(preferredName))
                    .findFirst()
                    .orElse(null);
            if (preferred != null) {
                return preferred;
            }
        }
        // If no preferred names found, return first available
        return voices.get(0);
    }

    /**
     * Select a voice with some randomization to avoid always picking the same one
     */
    private VoiceOption selectRandomVoice(List<VoiceOption> voices) {
        if (voices.size() == 1) {
            return voices.get(0);
        }

        // Use a simple rotation based on current time to add variety
        int index = (int) (System.currentTimeMillis() % voices.size());
        return voices.get(index);
    }

    /**
     * Generate human-readable recommendation reason
     */
    private String generateRecommendationReason(String detectedTone, ToneType recommendedTone, VoiceOption voice) {
        return String.format(
                "Your speech shows a %s delivery style. To improve impact, I recommend using a %s tone " +
                        "like the %s voice, which will help you %s and connect better with your audience.",
                detectedTone,
                recommendedTone.getValue(),
                voice.getName(),
                getImprovementBenefit(recommendedTone)
        );
    }

    private String getImprovementBenefit(ToneType tone) {
        return switch (tone) {
            case CONFIDENT -> "project authority and credibility";
            case CONVERSATIONAL -> "engage naturally and build rapport";
            case URGENT -> "convey importance and drive action";
            case EMPATHETIC -> "show understanding and build trust";
        };
    }

    /**
     * Calculate confidence score based on analysis quality
     */
    private double calculateConfidenceScore(String speechText, String detectedTone, ToneType recommendedTone) {
        double baseScore = 0.7; // Base confidence

        // Higher confidence for longer speeches (more data to analyze)
        if (speechText.length() > 200) baseScore += 0.1;
        if (speechText.length() > 500) baseScore += 0.1;

        // Higher confidence for clear tone detection
        if (!detectedTone.equals("neutral")) baseScore += 0.1;

        return Math.min(0.95, baseScore); // Cap at 95%
    }

    /**
     * Default recommendation when analysis fails
     */
    private VoiceRecommendation getDefaultRecommendation() {
        VoiceOption defaultVoice = getDefaultVoiceOption();
        return new VoiceRecommendation(
                defaultVoice,
                ToneType.CONVERSATIONAL,
                "A conversational tone will help you connect naturally with your audience.",
                0.6
        );
    }

    private VoiceOption getDefaultVoiceOption() {
        return VoiceOption.createProfessionalMale("en-US-alex", "Alex", "American");
    }
}
