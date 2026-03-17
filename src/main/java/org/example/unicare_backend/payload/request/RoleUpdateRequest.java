package org.example.unicare_backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleUpdateRequest {
    @NotBlank
    private String role;
}
