package com.blockix.backend.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String username,
        @Size(min = 8)
        @NotBlank
        String password
) {
}
