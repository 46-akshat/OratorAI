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

            // We now use the originalScript as the text to generate.
            // We can pick a professional and clear voice for the ideal delivery.
            SpeechRequest speechRequest = new SpeechRequest(originalScript, "en-US-julia", 1.0, "conversational");
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
        SpeechRequest request = new SpeechRequest(textToSpeak, "en-US-julia", 1.0, null);
        SpeechResponse response = murfService.generateSpeech(request);
        return response.getAudioUrl();
    }
}
