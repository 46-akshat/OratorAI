//package com.hackathon.aipresentationbackend.controller;
//
//import com.hackathon.aipresentationbackend.model.*;
//import com.hackathon.aipresentationbackend.service.VoiceRecommendationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Test controller for voice recommendation system
// */
//@RestController
//@RequestMapping("/api/test")
//@CrossOrigin(origins = "*")
//public class TestController {
//
//    private final VoiceRecommendationService voiceRecommendationService;
//
//    @Autowired
//    public TestController(VoiceRecommendationService voiceRecommendationService) {
//        this.voiceRecommendationService = voiceRecommendationService;
//    }
//
//    /**
//     * Test endpoint to verify voice recommendation system
//     */
//    @PostMapping("/voice-recommendation")
//    public ResponseEntity<Map<String, Object>> testVoiceRecommendation() {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // Test with sample speech text
//            String sampleSpeech = "Hello everyone, welcome to today's presentation. " +
//                    "I'm excited to share our quarterly results with you. " +
//                    "Our team has worked incredibly hard to achieve these milestones.";
//
//            VoiceAnalysis analysis = voiceRecommendationService.getVoiceRecommendation(
//                sampleSpeech,
//                "business",
//                "professional",
//                VoiceGender.MALE
//            );
//
//            result.put("success", true);
//            result.put("analysis", analysis);
//            result.put("message", "Voice recommendation system is working!");
//
//            return ResponseEntity.ok(result);
//
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("error", e.getMessage());
//            result.put("message", "Voice recommendation system test failed");
//
//            return ResponseEntity.internalServerError().body(result);
//        }
//    }
//
//    /**
//     * Test endpoint to list all available voices
//     */
//    @GetMapping("/voices")
//    public ResponseEntity<Map<String, Object>> testVoices() {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            result.put("success", true);
//            result.put("voices", Arrays.asList(MurfVoice.values()));
//            result.put("tones", Arrays.asList(ToneType.values()));
//            result.put("genders", Arrays.asList(VoiceGender.values()));
//            result.put("ages", Arrays.asList(VoiceAge.values()));
//            result.put("message", "Voice system models are working!");
//
//            return ResponseEntity.ok(result);
//
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("error", e.getMessage());
//
//            return ResponseEntity.internalServerError().body(result);
//        }
//    }
//
//    /**
//     * Health check endpoint
//     */
//    @GetMapping("/health")
//    public ResponseEntity<Map<String, String>> health() {
//        Map<String, String> status = new HashMap<>();
//        status.put("status", "UP");
//        status.put("service", "Voice Recommendation API");
//        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
//
//        return ResponseEntity.ok(status);
//    }
//}