package org.example.unicare_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "mood_histories")
public class MoodHistory {
    @Id
    private String id;

    private String userId;

    private int score; // 1 to 5

    private List<String> tags;

    private String note;

    @CreatedDate
    private Instant createdAt = Instant.now();

    public MoodHistory(String userId, int score, List<String> tags, String note) {
        this.userId = userId;
        this.score = score;
        this.tags = tags;
        this.note = note;
    }
}
