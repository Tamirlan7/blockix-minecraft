package com.blockix.backend.controller;

import com.blockix.backend.payload.LoginRequest;
import com.blockix.backend.payload.RefreshRequest;
import com.blockix.backend.payload.RegisterRequest;
import com.blockix.backend.model.Tokens;
import com.blockix.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Tokens> register(@RequestBody @Valid RegisterRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(body));
    }

    @PostMapping("/login")
    public ResponseEntity<Tokens> login(@RequestBody @Valid LoginRequest body) {
        return ResponseEntity.ok(authService.login(body));
    }
    @PostMapping("/refresh")
    public ResponseEntity<Tokens> refresh(@RequestBody @Valid RefreshRequest body) {
        return ResponseEntity.ok(authService.refresh(body));
    }

}
