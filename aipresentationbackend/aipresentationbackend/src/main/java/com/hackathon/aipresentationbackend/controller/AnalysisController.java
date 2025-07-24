package com.hackathon.aipresentationbackend.controller;

import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import com.hackathon.aipresentationbackend.service.AnalysisService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:8081", "https://d56632c13c30.ngrok-free.app/"}) // Ensure this port matches your frontend
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService){
        this.analysisService = analysisService;
    }

    /**
     * This is the main endpoint for the application.
     * It accepts a multipart/form-data request containing both an audio file and the original script text.
     */
    @PostMapping(value = "/analyze", consumes = "multipart/form-data")
    public ResponseEntity<AnalysisResponse> analyzeDelivery(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("originalScript") String originalScript) {

        try {
            // This method correctly handles both the file and the script
            AnalysisResponse response = analysisService.transcribeAndAnalyze(audioFile, originalScript);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Your GlobalExceptionHandler will catch and format errors from the service
            throw new RuntimeException("Failed to fully analyze presentation: " + e.getMessage(), e);
        }
    }

    /**
     * Test endpoint for AssemblyAI transcription.
     */
    @PostMapping(value = "/test-transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> testTranscription(@RequestParam("audioFile") MultipartFile audioFile) {
        try {
            String transcript = analysisService.transcribeAudio(audioFile);
            return ResponseEntity.ok(transcript);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Good practice when catching InterruptedException
            return ResponseEntity.status(500).body("Transcription failed: " + e.getMessage());
        }
    }

    /**
     * Test endpoint for Murf.ai text-to-speech.
     */
    @PostMapping("/test-speak")
    public ResponseEntity<String> testSpeech(@RequestBody String text) {
        try {
            String audioUrl = analysisService.generateSpeech(text);
            return ResponseEntity.ok(audioUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Speech Generation failed: " + e.getMessage());
        }
    }
}
