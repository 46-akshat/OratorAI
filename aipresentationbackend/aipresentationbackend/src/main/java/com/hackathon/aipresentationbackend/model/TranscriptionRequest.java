package com.hackathon.aipresentationbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Request model for AssemblyAI transcription
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptionRequest {
    
    @NotBlank(message = "Audio URL is required")
    @JsonProperty("audio_url")
    private String audioUrl;
    
    @JsonProperty("language_code")
    private String languageCode;
    
    @JsonProperty("punctuate")
    private Boolean punctuate;
    
    @JsonProperty("format_text")
    private Boolean formatText;
    
    @JsonProperty("dual_channel")
    private Boolean dualChannel;
    
    @JsonProperty("webhook_url")
    private String webhookUrl;
    
    @JsonProperty("speaker_labels")
    private Boolean speakerLabels;
    
    @JsonProperty("word_boost")
    private String[] wordBoost;
    
    @JsonProperty("boost_param")
    private String boostParam;
    
    @JsonProperty("filter_profanity")
    private Boolean filterProfanity;
    
    @JsonProperty("redact_pii")
    private Boolean redactPii;
    
    @JsonProperty("redact_pii_policies")
    private String[] redactPiiPolicies;
    
    @JsonProperty("custom_spelling")
    private Map<String, String> customSpelling;
    
    @JsonProperty("disfluencies")
    private Boolean disfluencies;

    
    // Default constructor
    public TranscriptionRequest() {
    }
    
    // Constructor with required fields
    public TranscriptionRequest(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    // Getters and setters
    public String getAudioUrl() {
        return audioUrl;
    }
    
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    public Boolean getPunctuate() {
        return punctuate;
    }
    
    public void setPunctuate(Boolean punctuate) {
        this.punctuate = punctuate;
    }
    
    public Boolean getFormatText() {
        return formatText;
    }
    
    public void setFormatText(Boolean formatText) {
        this.formatText = formatText;
    }
    
    public Boolean getDualChannel() {
        return dualChannel;
    }
    
    public void setDualChannel(Boolean dualChannel) {
        this.dualChannel = dualChannel;
    }
    
    public String getWebhookUrl() {
        return webhookUrl;
    }
    
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
    
    public Boolean getSpeakerLabels() {
        return speakerLabels;
    }
    
    public void setSpeakerLabels(Boolean speakerLabels) {
        this.speakerLabels = speakerLabels;
    }
    
    public String[] getWordBoost() {
        return wordBoost;
    }
    
    public void setWordBoost(String[] wordBoost) {
        this.wordBoost = wordBoost;
    }
    
    public String getBoostParam() {
        return boostParam;
    }
    
    public void setBoostParam(String boostParam) {
        this.boostParam = boostParam;
    }
    
    public Boolean getFilterProfanity() {
        return filterProfanity;
    }
    
    public void setFilterProfanity(Boolean filterProfanity) {
        this.filterProfanity = filterProfanity;
    }
    
    public Boolean getRedactPii() {
        return redactPii;
    }
    
    public void setRedactPii(Boolean redactPii) {
        this.redactPii = redactPii;
    }
    
    public String[] getRedactPiiPolicies() {
        return redactPiiPolicies;
    }
    
    public void setRedactPiiPolicies(String[] redactPiiPolicies) {
        this.redactPiiPolicies = redactPiiPolicies;
    }
    
    public Map<String, String> getCustomSpelling() {
        return customSpelling;
    }
    
    public void setCustomSpelling(Map<String, String> customSpelling) {
        this.customSpelling = customSpelling;
    }
    
    public Boolean getDisfluencies() {
        return disfluencies;
    }
    
    public void setDisfluencies(Boolean disfluencies) {
        this.disfluencies = disfluencies;
    }
    
    /**
     * Builder for TranscriptionRequest
     */
    public static class Builder {
        private String audioUrl;
        private String languageCode = "en_us";
        private Boolean punctuate = true;
        private Boolean formatText = true;
        private Boolean dualChannel = false;
        private String webhookUrl;
        private Boolean speakerLabels = false;
        private String[] wordBoost;
        private String boostParam;
        private Boolean filterProfanity = false;
        private Boolean redactPii = false;
        private String[] redactPiiPolicies;
        private Map<String, String> customSpelling;
        private Boolean disfluencies = true;
        
        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }
        
        public Builder languageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }
        
        public Builder punctuate(Boolean punctuate) {
            this.punctuate = punctuate;
            return this;
        }
        
        public Builder formatText(Boolean formatText) {
            this.formatText = formatText;
            return this;
        }
        
        public Builder dualChannel(Boolean dualChannel) {
            this.dualChannel = dualChannel;
            return this;
        }
        
        public Builder webhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }
        
        public Builder speakerLabels(Boolean speakerLabels) {
            this.speakerLabels = speakerLabels;
            return this;
        }
        
        public Builder wordBoost(String[] wordBoost) {
            this.wordBoost = wordBoost;
            return this;
        }
        
        public Builder boostParam(String boostParam) {
            this.boostParam = boostParam;
            return this;
        }
        
        public Builder filterProfanity(Boolean filterProfanity) {
            this.filterProfanity = filterProfanity;
            return this;
        }
        
        public Builder redactPii(Boolean redactPii) {
            this.redactPii = redactPii;
            return this;
        }
        
        public Builder redactPiiPolicies(String[] redactPiiPolicies) {
            this.redactPiiPolicies = redactPiiPolicies;
            return this;
        }
        
        public Builder customSpelling(Map<String, String> customSpelling) {
            this.customSpelling = customSpelling;
            return this;
        }
        
        public Builder disfluencies(Boolean disfluencies) {
            this.disfluencies = disfluencies;
            return this;
        }
        
        public TranscriptionRequest build() {
            TranscriptionRequest request = new TranscriptionRequest();
            request.audioUrl = this.audioUrl;
            request.languageCode = this.languageCode;
            request.punctuate = this.punctuate;
            request.formatText = this.formatText;
            request.dualChannel = this.dualChannel;
            request.webhookUrl = this.webhookUrl;
            request.speakerLabels = this.speakerLabels;
            request.wordBoost = this.wordBoost;
            request.boostParam = this.boostParam;
            request.filterProfanity = this.filterProfanity;
            request.redactPii = this.redactPii;
            request.redactPiiPolicies = this.redactPiiPolicies;
            request.customSpelling = this.customSpelling;
            request.disfluencies = this.disfluencies;
            return request;
        }
    }
}