package com.blockix.backend.dto;

import org.springframework.web.multipart.MultipartFile;


public record CreateArticleRequest(
        MultipartFile file,
        String title,
        String body
) {

}
