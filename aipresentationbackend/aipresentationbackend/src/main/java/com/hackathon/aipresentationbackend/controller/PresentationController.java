package com.hackathon.aipresentationbackend.controller;

import com.hackathon.aipresentationbackend.model.SpeechRequest;
import com.hackathon.aipresentationbackend.model.SpeechResponse;
import com.hackathon.aipresentationbackend.model.ToneRehearsalRequest;
import com.hackathon.aipresentationbackend.model.VoiceOption;
import com.hackathon.aipresentationbackend.service.MurfService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:8081", "https://d56632c13c30.ngrok-free.app/"})
public class PresentationController {
    private final MurfService murfService;

    public PresentationController(MurfService murfService){
        this.murfService=murfService;
    }
@PostMapping("/generate-speech")
 public ResponseEntity<SpeechResponse> generateSpeech(@Valid @RequestBody SpeechRequest request){
   SpeechResponse response= murfService.generateSpeech(request);

   return  ResponseEntity.ok(response);
 }
 @PostMapping("/tone-rehearsal")
  public ResponseEntity<SpeechResponse> rehearseTone(@RequestBody ToneRehearsalRequest toneRehearsalRequest){
    SpeechResponse response=murfService.generateToneVariation(toneRehearsalRequest);
    return ResponseEntity.ok(response);
  }
 @GetMapping("/voices")
  public ResponseEntity<List<VoiceOption>> getAvailableVoices(){
     List<VoiceOption> voices=murfService.getAvailableVoices();
     return ResponseEntity.ok(voices);
  }

 /**
  * Test specific voice IDs to find which ones work with Murf AI
  */
 @PostMapping("/test-voice")
 public ResponseEntity<java.util.Map<String, Object>> testVoiceId(@RequestBody java.util.Map<String, String> request) {
     java.util.Map<String, Object> result = new java.util.HashMap<>();
     
     try {
         String text = request.get("text");
         String voiceId = request.get("voiceId");
         
         if (text == null || voiceId == null) {
             result.put("success", false);
             result.put("error", "Both 'text' and 'voiceId' are required");
             return ResponseEntity.badRequest().body(result);
         }
         
         // Try to generate speech with the provided voice ID
         SpeechRequest speechRequest = new SpeechRequest(text, voiceId, 1.0, null);
         SpeechResponse response = murfService.generateSpeech(speechRequest);
         
         result.put("success", true);
         result.put("voiceId", voiceId);
         result.put("audioUrl", response.getAudioUrl());
         result.put("message", "Voice ID works!");
         
         return ResponseEntity.ok(result);
         
     } catch (Exception e) {
         result.put("success", false);
         result.put("voiceId", request.get("voiceId"));
         result.put("error", e.getMessage());
         result.put("message", "Voice ID failed - try a different one");
         
         return ResponseEntity.ok(result); // Return 200 even for failed voice IDs
     }
 }

}
