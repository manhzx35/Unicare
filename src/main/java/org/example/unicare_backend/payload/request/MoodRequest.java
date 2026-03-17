package org.example.unicare_backend.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

public class MoodRequest {
    @Min(1)
    @Max(5)
    private int score;

    private List<String> tags;

    private String note;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
