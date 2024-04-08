package com.blockix.backend.payload;

public record UpdateArticleRequest(
        String title,
        String body
) {
}
