package org.example.unicare_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "subscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    @Id
    private String id;
    private String userId;
    private String planName; // BASIC, PREMIUM, VIP
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
}
