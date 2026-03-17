package org.example.unicare_backend.repositories;

import org.example.unicare_backend.models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByVnpTxnRef(String vnpTxnRef);
}
