package com.blockix.backend.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Token {
    private String id;
    private Date issuedAt;
    private Date expiration;
    private TokenType tokenType;
    private List<String> roles;
    private Long userId;
}
