package com.hackathon.aipresentationbackend.model;




public class MurfRequest {
    private String text;
    private String voiceId;

    public MurfRequest(String text, String voiceId) {
        this.text = text;
        this.voiceId = voiceId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }
}
