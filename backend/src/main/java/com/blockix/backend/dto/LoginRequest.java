package com.blockix.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email
        String email,
        String username,
        @Size(min = 8)
        @NotBlank
        String password
) {
}
