package com.blockix.backend.payload;

public record ViewArticleRequest(
        boolean anonymous,
        long userId
) {
}
