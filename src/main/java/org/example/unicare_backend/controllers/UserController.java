package org.example.unicare_backend.controllers;

import org.example.unicare_backend.models.User;
import org.example.unicare_backend.payload.response.MessageResponse;
import org.example.unicare_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.example.unicare_backend.models.ERole;
import org.example.unicare_backend.payload.request.RoleUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PutMapping("/anonymous")
    public ResponseEntity<?> toggleAnonymousStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Toggle the isAnonymous flag
        user.setIsAnonymous(!user.getIsAnonymous());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Anonymous status updated to: " + user.getIsAnonymous()));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(@PathVariable String id, @RequestBody RoleUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        try {
            ERole newRole = ERole.valueOf(request.getRole());
            user.setRole(newRole);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User role updated successfully to: " + newRole.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid role provided."));
        }
    }
}
