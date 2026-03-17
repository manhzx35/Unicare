package org.example.unicare_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;

    private String passwordHash;

    private String displayName;

    private Boolean isAnonymous = false;

    private String role = ERole.ROLE_STUDENT.name();
    
    private String verificationCode;
    private Instant verificationExpiry;
    private boolean enabled = false;

    public ERole getRoleEnum() {
        try {
            return ERole.valueOf(this.role);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ERole.ROLE_STUDENT;
        }
    }

    public void setRole(ERole roleEnum) {
        this.role = roleEnum.name();
    }

    @CreatedDate
    private Instant createdAt = Instant.now();

    public User(String email, String passwordHash, String displayName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
    }
}
