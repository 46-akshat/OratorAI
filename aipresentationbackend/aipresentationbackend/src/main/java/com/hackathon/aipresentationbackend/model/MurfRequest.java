package com.hackathon.aipresentationbackend.model;




public class MurfRequest {
    private String text;
    private String voiceId;
    private String tone;
    private Double speed;

    public MurfRequest() {}

    public MurfRequest(String text, String voiceId) {
        this.text = text;
        this.voiceId = voiceId;
        this.speed = 1.0;
    }

    public MurfRequest(String text, String voiceId, String tone) {
        this.text = text;
        this.voiceId = voiceId;
        this.tone = tone;
        this.speed = 1.0;
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

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
