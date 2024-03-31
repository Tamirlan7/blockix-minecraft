package com.blockix.backend.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Token {
    private String id;
    private Instant issuedAt;
    private Instant expiration;
    private TokenType tokenType;
    private List<String> roles;
    private Long userId;
}
