package com.hackathon.aipresentationbackend.controller;

import com.hackathon.aipresentationbackend.model.AnalysisRequest;
import com.hackathon.aipresentationbackend.model.AnalysisResponse;
import com.hackathon.aipresentationbackend.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalysisController {

     private final AnalysisService analysisService;

     public  AnalysisController(AnalysisService analysisService){
         this.analysisService=analysisService;
     }
     @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyzedelivery(@RequestBody AnalysisRequest analysisRequest){
         AnalysisResponse response=analysisService.analyzeAndGenerateAudio(analysisRequest);
         return ResponseEntity.ok(response);
     }
      @PostMapping(value = "/test-transcribe",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<String> testtranscription(@RequestParam("audioFile")MultipartFile audioFile){
         try{
             byte[] audiodata= audioFile.getBytes();
             String transcript=analysisService.transcribeAudio(audiodata);
             return ResponseEntity.ok(transcript);
         } catch (IOException e) {
             return  ResponseEntity.status(500).body("Error reading file:"+e.getMessage());
         }
         catch (Exception e) {
             return ResponseEntity.status(500).body("Transcription failed: " + e.getMessage());
         }
      }
    @PostMapping("/test-speak")
      public ResponseEntity<String> testspeech(@RequestBody String text){
             try{
                 String audioUrl=analysisService.generateSpeech(text);
                 return ResponseEntity.ok(audioUrl);
             } catch (Exception e){
                 return ResponseEntity.status(500).body("Speech Generation failed:"+e.getMessage());
             }
      }
}
