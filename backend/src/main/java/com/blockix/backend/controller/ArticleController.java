package com.blockix.backend.controller;

import com.blockix.backend.dto.*;
import com.blockix.backend.service.ArticleComment;
import com.blockix.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MODER')")
    public ResponseEntity<CreateArticleResponse> createArticle(CreateArticleRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(body));
    }

    @GetMapping
    public ResponseEntity<Page<GetArticleResponse>> getAllArticles(
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

    @GetMapping("/{id}")
    public ResponseEntity<GetArticleResponse> getArticleById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.findArticleById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODER')")
    public ResponseEntity<DeleteArticleResponse> deleteArticleById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.deleteArticleById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODER')")
    public ResponseEntity<UpdateArticleResponse> updateArticleById(
        @PathVariable("id") Long id,
        @RequestBody UpdateArticleRequest body
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.updateArticleById(id, body));
    }

    @PutMapping("/{id}/share")
    public ResponseEntity<UpdateArticleResponse> shareArticle(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.shareArticleById(id));
    }

    @PutMapping("/{articleId}/like")
    public ResponseEntity<UpdateArticleResponse> likeArticle(
            @PathVariable("articleId") Long articleId,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.likeArticle(articleId, authentication));
    }

    @PutMapping("/{articleId}/view/anonymous")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UpdateArticleResponse> viewArticleAnonymously(@PathVariable("articleId") Long articleId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.viewArticleAnonymously(articleId));
    }

    @PutMapping("/{articleId}/view")
    public ResponseEntity<UpdateArticleResponse> viewArticle(
            @PathVariable("articleId") Long articleId,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.viewArticle(articleId, authentication));
    }

    @PostMapping("/{articleId}/comment")
    public ResponseEntity<UpdateArticleResponse> createArticleComment(
            @PathVariable("articleId") Long articleId,
            @RequestBody ArticleComment comment,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticleComment(articleId, comment, authentication));
    }
}
