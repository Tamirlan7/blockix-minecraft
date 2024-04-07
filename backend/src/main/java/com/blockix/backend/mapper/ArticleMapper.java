package com.blockix.backend.mapper;

import com.blockix.backend.dto.*;
import com.blockix.backend.model.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public GetArticleResponse mapToGetArticleResponse(Article article) {
        return GetArticleResponse.builder()
                .title(article.getTitle())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .id(article.getId())
                .views(article.getViews().size())
                .likes(article.getUserLikes().size())
                .comments(article.getMessages())
                .sharedCount(article.getSharedCount())
                .build();
    }

    public CreateArticleResponse mapToCreateArticleResponse(Article article) {
        return CreateArticleResponse.builder()
                .title(article.getTitle())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .id(article.getId())
                .views(article.getViews().size())
                .likes(article.getUserLikes().size())
                .comments(article.getMessages())
                .sharedCount(article.getSharedCount())
                .build();
    }

    public DeleteArticleResponse mapToDeleteArticleResponse(Long articleId) {
        return DeleteArticleResponse.builder()
                .deletedSuccessfully(true)
                .articleId(articleId)
                .build();
    }

    public UpdateArticleResponse mapToUpdateArticleResponse(Article article) {
        return UpdateArticleResponse.builder()
                .title(article.getTitle())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .id(article.getId())
                .views(article.getViews().size())
                .likes(article.getUserLikes().size())
                .comments(article.getMessages())
                .sharedCount(article.getSharedCount())
                .build();
    }
}
