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

}
