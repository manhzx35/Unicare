package org.example.unicare_backend.repositories;

import org.example.unicare_backend.models.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Optional<Subscription> findByUserIdAndActive(String userId, boolean active);
}
