package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TranscriptionRequestTest {

    @Test
    void constructor_WithAudioUrl_SetsAudioUrl() {
        // Arrange & Act
        String audioUrl = "https://example.com/audio.mp3";
        TranscriptionRequest request = new TranscriptionRequest(audioUrl);
        
        // Assert
        assertEquals(audioUrl, request.getAudioUrl());
    }
    
    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        TranscriptionRequest request = new TranscriptionRequest();
        String audioUrl = "https://example.com/audio.mp3";
        String languageCode = "en_us";
        Boolean punctuate = true;
        Boolean formatText = true;
        Boolean dualChannel = false;
        String webhookUrl = "https://example.com/webhook";
        Boolean speakerLabels = true;
        String[] wordBoost = {"example", "test"};
        String boostParam = "high";
        Boolean filterProfanity = true;
        Boolean redactPii = true;
        String[] redactPiiPolicies = {"pii_policy"};
        Map<String, String> customSpelling = new HashMap<>();
        customSpelling.put("word", "spelling");
        
        // Act
        request.setAudioUrl(audioUrl);
        request.setLanguageCode(languageCode);
        request.setPunctuate(punctuate);
        request.setFormatText(formatText);
        request.setDualChannel(dualChannel);
        request.setWebhookUrl(webhookUrl);
        request.setSpeakerLabels(speakerLabels);
        request.setWordBoost(wordBoost);
        request.setBoostParam(boostParam);
        request.setFilterProfanity(filterProfanity);
        request.setRedactPii(redactPii);
        request.setRedactPiiPolicies(redactPiiPolicies);
        request.setCustomSpelling(customSpelling);
        
        // Assert
        assertEquals(audioUrl, request.getAudioUrl());
        assertEquals(languageCode, request.getLanguageCode());
        assertEquals(punctuate, request.getPunctuate());
        assertEquals(formatText, request.getFormatText());
        assertEquals(dualChannel, request.getDualChannel());
        assertEquals(webhookUrl, request.getWebhookUrl());
        assertEquals(speakerLabels, request.getSpeakerLabels());
        assertArrayEquals(wordBoost, request.getWordBoost());
        assertEquals(boostParam, request.getBoostParam());
        assertEquals(filterProfanity, request.getFilterProfanity());
        assertEquals(redactPii, request.getRedactPii());
        assertArrayEquals(redactPiiPolicies, request.getRedactPiiPolicies());
        assertEquals(customSpelling, request.getCustomSpelling());
    }
    
    @Test
    void builder_CreatesCorrectObject() {
        // Arrange
        String audioUrl = "https://example.com/audio.mp3";
        String languageCode = "en_us";
        Boolean punctuate = true;
        Boolean formatText = true;
        Boolean dualChannel = false;
        String webhookUrl = "https://example.com/webhook";
        Boolean speakerLabels = true;
        String[] wordBoost = {"example", "test"};
        String boostParam = "high";
        Boolean filterProfanity = true;
        Boolean redactPii = true;
        String[] redactPiiPolicies = {"pii_policy"};
        Map<String, String> customSpelling = new HashMap<>();
        customSpelling.put("word", "spelling");
        
        // Act
        TranscriptionRequest request = new TranscriptionRequest.Builder()
                .audioUrl(audioUrl)
                .languageCode(languageCode)
                .punctuate(punctuate)
                .formatText(formatText)
                .dualChannel(dualChannel)
                .webhookUrl(webhookUrl)
                .speakerLabels(speakerLabels)
                .wordBoost(wordBoost)
                .boostParam(boostParam)
                .filterProfanity(filterProfanity)
                .redactPii(redactPii)
                .redactPiiPolicies(redactPiiPolicies)
                .customSpelling(customSpelling)
                .build();
        
        // Assert
        assertEquals(audioUrl, request.getAudioUrl());
        assertEquals(languageCode, request.getLanguageCode());
        assertEquals(punctuate, request.getPunctuate());
        assertEquals(formatText, request.getFormatText());
        assertEquals(dualChannel, request.getDualChannel());
        assertEquals(webhookUrl, request.getWebhookUrl());
        assertEquals(speakerLabels, request.getSpeakerLabels());
        assertArrayEquals(wordBoost, request.getWordBoost());
        assertEquals(boostParam, request.getBoostParam());
        assertEquals(filterProfanity, request.getFilterProfanity());
        assertEquals(redactPii, request.getRedactPii());
        assertArrayEquals(redactPiiPolicies, request.getRedactPiiPolicies());
        assertEquals(customSpelling, request.getCustomSpelling());
    }
    
    @Test
    void builder_WithDefaultValues_SetsDefaults() {
        // Arrange & Act
        String audioUrl = "https://example.com/audio.mp3";
        TranscriptionRequest request = new TranscriptionRequest.Builder()
                .audioUrl(audioUrl)
                .build();
        
        // Assert
        assertEquals(audioUrl, request.getAudioUrl());
        assertEquals("en_us", request.getLanguageCode());
        assertTrue(request.getPunctuate());
        assertTrue(request.getFormatText());
        assertFalse(request.getDualChannel());
        assertFalse(request.getSpeakerLabels());
        assertFalse(request.getFilterProfanity());
        assertFalse(request.getRedactPii());
    }
}