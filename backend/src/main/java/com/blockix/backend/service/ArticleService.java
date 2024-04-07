package com.blockix.backend.service;

import com.blockix.backend.dto.CreateArticleRequest;
import com.blockix.backend.dto.CreateArticleResponse;
import com.blockix.backend.dto.GetArticlesArgs;
import com.blockix.backend.dto.GetArticlesResponse;
import com.blockix.backend.exception.CustomInternalServerException;
import com.blockix.backend.model.Article;
import com.blockix.backend.model.ArticleMediaFile;
import com.blockix.backend.repository.ArticleMediaFileRepository;
import com.blockix.backend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMediaFileRepository articleMediaFileRepository;

    @Transactional
    public CreateArticleResponse createArticle(CreateArticleRequest body) {
        Article article = articleRepository.save(
            new Article().toBuilder()
                    .title(body.title())
                    .body(body.body())
                    .build()
        );

        Path p = Path.of("files", "articles", article.getId().toString());

        if (!Files.exists(p)) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                throw new CustomInternalServerException("Unable to create directory");
            }
        }

        try {
            Files.copy(body.file().getInputStream(), p.resolve(Objects.requireNonNull(body.file().getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
            ArticleMediaFile mediaFile = articleMediaFileRepository.save(
                    new ArticleMediaFile().toBuilder()
                            .type(body.file().getContentType())
                            .path(p.resolve(Objects.requireNonNull(body.file().getOriginalFilename())).toString())
                            .article(article)
                            .name(body.file().getOriginalFilename())
                            .build()
            );

            article.setMediaFiles(new LinkedList<>(Collections.singletonList(mediaFile)));
            articleRepository.save(article);
        } catch (IOException e) {
            throw new CustomInternalServerException("Unable to copy file");
        }

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


    public Page<GetArticlesResponse> getAllArticles(GetArticlesArgs args) {
        return articleRepository
                .findByTitleContaining(args.getTitle(), PageRequest.of(args.getPage(), args.getSize(), Sort.by("id")))
                .map(article -> GetArticlesResponse.builder()
                        .title(article.getTitle())
                        .body(article.getBody())
                        .createdAt(article.getCreatedAt())
                        .updatedAt(article.getUpdatedAt())
                        .id(article.getId())
                        .views(article.getViews().size())
                        .likes(article.getUserLikes().size())
                        .comments(article.getMessages())
                        .sharedCount(article.getSharedCount())
                        .build());
    }
}
