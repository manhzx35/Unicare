package org.example.unicare_backend.controllers;

import jakarta.validation.Valid;
import org.example.unicare_backend.models.TestResult;
import org.example.unicare_backend.payload.request.TestSubmissionRequest;
import org.example.unicare_backend.payload.response.TestSubmissionResponse;
import org.example.unicare_backend.repositories.TestResultRepository;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.TestScoreCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestScoreCalculatorService scoreCalculatorService;

    @Autowired
    private TestResultRepository testResultRepository;

    @PostMapping("/submit")
    public ResponseEntity<TestSubmissionResponse> submitTest(@Valid @RequestBody TestSubmissionRequest request) {
        
        // 1. Calculate Score and Status
        TestSubmissionResponse responseDto = scoreCalculatorService.calculateResult(request.getTestType(), request.getAnswers());

        // 2. Check if user is authenticated (can be a guest)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userId = userDetails.getId();
        }

        // 3. Save to MongoDB
        TestResult testResult = new TestResult(
                userId, // might be null for guests
                request.getTestType(),
                responseDto.getTotalScore(),
                responseDto.getRiskLevel()
        );
        testResultRepository.save(testResult);

        // 4. Return DTO with (sosTriggered) flag
        return ResponseEntity.ok(responseDto);
    }
}
