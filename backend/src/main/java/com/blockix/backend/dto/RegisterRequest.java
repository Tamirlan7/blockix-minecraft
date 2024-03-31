package com.blockix.backend.dto;

public record RegisterRequest(
    String username,
    String password,
    String email
) {
}
