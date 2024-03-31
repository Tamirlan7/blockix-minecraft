package com.blockix.backend.controller;

import com.blockix.backend.dto.CreateNewsRequest;
import com.blockix.backend.dto.CreateNewsResponse;
import com.blockix.backend.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsResponse> createNews(@RequestBody @Valid CreateNewsRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsService.createNews(body));
    }

}
