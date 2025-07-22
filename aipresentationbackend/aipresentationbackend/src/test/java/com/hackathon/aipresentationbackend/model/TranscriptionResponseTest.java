package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranscriptionResponseTest {

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        String id = "test-id";
        String status = "completed";
        String text = "This is a test transcription.";
        String audioUrl = "https://example.com/audio.mp3";
        String error = null;
        List<TranscriptionResponse.Word> words = new ArrayList<>();
        List<TranscriptionResponse.Utterance> utterances = new ArrayList<>();
        Double confidence = 0.95;
        Double audioDuration = 30.5;
        Boolean punctuate = true;
        Boolean formatText = true;
        Boolean dualChannel = false;
        String webhookUrl = "https://example.com/webhook";
        Integer webhookStatusCode = 200;
        Boolean webhookAuth = true;
        Boolean speedBoost = true;
        Object autoHighlightsResult = new Object();
        Boolean autoHighlights = true;
        Integer audioStartFrom = 0;
        Integer audioEndAt = 30;
        List<String> wordBoost = new ArrayList<>();
        String boostParam = "high";
        Boolean filterProfanity = true;
        Boolean redactPii = true;
        Boolean redactPiiAudio = true;
        String redactPiiAudioQuality = "high";
        List<String> redactPiiPolicies = new ArrayList<>();
        String redactPiiSub = "PII";
        Boolean speakerLabels = true;
        Boolean contentSafety = true;
        Boolean iabCategories = true;
        String languageCode = "en_us";
        Object customSpelling = new Object();
        Boolean disfluencies = true;
        Boolean sentimentAnalysis = true;
        Boolean autoChapters = true;
        Boolean entityDetection = true;
        Double speechThreshold = 0.5;
        
        // Act
        response.setId(id);
        response.setStatus(status);
        response.setText(text);
        response.setAudioUrl(audioUrl);
        response.setError(error);
        response.setWords(words);
        response.setUtterances(utterances);
        response.setConfidence(confidence);
        response.setAudioDuration(audioDuration);
        response.setPunctuate(punctuate);
        response.setFormatText(formatText);
        response.setDualChannel(dualChannel);
        response.setWebhookUrl(webhookUrl);
        response.setWebhookStatusCode(webhookStatusCode);
        response.setWebhookAuth(webhookAuth);
        response.setSpeedBoost(speedBoost);
        response.setAutoHighlightsResult(autoHighlightsResult);
        response.setAutoHighlights(autoHighlights);
        response.setAudioStartFrom(audioStartFrom);
        response.setAudioEndAt(audioEndAt);
        response.setWordBoost(wordBoost);
        response.setBoostParam(boostParam);
        response.setFilterProfanity(filterProfanity);
        response.setRedactPii(redactPii);
        response.setRedactPiiAudio(redactPiiAudio);
        response.setRedactPiiAudioQuality(redactPiiAudioQuality);
        response.setRedactPiiPolicies(redactPiiPolicies);
        response.setRedactPiiSub(redactPiiSub);
        response.setSpeakerLabels(speakerLabels);
        response.setContentSafety(contentSafety);
        response.setIabCategories(iabCategories);
        response.setLanguageCode(languageCode);
        response.setCustomSpelling(customSpelling);
        response.setDisfluencies(disfluencies);
        response.setSentimentAnalysis(sentimentAnalysis);
        response.setAutoChapters(autoChapters);
        response.setEntityDetection(entityDetection);
        response.setSpeechThreshold(speechThreshold);
        
        // Assert
        assertEquals(id, response.getId());
        assertEquals(status, response.getStatus());
        assertEquals(text, response.getText());
        assertEquals(audioUrl, response.getAudioUrl());
        assertEquals(error, response.getError());
        assertEquals(words, response.getWords());
        assertEquals(utterances, response.getUtterances());
        assertEquals(confidence, response.getConfidence());
        assertEquals(audioDuration, response.getAudioDuration());
        assertEquals(punctuate, response.getPunctuate());
        assertEquals(formatText, response.getFormatText());
        assertEquals(dualChannel, response.getDualChannel());
        assertEquals(webhookUrl, response.getWebhookUrl());
        assertEquals(webhookStatusCode, response.getWebhookStatusCode());
        assertEquals(webhookAuth, response.getWebhookAuth());
        assertEquals(speedBoost, response.getSpeedBoost());
        assertEquals(autoHighlightsResult, response.getAutoHighlightsResult());
        assertEquals(autoHighlights, response.getAutoHighlights());
        assertEquals(audioStartFrom, response.getAudioStartFrom());
        assertEquals(audioEndAt, response.getAudioEndAt());
        assertEquals(wordBoost, response.getWordBoost());
        assertEquals(boostParam, response.getBoostParam());
        assertEquals(filterProfanity, response.getFilterProfanity());
        assertEquals(redactPii, response.getRedactPii());
        assertEquals(redactPiiAudio, response.getRedactPiiAudio());
        assertEquals(redactPiiAudioQuality, response.getRedactPiiAudioQuality());
        assertEquals(redactPiiPolicies, response.getRedactPiiPolicies());
        assertEquals(redactPiiSub, response.getRedactPiiSub());
        assertEquals(speakerLabels, response.getSpeakerLabels());
        assertEquals(contentSafety, response.getContentSafety());
        assertEquals(iabCategories, response.getIabCategories());
        assertEquals(languageCode, response.getLanguageCode());
        assertEquals(customSpelling, response.getCustomSpelling());
        assertEquals(disfluencies, response.getDisfluencies());
        assertEquals(sentimentAnalysis, response.getSentimentAnalysis());
        assertEquals(autoChapters, response.getAutoChapters());
        assertEquals(entityDetection, response.getEntityDetection());
        assertEquals(speechThreshold, response.getSpeechThreshold());
    }
    
    @Test
    void isCompleted_WithCompletedStatus_ReturnsTrue() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("completed");
        
        // Act & Assert
        assertTrue(response.isCompleted());
    }
    
    @Test
    void isCompleted_WithOtherStatus_ReturnsFalse() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("processing");
        
        // Act & Assert
        assertFalse(response.isCompleted());
    }
    
    @Test
    void isProcessing_WithProcessingStatus_ReturnsTrue() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("processing");
        
        // Act & Assert
        assertTrue(response.isProcessing());
    }
    
    @Test
    void isProcessing_WithQueuedStatus_ReturnsTrue() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("queued");
        
        // Act & Assert
        assertTrue(response.isProcessing());
    }
    
    @Test
    void isProcessing_WithOtherStatus_ReturnsFalse() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("completed");
        
        // Act & Assert
        assertFalse(response.isProcessing());
    }
    
    @Test
    void hasFailed_WithErrorStatus_ReturnsTrue() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("error");
        response.setError("Something went wrong");
        
        // Act & Assert
        assertTrue(response.hasFailed());
    }
    
    @Test
    void hasFailed_WithErrorStatusButNoErrorMessage_ReturnsFalse() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("error");
        response.setError(null);
        
        // Act & Assert
        assertFalse(response.hasFailed());
    }
    
    @Test
    void hasFailed_WithOtherStatus_ReturnsFalse() {
        // Arrange
        TranscriptionResponse response = new TranscriptionResponse();
        response.setStatus("completed");
        
        // Act & Assert
        assertFalse(response.hasFailed());
    }
    
    @Test
    void word_SettersAndGetters_WorkCorrectly() {
        // Arrange
        TranscriptionResponse.Word word = new TranscriptionResponse.Word();
        String text = "test";
        Double start = 1.0;
        Double end = 2.0;
        Double confidence = 0.9;
        String speaker = "speaker_1";
        
        // Act
        word.setText(text);
        word.setStart(start);
        word.setEnd(end);
        word.setConfidence(confidence);
        word.setSpeaker(speaker);
        
        // Assert
        assertEquals(text, word.getText());
        assertEquals(start, word.getStart());
        assertEquals(end, word.getEnd());
        assertEquals(confidence, word.getConfidence());
        assertEquals(speaker, word.getSpeaker());
    }
    
    @Test
    void utterance_SettersAndGetters_WorkCorrectly() {
        // Arrange
        TranscriptionResponse.Utterance utterance = new TranscriptionResponse.Utterance();
        String text = "This is a test utterance.";
        Double start = 1.0;
        Double end = 5.0;
        Double confidence = 0.95;
        String speaker = "speaker_1";
        List<TranscriptionResponse.Word> words = new ArrayList<>();
        
        // Act
        utterance.setText(text);
        utterance.setStart(start);
        utterance.setEnd(end);
        utterance.setConfidence(confidence);
        utterance.setSpeaker(speaker);
        utterance.setWords(words);
        
        // Assert
        assertEquals(text, utterance.getText());
        assertEquals(start, utterance.getStart());
        assertEquals(end, utterance.getEnd());
        assertEquals(confidence, utterance.getConfidence());
        assertEquals(speaker, utterance.getSpeaker());
        assertEquals(words, utterance.getWords());
    }
}