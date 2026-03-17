package org.example.unicare_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document(collection = "test_results")
public class TestResult {
    @Id
    private String id;

    // nullable for Guests
    private String userId;

    private String testType; // e.g., "PHQ-9", "GAD-7"

    private int totalScore;

    private String riskLevel; // e.g., "MILD", "MODERATE", "SEVERE"

    @CreatedDate
    private Instant createdAt = Instant.now();

    public TestResult(String userId, String testType, int totalScore, String riskLevel) {
        this.userId = userId;
        this.testType = testType;
        this.totalScore = totalScore;
        this.riskLevel = riskLevel;
    }
}
