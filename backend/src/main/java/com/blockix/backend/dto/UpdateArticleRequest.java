package com.blockix.backend.dto;

public record UpdateArticleRequest(
        String title,
        String body
) {
}
