package org.example.unicare_backend.services;

import org.example.unicare_backend.models.MoodHistory;
import org.example.unicare_backend.models.TestResult;
import org.example.unicare_backend.repositories.MoodHistoryRepository;
import org.example.unicare_backend.repositories.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private MoodHistoryRepository moodHistoryRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    public List<String> getRecommendations(String userId) {
        List<MoodHistory> moods = moodHistoryRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
        MoodHistory latestMood = moods.isEmpty() ? null : moods.get(0);

        List<TestResult> tests = testResultRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
        TestResult latestTest = tests.isEmpty() ? null : tests.get(0);

        boolean needsBreathingExercise = false;

        if (latestMood != null && latestMood.getScore() <= 2) {
            needsBreathingExercise = true;
        }

        if (latestTest != null && "SEVERE".equalsIgnoreCase(latestTest.getRiskLevel())) {
            needsBreathingExercise = true;
        }

        if (needsBreathingExercise) {
            return List.of(
                    "Box Breathing (4-4-4-4)",
                    "Deep Diaphragmatic Breathing",
                    "4-7-8 Relaxing Breath"
            );
        } else {
            return List.of(
                    "Nature Sounds - Rain",
                    "Binaural Beats for Focus",
                    "Calm Piano Audio"
            );
        }
    }
}
