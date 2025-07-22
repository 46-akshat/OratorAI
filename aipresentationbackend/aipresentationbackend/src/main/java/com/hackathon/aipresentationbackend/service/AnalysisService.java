package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.model.AnalysisRequest;
import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import com.hackathon.aipresentationbackend.model.MurfRequest;
import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

@Service
public class AnalysisService {
    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);
    private final WebClient webClient;
    private final GeminiService geminiService;
    private final MurfService murfService;
    private final AssemblyAIService assemblyAIService;

    @Value("${assemblyai.api.key}")
    private String assemblyApiKey;

    public AnalysisService(WebClient webClient, GeminiService geminiService, MurfService murfService, AssemblyAIService assemblyAIService) {
        this.webClient = webClient;
        this.geminiService = geminiService;
        this.murfService = murfService;
        this.assemblyAIService = assemblyAIService;
    }

    public AnalysisResponse analyzeAndGenerateAudio(AnalysisRequest analysisRequest) {
        log.info("Starting analysis for script...");
        
        // Check if we have both original script and spoken script
        if (analysisRequest.getOriginalscript() != null && !analysisRequest.getOriginalscript().isEmpty() &&
            analysisRequest.getSpokenscript() != null && !analysisRequest.getSpokenscript().isEmpty()) {
            
            // Use Gemini to analyze the presentation
            AnalysisResponse analysisResponse = geminiService.analyzePresentation(
                    analysisRequest.getOriginalscript(), 
                    analysisRequest.getSpokenscript());
            
            // Generate audio for improvement example if needed
            if (analysisResponse.getImprovementPoints() != null && !analysisResponse.getImprovementPoints().isEmpty()) {
                try {
                    // Extract a key sentence from improvement points to generate as audio example
                    String exampleText = extractKeyExampleSentence(analysisResponse.getImprovementPoints());
                    
                    // Generate speech for the example
                    SpeechRequest speechRequest = new SpeechRequest(exampleText, "en-US-julia", 1.0, null);
                    SpeechResponse speechResponse = murfService.generateSpeech(speechRequest);
                    
                    // Update the analysis response with the audio URL
                    return new AnalysisResponse.Builder()
                            .score(analysisResponse.getScore())
                            .positiveFeedback(analysisResponse.getPositiveFeedback())
                            .improvementPoints(analysisResponse.getImprovementPoints())
                            .audioUrl(speechResponse.getAudioUrl())
                            .build();
                } catch (Exception e) {
                    log.error("Error generating audio example: {}", e.getMessage());
                    // Return the analysis without audio if speech generation fails
                    return analysisResponse;
                }
            }
            
            return analysisResponse;
        } else {
            log.warn("Incomplete analysis request, using mock response");
            return getMockResponse();
        }
    }

    private String extractKeyExampleSentence(String improvementPoints) {
        // Extract a key sentence from the improvement points to use as an audio example
        // In a real implementation, this would be more sophisticated
        
        // For now, just take the first sentence or a portion of the text
        String example = improvementPoints;
        if (example.contains(".")) {
            example = example.substring(0, example.indexOf(".") + 1);
        }
        
        // Limit to a reasonable length
        if (example.length() > 200) {
            example = example.substring(0, 197) + "...";
        }
        
        return "Here's how to improve: " + example;
    }

    private AnalysisResponse getMockResponse() {
        log.warn("Using Mock response for analysis. Backend logic is not yet implemented.");
        return new AnalysisResponse.Builder()
                .score(8)
                .positiveFeedback("This is a mock response: Your pacing was excellent!")
                .improvementPoints("This is a Mock response: Try to vary your tone more")
                .audioUrl("mock.mp3")
                .build();
    }

    /**
     * Transcribe audio from a byte array
     * 
     * @param audioData The audio data to transcribe
     * @return The transcription text
     * @throws IOException if there is an error processing the audio
     * @throws InterruptedException if the transcription is interrupted
     */
    public String transcribeAudio(byte[] audioData) throws IOException, InterruptedException {
        log.info("Transcribing audio data of size: {} bytes", audioData.length);
        
        // This method is kept for backward compatibility
        // In a real implementation, we would convert the byte array to a MultipartFile
        // and call the AssemblyAIService
        
        return "Transcription pending implementation for byte array input. Please use MultipartFile version.";
    }
    
    /**
     * Transcribe audio from a MultipartFile
     * 
     * @param audioFile The audio file to transcribe
     * @return The transcription text
     * @throws IOException if there is an error processing the audio
     */
    public String transcribeAudio(MultipartFile audioFile) throws IOException {
        log.info("Transcribing audio file: {}", audioFile.getOriginalFilename());
        return assemblyAIService.transcribeAudio(audioFile);
    }

    public String generateSpeech(String textToSpeak) {
        log.info("Generating speech with Murf.ai...");
        
        SpeechRequest request = new SpeechRequest(textToSpeak, "en-US-julia", 1.0, null);
        SpeechResponse response = murfService.generateSpeech(request);
        
        return response.getAudioUrl();
    }
}