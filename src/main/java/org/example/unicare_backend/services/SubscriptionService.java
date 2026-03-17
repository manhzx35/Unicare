package org.example.unicare_backend.services;

import org.example.unicare_backend.models.Subscription;
import org.example.unicare_backend.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Optional<Subscription> getActiveSubscription(String userId) {
        return subscriptionRepository.findByUserIdAndActive(userId, true);
    }

    @Transactional
    public boolean cancelSubscription(String userId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUserIdAndActive(userId, true);
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.setActive(false);
            subscription.setEndDate(LocalDateTime.now());
            subscriptionRepository.save(subscription);
            return true;
        }
        return false;
    }
}
