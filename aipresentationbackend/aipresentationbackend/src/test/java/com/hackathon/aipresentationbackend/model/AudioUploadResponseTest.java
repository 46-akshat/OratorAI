package com.hackathon.aipresentationbackend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AudioUploadResponseTest {

    @Test
    void defaultConstructor_CreatesEmptyObject() {
        // Arrange & Act
        AudioUploadResponse response = new AudioUploadResponse();
        
        // Assert
        assertNull(response.getUploadUrl());
    }
    
    @Test
    void constructor_WithUploadUrl_SetsUploadUrl() {
        // Arrange & Act
        String uploadUrl = "https://example.com/upload";
        AudioUploadResponse response = new AudioUploadResponse(uploadUrl);
        
        // Assert
        assertEquals(uploadUrl, response.getUploadUrl());
    }
    
    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        AudioUploadResponse response = new AudioUploadResponse();
        String uploadUrl = "https://example.com/upload";
        
        // Act
        response.setUploadUrl(uploadUrl);
        
        // Assert
        assertEquals(uploadUrl, response.getUploadUrl());
    }
}