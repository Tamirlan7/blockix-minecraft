package com.blockix.backend.controller;

import com.blockix.backend.model.Article;
import com.blockix.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/viewed-articles")
    public ResponseEntity<List<Article>> getUserViewedArticles(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAllViewedArticles(authentication));
    }

}
