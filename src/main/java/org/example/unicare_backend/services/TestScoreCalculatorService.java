package org.example.unicare_backend.services;

import org.example.unicare_backend.payload.response.TestSubmissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestScoreCalculatorService {

    public TestSubmissionResponse calculateResult(String testType, List<Integer> answers) {
        int totalScore = answers.stream().mapToInt(Integer::intValue).sum();

        String riskLevel = determineRiskLevel(testType, totalScore);
        boolean isSosTriggered = "SEVERE".equalsIgnoreCase(riskLevel);

        return new TestSubmissionResponse(totalScore, riskLevel, isSosTriggered);
    }

    private String determineRiskLevel(String testType, int score) {

        if (score <= 9) {
            return "MILD";
        } else if (score <= 14) {
            return "MODERATE";
        } else {
            return "SEVERE";
        }
    }
}
