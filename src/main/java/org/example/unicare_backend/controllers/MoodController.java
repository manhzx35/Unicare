package org.example.unicare_backend.controllers;

import jakarta.validation.Valid;
import org.example.unicare_backend.models.MoodHistory;
import org.example.unicare_backend.payload.request.MoodRequest;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/mood")
public class MoodController {
    @Autowired
    private MoodService moodService;

    @PostMapping("/check-in")
    public ResponseEntity<MoodHistory> checkIn(@Valid @RequestBody MoodRequest moodRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(401).build();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        MoodHistory savedMood = moodService.checkInMood(userDetails.getId(), moodRequest);

        return ResponseEntity.ok(savedMood);
    }

    @GetMapping("/history")
    public ResponseEntity<List<MoodHistory>> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(401).build();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<MoodHistory> history = moodService.getMoodHistory(userDetails.getId());

        return ResponseEntity.ok(history);
    }
}
