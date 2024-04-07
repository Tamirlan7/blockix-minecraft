package com.blockix.backend.dto;

public record ViewArticleRequest(
        boolean anonymous,
        long userId
) {
}
