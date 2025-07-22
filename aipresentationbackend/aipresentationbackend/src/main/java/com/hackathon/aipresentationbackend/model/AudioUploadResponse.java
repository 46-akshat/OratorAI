package com.hackathon.aipresentationbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for AssemblyAI audio upload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioUploadResponse {
    
    @JsonProperty("upload_url")
    private String uploadUrl;
    
    // Default constructor
    public AudioUploadResponse() {
    }
    
    // Constructor with fields
    public AudioUploadResponse(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
    
    // Getters and setters
    public String getUploadUrl() {
        return uploadUrl;
    }
    
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}