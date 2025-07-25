//package com.hackathon.aipresentationbackend.service;
//
//import com.hackathon.aipresentationbackend.model.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
///**
// * Service for orchestrating voice analysis and recommendation workflow
// */
//@Service
//public class VoiceRecommendationService {
//
//    private static final Logger log = LoggerFactory.getLogger(VoiceRecommendationService.class);
//
//    private final GeminiService geminiService;
//    private final MurfService murfService;
//    private final AssemblyAIService assemblyAIService;
//    private final MockVoiceRecommendationService mockService;
//
//    @Value("${mock.mode:false}")
//    private boolean mockMode;
//
//    @Autowired
//    public VoiceRecommendationService(GeminiService geminiService,
//                                    MurfService murfService,
//                                    AssemblyAIService assemblyAIService,
//                                    @Autowired(required = false) MockVoiceRecommendationService mockService) {
//        this.geminiService = geminiService;
//        this.murfService = murfService;
//        this.assemblyAIService = assemblyAIService;
//        this.mockService = mockService;
//    }
//
//    /**
//     * Complete workflow: analyze speech, recommend voice, generate audio
//     */
//    public VoiceRecommendationResponse processVoiceRecommendation(VoiceRecommendationRequest request) {
//        // Use mock service if in mock mode
//        if (mockMode && mockService != null) {
//            log.info("Using mock service for voice recommendation");
//            return mockService.processVoiceRecommendation(request);
//        }
//
//        try {
//            log.info("Processing voice recommendation request");
//
//            // Step 1: Convert audio to text if needed
//            String speechText = request.getSpeechText();
//            if (speechText == null || speechText.trim().isEmpty()) {
//                if (request.getAudioBase64() != null && !request.getAudioBase64().trim().isEmpty()) {
//                    log.info("Converting audio to text using AssemblyAI");
//                    TranscriptionResponse transcription = assemblyAIService.transcribeAudio(request.getAudioBase64());
//                    speechText = transcription.getText();
//                } else {
//                    return VoiceRecommendationResponse.error("Either speechText or audioBase64 must be provided");
//                }
//            }
//
//            // Step 2: Analyze speech and get voice recommendation from Gemini
//            log.info("Analyzing speech with Gemini AI");
//            VoiceAnalysis analysis = geminiService.analyzeVoiceAndRecommend(
//                speechText,
//                request.getPresentationType(),
//                request.getTargetAudience(),
//                request.getPreferredGender()
//            );
//
//            // Step 3: Generate audio using recommended voice with Murf AI
//            log.info("Generating audio with recommended voice: {}", analysis.getRecommendedVoice().getVoiceId());
//            String audioUrl = generateAudioWithMurf(speechText, analysis.getRecommendedVoice());
//
//            // Step 4: Return complete response
//            VoiceRecommendationResponse response = VoiceRecommendationResponse.success(analysis, audioUrl);
//            log.info("Voice recommendation process completed successfully");
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error processing voice recommendation: {}", e.getMessage(), e);
//            return VoiceRecommendationResponse.error("Failed to process voice recommendation: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Generate audio using Murf AI with the recommended voice
//     */
//    private String generateAudioWithMurf(String text, MurfVoice recommendedVoice) {
//        try {
//            // Create Murf request with recommended voice
//            MurfRequest murfRequest = new MurfRequest();
//            murfRequest.setText(text);
//            murfRequest.setVoiceId(recommendedVoice.getVoiceId());
//            murfRequest.setTone(recommendedVoice.getPrimaryTone().getValue());
//
//            // Generate audio
//            return murfService.generateSpeech(murfRequest);
//
//        } catch (Exception e) {
//            log.error("Error generating audio with Murf AI: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to generate audio with recommended voice", e);
//        }
//    }
//
//    /**
//     * Get voice recommendation without generating audio
//     */
//    public VoiceAnalysis getVoiceRecommendation(String speechText, String presentationType,
//                                              String targetAudience, VoiceGender preferredGender) {
//        log.info("Getting voice recommendation for speech analysis");
//        return geminiService.analyzeVoiceAndRecommend(speechText, presentationType, targetAudience, preferredGender);
//    }
//
//    /**
//     * Generate audio for a specific voice (for testing different voices)
//     */
//    public String generateAudioForVoice(String text, MurfVoice voice) {
//        log.info("Generating audio for voice: {}", voice.getVoiceId());
//        return generateAudioWithMurf(text, voice);
//    }
//}
package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for orchestrating voice analysis and recommendation workflow
 */
@Service
public class VoiceRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(VoiceRecommendationService.class);

    private final GeminiService geminiService;
    private final MurfService murfService;
    private final AssemblyAIService assemblyAIService;
    private final MockVoiceRecommendationService mockService;

    @Value("${mock.mode:false}")
    private boolean mockMode;

    @Autowired
    public VoiceRecommendationService(GeminiService geminiService,
                                      MurfService murfService,
                                      AssemblyAIService assemblyAIService,
                                      @Autowired(required = false) MockVoiceRecommendationService mockService) {
        this.geminiService = geminiService;
        this.murfService = murfService;
        this.assemblyAIService = assemblyAIService;
        this.mockService = mockService;
    }

    /**
     * Complete workflow: analyze speech, recommend voice, generate audio
     */
    public VoiceRecommendationResponse processVoiceRecommendation(VoiceRecommendationRequest request) {
        // Use mock service if in mock mode
        if (mockMode && mockService != null) {
            log.info("Using mock service for voice recommendation");
            return mockService.processVoiceRecommendation(request);
        }

        try {
            log.info("Processing voice recommendation request");

            // Step 1: Convert audio to text if needed
            String speechText = request.getSpeechText();
            if (speechText == null || speechText.trim().isEmpty()) {
                if (request.getAudioBase64() != null && !request.getAudioBase64().trim().isEmpty()) {
                    log.info("Converting audio to text using AssemblyAI");
                    // CORRECTED: The method returns a String, so assign it to the speechText variable.
                    speechText = assemblyAIService.transcribeAudio(request.getAudioBase64());
                } else {
                    return VoiceRecommendationResponse.error("Either speechText or audioBase64 must be provided");
                }
            }

            // Step 2: Analyze speech and get voice recommendation from Gemini
            log.info("Analyzing speech with Gemini AI");
            VoiceAnalysis analysis = geminiService.analyzeVoiceAndRecommend(
                    speechText,
                    request.getPresentationType(),
                    request.getTargetAudience(),
                    request.getPreferredGender()
            );

            // Step 3: Generate audio using recommended voice with Murf AI
            log.info("Generating audio with recommended voice: {}", analysis.getRecommendedVoice().getVoiceId());
            String audioUrl = generateAudioWithMurf(speechText, analysis.getRecommendedVoice());

            // Step 4: Return complete response
            VoiceRecommendationResponse response = VoiceRecommendationResponse.success(analysis, audioUrl);
            log.info("Voice recommendation process completed successfully");
            return response;

        } catch (Exception e) {
            log.error("Error processing voice recommendation: {}", e.getMessage(), e);
            return VoiceRecommendationResponse.error("Failed to process voice recommendation: " + e.getMessage());
        }
    }

    /**
     * Generate audio using Murf AI with the recommended voice
     */
    private String generateAudioWithMurf(String text, MurfVoice recommendedVoice) {
        try {
            // Create Murf request with recommended voice
            SpeechRequest murfRequest = new SpeechRequest();
            murfRequest.setText(text);
            murfRequest.setVoiceId(recommendedVoice.getVoiceId());
            murfRequest.setTone(recommendedVoice.getPrimaryTone().getValue());

            // Generate audio
            SpeechResponse speechResponse = murfService.generateSpeech(murfRequest);
            return speechResponse.getAudioUrl();

        } catch (Exception e) {
            log.error("Error generating audio with Murf AI: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate audio with recommended voice", e);
        }
    }

    /**
     * Get voice recommendation without generating audio
     */
    public VoiceAnalysis getVoiceRecommendation(String speechText, String presentationType,
                                                String targetAudience, VoiceGender preferredGender) {
        log.info("Getting voice recommendation for speech analysis");
        return geminiService.analyzeVoiceAndRecommend(speechText, presentationType, targetAudience, preferredGender);
    }

    /**
     * Generate audio for a specific voice (for testing different voices)
     */
    public String generateAudioForVoice(String text, MurfVoice voice) {
        log.info("Generating audio for voice: {}", voice.getVoiceId());
        return generateAudioWithMurf(text, voice);
    }
}
