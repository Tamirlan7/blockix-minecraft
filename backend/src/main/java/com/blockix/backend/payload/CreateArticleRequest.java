package com.blockix.backend.payload;

import org.springframework.web.multipart.MultipartFile;


public record CreateArticleRequest(
        MultipartFile file,
        String title,
        String body
) {

}
