package org.example.unicare_backend.controllers;

import jakarta.validation.Valid;
import org.example.unicare_backend.payload.request.ExerciseCompletionRequest;
import org.example.unicare_backend.payload.response.MessageResponse;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/first-aid")
public class FirstAidController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<String>> getRecommendations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(401).build();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> recommendations = recommendationService.getRecommendations(userDetails.getId());
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/complete")
    public ResponseEntity<MessageResponse> completeExercise(@Valid @RequestBody ExerciseCompletionRequest request) {
        
        // (Gamification / XP feature removed for MVP)
        // We solely check completion percentage and log a generic success payload
        if (request.getCompletionPercentage() == 100) {
            return ResponseEntity.ok(new MessageResponse("Exercise completed successfully"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Exercise progress saved: " + request.getCompletionPercentage() + "%"));
        }
    }
}
