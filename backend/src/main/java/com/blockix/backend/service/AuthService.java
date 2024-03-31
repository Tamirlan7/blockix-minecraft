package com.blockix.backend.service;

import com.blockix.backend.dto.RegisterRequest;
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
}
