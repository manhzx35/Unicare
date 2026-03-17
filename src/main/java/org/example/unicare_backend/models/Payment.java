package org.example.unicare_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    private String id;
    private String userId;
    private long amount;
    private String orderInfo;
    private String vnpTxnRef;
    private String vnpTransactionNo;
    private String vnpResponseCode;
    private String vnpTransactionStatus;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
