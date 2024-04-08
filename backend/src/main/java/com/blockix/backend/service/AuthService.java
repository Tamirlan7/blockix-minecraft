package com.blockix.backend.service;

import com.blockix.backend.payload.LoginRequest;
import com.blockix.backend.payload.RefreshRequest;
import com.blockix.backend.payload.RegisterRequest;
import com.blockix.backend.exception.CustomBadRequestException;
import com.blockix.backend.exception.CustomNotFoundException;
import com.blockix.backend.jwt.model.Token;
import com.blockix.backend.jwt.model.TokenType;
import com.blockix.backend.jwt.service.JwtService;
import com.blockix.backend.model.Role;
import com.blockix.backend.model.Tokens;
import com.blockix.backend.model.User;
import com.blockix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Tokens register(RegisterRequest body) {
        userRepository.findByUsername(body.username())
                .ifPresent((u) -> {
                    throw new CustomBadRequestException("User with username " + body.username() + " already exist.");
                });

        userRepository.findByEmail(body.email())
                .ifPresent((u) -> {
                    throw new CustomBadRequestException("User with email " + body.email() + " already exist.");
                });

        User user = userRepository.save(
                User.builder()
                        .username(body.username())
                        .password(passwordEncoder.encode(body.password()))
                        .email(body.email())
                        .role(Role.ROLE_USER)
                        .build()
        );

        return Tokens.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public Tokens login(LoginRequest body) {
        User user = null;

        if (body.username() != null && !body.username().isBlank()) {
            user = userRepository.findByUsername(body.username())
                    .orElseThrow(() -> new CustomNotFoundException("User with username " + body.username() + " not found."));
        }

        if (body.email() != null && !body.email().isBlank()) {
            user = userRepository.findByEmail(body.email())
                    .orElseThrow(() -> new CustomNotFoundException("User with email " + body.email() + " not found."));
        }

        if (user == null) {
            throw new CustomBadRequestException("User not found, the reason could be username or email are null");
        }

        return Tokens.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public Tokens refresh(RefreshRequest body) {
        Token token = jwtService.deserializeToken(body.refreshToken());

        if (token.getTokenType() == TokenType.ACCESS_TOKEN) {
            throw new CustomBadRequestException("accessToken has been passed, when refreshToken needed.");
        }

        if (jwtService.isValid(token)) {
            User user = userRepository.findById(token.getUserId())
                    .orElseThrow(() -> new CustomBadRequestException("User with id (in refreshToken) " + token.getUserId() + " not found."));

            return Tokens.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        } else {
            throw new CustomBadRequestException("RefreshToken is not valid (most probably expired)");
        }
    }
}
