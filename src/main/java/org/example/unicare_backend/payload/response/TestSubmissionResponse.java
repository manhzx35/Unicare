package org.example.unicare_backend.payload.response;

public class TestSubmissionResponse {
    private int totalScore;
    private String riskLevel;
    private boolean isSosTriggered;

    public TestSubmissionResponse(int totalScore, String riskLevel, boolean isSosTriggered) {
        this.totalScore = totalScore;
        this.riskLevel = riskLevel;
        this.isSosTriggered = isSosTriggered;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public boolean isSosTriggered() {
        return isSosTriggered;
    }

    public void setSosTriggered(boolean sosTriggered) {
        isSosTriggered = sosTriggered;
    }
}
