package org.example.unicare_backend.controllers;

import org.example.unicare_backend.models.Subscription;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSubscription() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        
        Optional<Subscription> subscription = subscriptionService.getActiveSubscription(userId);
        if (subscription.isPresent()) {
            return ResponseEntity.ok(subscription.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSubscription() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        
        boolean success = subscriptionService.cancelSubscription(userId);
        if (success) {
            return ResponseEntity.ok("Subscription cancelled successfully");
        } else {
            return ResponseEntity.status(400).body("No active subscription found to cancel");
        }
    }
}
