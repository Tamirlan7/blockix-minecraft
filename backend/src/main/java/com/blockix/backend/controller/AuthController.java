package com.blockix.backend.controller;

import com.blockix.backend.dto.RegisterRequest;
import com.blockix.backend.model.Tokens;
import com.blockix.backend.service.AuthService;
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
    public ResponseEntity<Tokens> register(@RequestBody RegisterRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(body));
    }

}
