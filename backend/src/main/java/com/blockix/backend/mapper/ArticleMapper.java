package com.blockix.backend.mapper;

import com.blockix.backend.dto.ArticleDto;
import com.blockix.backend.model.Article;
import com.blockix.backend.payload.CreateArticleResponse;
import com.blockix.backend.payload.DeleteArticleResponse;
import com.blockix.backend.payload.GetArticleResponse;
import com.blockix.backend.payload.UpdateArticleResponse;
import org.springframework.stereotype.Component;

public class ArticleMapper {

    public static ArticleDto mapToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .views(article.getViews().size())
                .likes(article.getUserLikes().size())
                .sharedCount(article.getSharedCount())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .mediaFiles(article.getMediaFiles())
                .comments(article.getMessages().stream().map(ArticleMessageMapper::mapToDto).toList())
                .build();
    }

}
