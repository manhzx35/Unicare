package org.example.unicare_backend.repositories;

import org.example.unicare_backend.models.MoodHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodHistoryRepository extends MongoRepository<MoodHistory, String> {
    List<MoodHistory> findByUserId(String userId, Sort sort);
}
