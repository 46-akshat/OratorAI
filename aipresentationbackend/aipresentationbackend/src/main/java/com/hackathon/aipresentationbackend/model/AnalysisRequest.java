package com.hackathon.aipresentationbackend.model;




public class AnalysisRequest {
    private String originalscript;
    private  String spokenscript;

    public AnalysisRequest(String originalscript, String spokenscript) {
        this.originalscript = originalscript;
        this.spokenscript = spokenscript;
    }

    public String getOriginalscript() {
        return originalscript;
    }

    public void setOriginalscript(String originalscript) {
        this.originalscript = originalscript;
    }

    public String getSpokenscript() {
        return spokenscript;
    }

    public void setSpokenscript(String spokenscript) {
        this.spokenscript = spokenscript;
    }
}
