package org.example.unicare_backend.services;

import org.example.unicare_backend.models.MoodHistory;
import org.example.unicare_backend.payload.request.MoodRequest;
import org.example.unicare_backend.repositories.MoodHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {
    @Autowired
    private MoodHistoryRepository moodHistoryRepository;

    public MoodHistory checkInMood(String userId, MoodRequest request) {
        MoodHistory moodHistory = new MoodHistory(
                userId,
                request.getScore(),
                request.getTags(),
                request.getNote()
        );
        return moodHistoryRepository.save(moodHistory);
    }

    public List<MoodHistory> getMoodHistory(String userId) {
        return moodHistoryRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
