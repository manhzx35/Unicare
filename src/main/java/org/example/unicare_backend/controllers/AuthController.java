package org.example.unicare_backend.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.example.unicare_backend.models.User;
import org.example.unicare_backend.payload.request.LoginRequest;
import org.example.unicare_backend.payload.request.RegisterRequest;
import org.example.unicare_backend.payload.response.JwtResponse;
import org.example.unicare_backend.payload.response.MessageResponse;
import org.example.unicare_backend.repositories.UserRepository;
import org.example.unicare_backend.security.jwt.JwtUtils;
import org.example.unicare_backend.security.services.UserDetailsImpl;
import org.example.unicare_backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getDisplayName(),
                user.getRoleEnum().name(),
                user.getIsAnonymous()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        // Business Rule BR-01: Removed .edu.vn restriction

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        try {
            // Create new user's account
            User user = new User(signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getDisplayName());

            String otp = generateOtp();
            user.setVerificationCode(otp);
            user.setVerificationExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));
            user.setEnabled(false);

            userRepository.save(user);
            logger.info("User registered successfully, sending OTP to {}", user.getEmail());
            
            try {
                emailService.sendOtpEmail(user.getEmail(), otp);
            } catch (Exception e) {
                logger.error("Failed to send OTP email to {}: {}", user.getEmail(), e.getMessage());
                // We still registered the user, maybe return a different message?
                return ResponseEntity.ok(new MessageResponse("Chào mừng bạn! Tuy nhiên, việc gửi mã xác thực gặp sự cố. Vui lòng bấm 'Gửi lại mã'."));
            }

            return ResponseEntity.ok(new MessageResponse("Chào mừng bạn! Vui lòng kiểm tra email để lấy mã xác thực."));
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new MessageResponse("Lỗi hệ thống trong quá trình đăng ký: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: Không tìm thấy người dùng."));
        }

        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã được xác thực trước đó."));
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mã xác thực không chính xác."));
        }

        if (user.getVerificationExpiry().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mã xác thực đã hết hạn."));
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Xác thực thành công! Bây giờ bạn có thể đăng nhập."));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: Không tìm thấy người dùng."));
        }

        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã được xác thực."));
        }

        String otp = generateOtp();
        user.setVerificationCode(otp);
        user.setVerificationExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(new MessageResponse("Mã xác thực mới đã được gửi đến email của bạn."));
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
