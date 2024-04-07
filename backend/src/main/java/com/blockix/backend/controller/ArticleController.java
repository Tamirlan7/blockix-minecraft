package com.blockix.backend.controller;

import com.blockix.backend.dto.CreateArticleRequest;
import com.blockix.backend.dto.CreateArticleResponse;
import com.blockix.backend.dto.GetArticlesArgs;
import com.blockix.backend.dto.GetArticlesResponse;
import com.blockix.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('MODER')")
    public ResponseEntity<CreateArticleResponse> createArticle(CreateArticleRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(body));
    }

    @GetMapping()
    public ResponseEntity<Page<GetArticlesResponse>> getAllArticles(
            @RequestParam(name = "title", required = false, defaultValue = "") String title,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getAllArticles(
                        GetArticlesArgs.builder()
                                .title(title)
                                .page(page)
                                .size(size)
                                .build()
                ));
    }

}
