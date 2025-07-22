package com.hackathon.aipresentationbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response model for AssemblyAI transcription
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptionResponse {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("audio_url")
    private String audioUrl;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("words")
    private List<Word> words;
    
    @JsonProperty("utterances")
    private List<Utterance> utterances;
    
    @JsonProperty("confidence")
    private Double confidence;
    
    @JsonProperty("audio_duration")
    private Double audioDuration;
    
    @JsonProperty("punctuate")
    private Boolean punctuate;
    
    @JsonProperty("format_text")
    private Boolean formatText;
    
    @JsonProperty("dual_channel")
    private Boolean dualChannel;
    
    @JsonProperty("webhook_url")
    private String webhookUrl;
    
    @JsonProperty("webhook_status_code")
    private Integer webhookStatusCode;
    
    @JsonProperty("webhook_auth")
    private Boolean webhookAuth;
    
    @JsonProperty("speed_boost")
    private Boolean speedBoost;
    
    @JsonProperty("auto_highlights_result")
    private Object autoHighlightsResult;
    
    @JsonProperty("auto_highlights")
    private Boolean autoHighlights;
    
    @JsonProperty("audio_start_from")
    private Integer audioStartFrom;
    
    @JsonProperty("audio_end_at")
    private Integer audioEndAt;
    
    @JsonProperty("word_boost")
    private List<String> wordBoost;
    
    @JsonProperty("boost_param")
    private String boostParam;
    
    @JsonProperty("filter_profanity")
    private Boolean filterProfanity;
    
    @JsonProperty("redact_pii")
    private Boolean redactPii;
    
    @JsonProperty("redact_pii_audio")
    private Boolean redactPiiAudio;
    
    @JsonProperty("redact_pii_audio_quality")
    private String redactPiiAudioQuality;
    
    @JsonProperty("redact_pii_policies")
    private List<String> redactPiiPolicies;
    
    @JsonProperty("redact_pii_sub")
    private String redactPiiSub;
    
    @JsonProperty("speaker_labels")
    private Boolean speakerLabels;
    
    @JsonProperty("content_safety")
    private Boolean contentSafety;
    
    @JsonProperty("iab_categories")
    private Boolean iabCategories;
    
    @JsonProperty("language_code")
    private String languageCode;
    
    @JsonProperty("custom_spelling")
    private Object customSpelling;
    
    @JsonProperty("disfluencies")
    private Boolean disfluencies;
    
    @JsonProperty("sentiment_analysis")
    private Boolean sentimentAnalysis;
    
    @JsonProperty("auto_chapters")
    private Boolean autoChapters;
    
    @JsonProperty("entity_detection")
    private Boolean entityDetection;
    
    @JsonProperty("speech_threshold")
    private Double speechThreshold;
    
    // Nested classes for complex properties
    
    /**
     * Word model for transcription response
     */
    public static class Word {
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("start")
        private Double start;
        
        @JsonProperty("end")
        private Double end;
        
        @JsonProperty("confidence")
        private Double confidence;
        
        @JsonProperty("speaker")
        private String speaker;
        
        // Getters and setters
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public Double getStart() {
            return start;
        }
        
        public void setStart(Double start) {
            this.start = start;
        }
        
        public Double getEnd() {
            return end;
        }
        
        public void setEnd(Double end) {
            this.end = end;
        }
        
        public Double getConfidence() {
            return confidence;
        }
        
        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
        
        public String getSpeaker() {
            return speaker;
        }
        
        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }
    }
    
    /**
     * Utterance model for transcription response
     */
    public static class Utterance {
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("start")
        private Double start;
        
        @JsonProperty("end")
        private Double end;
        
        @JsonProperty("confidence")
        private Double confidence;
        
        @JsonProperty("speaker")
        private String speaker;
        
        @JsonProperty("words")
        private List<Word> words;
        
        // Getters and setters
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public Double getStart() {
            return start;
        }
        
        public void setStart(Double start) {
            this.start = start;
        }
        
        public Double getEnd() {
            return end;
        }
        
        public void setEnd(Double end) {
            this.end = end;
        }
        
        public Double getConfidence() {
            return confidence;
        }
        
        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
        
        public String getSpeaker() {
            return speaker;
        }
        
        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }
        
        public List<Word> getWords() {
            return words;
        }
        
        public void setWords(List<Word> words) {
            this.words = words;
        }
    }
    
    // Getters and setters for main class
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getAudioUrl() {
        return audioUrl;
    }
    
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public List<Word> getWords() {
        return words;
    }
    
    public void setWords(List<Word> words) {
        this.words = words;
    }
    
    public List<Utterance> getUtterances() {
        return utterances;
    }
    
    public void setUtterances(List<Utterance> utterances) {
        this.utterances = utterances;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public Double getAudioDuration() {
        return audioDuration;
    }
    
    public void setAudioDuration(Double audioDuration) {
        this.audioDuration = audioDuration;
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
    
    public Integer getWebhookStatusCode() {
        return webhookStatusCode;
    }
    
    public void setWebhookStatusCode(Integer webhookStatusCode) {
        this.webhookStatusCode = webhookStatusCode;
    }
    
    public Boolean getWebhookAuth() {
        return webhookAuth;
    }
    
    public void setWebhookAuth(Boolean webhookAuth) {
        this.webhookAuth = webhookAuth;
    }
    
    public Boolean getSpeedBoost() {
        return speedBoost;
    }
    
    public void setSpeedBoost(Boolean speedBoost) {
        this.speedBoost = speedBoost;
    }
    
    public Object getAutoHighlightsResult() {
        return autoHighlightsResult;
    }
    
    public void setAutoHighlightsResult(Object autoHighlightsResult) {
        this.autoHighlightsResult = autoHighlightsResult;
    }
    
    public Boolean getAutoHighlights() {
        return autoHighlights;
    }
    
    public void setAutoHighlights(Boolean autoHighlights) {
        this.autoHighlights = autoHighlights;
    }
    
    public Integer getAudioStartFrom() {
        return audioStartFrom;
    }
    
    public void setAudioStartFrom(Integer audioStartFrom) {
        this.audioStartFrom = audioStartFrom;
    }
    
    public Integer getAudioEndAt() {
        return audioEndAt;
    }
    
    public void setAudioEndAt(Integer audioEndAt) {
        this.audioEndAt = audioEndAt;
    }
    
    public List<String> getWordBoost() {
        return wordBoost;
    }
    
    public void setWordBoost(List<String> wordBoost) {
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
    
    public Boolean getRedactPiiAudio() {
        return redactPiiAudio;
    }
    
    public void setRedactPiiAudio(Boolean redactPiiAudio) {
        this.redactPiiAudio = redactPiiAudio;
    }
    
    public String getRedactPiiAudioQuality() {
        return redactPiiAudioQuality;
    }
    
    public void setRedactPiiAudioQuality(String redactPiiAudioQuality) {
        this.redactPiiAudioQuality = redactPiiAudioQuality;
    }
    
    public List<String> getRedactPiiPolicies() {
        return redactPiiPolicies;
    }
    
    public void setRedactPiiPolicies(List<String> redactPiiPolicies) {
        this.redactPiiPolicies = redactPiiPolicies;
    }
    
    public String getRedactPiiSub() {
        return redactPiiSub;
    }
    
    public void setRedactPiiSub(String redactPiiSub) {
        this.redactPiiSub = redactPiiSub;
    }
    
    public Boolean getSpeakerLabels() {
        return speakerLabels;
    }
    
    public void setSpeakerLabels(Boolean speakerLabels) {
        this.speakerLabels = speakerLabels;
    }
    
    public Boolean getContentSafety() {
        return contentSafety;
    }
    
    public void setContentSafety(Boolean contentSafety) {
        this.contentSafety = contentSafety;
    }
    
    public Boolean getIabCategories() {
        return iabCategories;
    }
    
    public void setIabCategories(Boolean iabCategories) {
        this.iabCategories = iabCategories;
    }
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    public Object getCustomSpelling() {
        return customSpelling;
    }
    
    public void setCustomSpelling(Object customSpelling) {
        this.customSpelling = customSpelling;
    }
    
    public Boolean getDisfluencies() {
        return disfluencies;
    }
    
    public void setDisfluencies(Boolean disfluencies) {
        this.disfluencies = disfluencies;
    }
    
    public Boolean getSentimentAnalysis() {
        return sentimentAnalysis;
    }
    
    public void setSentimentAnalysis(Boolean sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }
    
    public Boolean getAutoChapters() {
        return autoChapters;
    }
    
    public void setAutoChapters(Boolean autoChapters) {
        this.autoChapters = autoChapters;
    }
    
    public Boolean getEntityDetection() {
        return entityDetection;
    }
    
    public void setEntityDetection(Boolean entityDetection) {
        this.entityDetection = entityDetection;
    }
    
    public Double getSpeechThreshold() {
        return speechThreshold;
    }
    
    public void setSpeechThreshold(Double speechThreshold) {
        this.speechThreshold = speechThreshold;
    }
    
    /**
     * Check if the transcription is completed
     * 
     * @return true if the transcription is completed, false otherwise
     */
    public boolean isCompleted() {
        return "completed".equals(status);
    }
    
    /**
     * Check if the transcription is processing
     * 
     * @return true if the transcription is processing, false otherwise
     */
    public boolean isProcessing() {
        return "processing".equals(status) || "queued".equals(status);
    }
    
    /**
     * Check if the transcription has failed
     * 
     * @return true if the transcription has failed, false otherwise
     */
    public boolean hasFailed() {
        return "error".equals(status) && error != null;
    }
}