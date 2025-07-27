//package com.hackathon.aipresentationbackend.service;
//
//import com.hackathon.aipresentationbackend.model.AnalysisResponse;
//import com.hackathon.aipresentationbackend.model.SpeechRequest;
//import com.hackathon.aipresentationbackend.model.SpeechResponse;
//import com.hackathon.aipresentationbackend.model.VoiceRecommendation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class AnalysisService {
//    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);
//
//    // Services for each external API are injected
//    private final GeminiService geminiService;
//    private final MurfService murfService;
//    private final AssemblyAIService assemblyAIService;
//    private final VoiceAnalysisService voiceAnalysisService; // New service for voice recommendations
//
//    public AnalysisService(GeminiService geminiService, MurfService murfService,
//                           AssemblyAIService assemblyAIService, VoiceAnalysisService voiceAnalysisService) {
//        this.geminiService = geminiService;
//        this.murfService = murfService;
//        this.assemblyAIService = assemblyAIService;
//        this.voiceAnalysisService = voiceAnalysisService;
//    }
//
//    /**
//     * Orchestrates the entire workflow for the main /analyze endpoint.
//     * 1. Transcribes the audio file to get the spoken text.
//     * 2. Sends the transcript and original script to Gemini for analysis.
//     * 3. Generates audio feedback from the improvement points provided by Gemini.
//     *
//     * @param audioFile The audio file recorded by the user.
//     * @param originalScript The original script the user was practicing.
//     * @return A complete AnalysisResponse with score, feedback, and an audio URL.
//     * @throws IOException if there's an issue reading the audio file.
//     * @throws InterruptedException if the transcription polling is interrupted.
//     */
//    public AnalysisResponse transcribeAndAnalyze(MultipartFile audioFile, String originalScript) throws IOException, InterruptedException {
//        log.info("Starting full transcription and analysis process...");
//
//        // Step 1: Transcribe the audio to get the spoken text
//        String spokenTranscript = assemblyAIService.transcribeAudio(audioFile);
//
//        // Step 2: Send both scripts to Gemini for analysis
//        AnalysisResponse analysisFromGemini = geminiService.analyzePresentation(originalScript, spokenTranscript);
//
//        // Step 3: NEW - Analyze speech for voice recommendation
//        VoiceRecommendation voiceRecommendation = null;
//        try {
//            log.info("Analyzing speech for voice recommendation...");
//            // Extract detected tone from Gemini analysis or use a simple detection
//            String detectedTone = extractToneFromAnalysis(analysisFromGemini, spokenTranscript);
//            voiceRecommendation = voiceAnalysisService.analyzeAndRecommendVoice(spokenTranscript, detectedTone);
//        } catch (Exception e) {
//            log.warn("Voice recommendation failed, continuing without it: {}", e.getMessage());
//        }
//
//        // Step 4: Generate audio using recommended voice (or fallback to default)
//        try {
//            log.info("Generating ideal audio delivery for the original script.");
//
//            String voiceId = "en-US-julia"; // Default voice
//            String tone = "conversational"; // Default tone
//
//            // Use recommended voice if available
//            if (voiceRecommendation != null && voiceRecommendation.getVoiceOption() != null) {
//                voiceId = voiceRecommendation.getVoiceOption().getVoiceId();
//                tone = voiceRecommendation.getRecommendedTone().getValue();
//                log.info("Using recommended voice: {} with tone: {}", voiceId, tone);
//            }
//
//            SpeechRequest speechRequest = new SpeechRequest(originalScript, voiceId, 1.0, tone);
//            SpeechResponse speechResponse = murfService.generateSpeech(speechRequest);
//
//            // Create a new, final response that includes everything
//            return new AnalysisResponse.Builder()
//                    .score(analysisFromGemini.getScore())
//                    .positiveFeedback(analysisFromGemini.getPositiveFeedback())
//                    .improvementPoints(analysisFromGemini.getImprovementPoints())
//                    .spokenTranscript(spokenTranscript)
//                    .audioUrl(speechResponse.getAudioUrl())
//                    .voiceRecommendation(voiceRecommendation) // NEW - Include voice recommendation
//                    .build();
//
//        } catch (Exception e) {
//            log.error("Failed to generate ideal audio delivery, returning analysis without it. Error: {}", e.getMessage());
//            // If audio generation fails, we still return the valuable text feedback from Gemini.
//            return new AnalysisResponse.Builder()
//                    .score(analysisFromGemini.getScore())
//                    .positiveFeedback(analysisFromGemini.getPositiveFeedback())
//                    .improvementPoints(analysisFromGemini.getImprovementPoints())
//                    .spokenTranscript(spokenTranscript)
//                    .voiceRecommendation(voiceRecommendation) // Include even if audio generation failed
//                    .build();
//        }
//    }
//
//    /**
//     * A wrapper method for the /test-transcribe endpoint.
//     * Delegates directly to the AssemblyAIService.
//     */
//    public String transcribeAudio(MultipartFile audioFile) throws IOException, InterruptedException {
//        log.info("Delegating transcription for test endpoint to AssemblyAIService...");
//        return assemblyAIService.transcribeAudio(audioFile);
//    }
//
//    /**
//     * A wrapper method for the /test-speak endpoint.
//     * Delegates directly to the MurfService.
//     */
//    public String generateSpeech(String textToSpeak) {
//        log.info("Delegating speech generation for test endpoint to MurfService...");
//        SpeechRequest request = new SpeechRequest(textToSpeak, "en-US-julia", 1.0, null);
//        SpeechResponse response = murfService.generateSpeech(request);
//        return response.getAudioUrl();
//    }
//
//    /**
//     * Extract detected tone from Gemini analysis or speech text
//     */
//    private String extractToneFromAnalysis(AnalysisResponse analysis, String spokenTranscript) {
//        // Try to extract tone from improvement points
//        String improvementPoints = analysis.getImprovementPoints().toLowerCase();
//
//        if (improvementPoints.contains("filler") || improvementPoints.contains("um") ||
//                improvementPoints.contains("hesitat")) {
//            return "hesitant";
//        }
//
//        if (improvementPoints.contains("confidence") || improvementPoints.contains("authority")) {
//            return "needs confidence";
//        }
//
//        if (improvementPoints.contains("energy") || improvementPoints.contains("enthusiasm")) {
//            return "low energy";
//        }
//
//        // Analyze the spoken transcript directly
//        String lowerTranscript = spokenTranscript.toLowerCase();
//        if (lowerTranscript.contains("um") || lowerTranscript.contains("uh")) {
//            return "hesitant";
//        }
//
//        // Default to neutral if we can't determine
//        return "neutral";
//    }
//}
package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AnalysisService {
    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    // Services for each external API are injected
    private final GeminiService geminiService;
    private final MurfService murfService;
    private final AssemblyAIService assemblyAIService;

    public AnalysisService(GeminiService geminiService, MurfService murfService, AssemblyAIService assemblyAIService) {
        this.geminiService = geminiService;
        this.murfService = murfService;
        this.assemblyAIService = assemblyAIService;
    }

    /**
     * Orchestrates the entire workflow for the main /analyze endpoint.
     * 1. Transcribes the audio file to get the spoken text.
     * 2. Sends the transcript and original script to Gemini for analysis.
     * 3. Generates audio feedback from the improvement points provided by Gemini.
     *
     * @param audioFile The audio file recorded by the user.
     * @param originalScript The original script the user was practicing.
     * @return A complete AnalysisResponse with score, feedback, and an audio URL.
     * @throws IOException if there's an issue reading the audio file.
     * @throws InterruptedException if the transcription polling is interrupted.
     */
    public AnalysisResponse transcribeAndAnalyze(MultipartFile audioFile, String originalScript) throws IOException, InterruptedException {
        log.info("Starting full transcription and analysis process...");

        // Step 1: Transcribe the audio to get the spoken text
        String spokenTranscript = assemblyAIService.transcribeAudio(audioFile);

        // Step 2: Send both scripts to Gemini for analysis
        AnalysisResponse analysisFromGemini = geminiService.analyzePresentation(originalScript, spokenTranscript);

        // --- THIS IS THE UPDATED LOGIC ---
        // Step 3: Generate an IDEAL audio delivery of the ORIGINAL script.
        try {
            log.info("Generating ideal audio delivery for the original script.");

            // CORRECTED: The voice is now set to Marcus
            SpeechRequest speechRequest = new SpeechRequest(originalScript, "en-US-marcus", 1.0, "conversational");
            SpeechResponse speechResponse = murfService.generateSpeech(speechRequest);

            // Create a new, final response that includes the Gemini analysis AND the new audio URL.
            return new AnalysisResponse.Builder()
                    .score(analysisFromGemini.getScore())
                    .positiveFeedback(analysisFromGemini.getPositiveFeedback())
                    .improvementPoints(analysisFromGemini.getImprovementPoints())
                    .spokenTranscript(spokenTranscript) // It's good practice to return the transcript too
                    .audioUrl(speechResponse.getAudioUrl())
                    .build();

        } catch (Exception e) {
            log.error("Failed to generate ideal audio delivery, returning analysis without it. Error: {}", e.getMessage());
            // If audio generation fails, we still return the valuable text feedback from Gemini.
            return analysisFromGemini;
        }
    }

    /**
     * A wrapper method for the /test-transcribe endpoint.
     * Delegates directly to the AssemblyAIService.
     */
    public String transcribeAudio(MultipartFile audioFile) throws IOException, InterruptedException {
        log.info("Delegating transcription for test endpoint to AssemblyAIService...");
        return assemblyAIService.transcribeAudio(audioFile);
    }

    /**
     * A wrapper method for the /test-speak endpoint.
     * Delegates directly to the MurfService.
     */
    public String generateSpeech(String textToSpeak) {
        log.info("Delegating speech generation for test endpoint to MurfService...");
        // CORRECTED: The voice is also updated here for consistency in testing
        SpeechRequest request = new SpeechRequest(textToSpeak, "en-US-marcus", 1.0, null);
        SpeechResponse response = murfService.generateSpeech(request);
        return response.getAudioUrl();
    }
}
