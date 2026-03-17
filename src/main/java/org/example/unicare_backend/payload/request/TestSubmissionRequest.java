package org.example.unicare_backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class TestSubmissionRequest {
    @NotBlank
    private String testType; // E.g., "PHQ-9", "GAD-7"

    @NotEmpty
    private List<Integer> answers; // Array of answers/scores

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
}
