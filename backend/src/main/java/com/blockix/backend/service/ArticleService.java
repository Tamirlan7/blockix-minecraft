package com.blockix.backend.service;

import com.blockix.backend.dto.*;
import com.blockix.backend.exception.CustomBadRequestException;
import com.blockix.backend.exception.CustomInternalServerException;
import com.blockix.backend.exception.CustomNotFoundException;
import com.blockix.backend.mapper.ArticleMapper;
import com.blockix.backend.model.*;
import com.blockix.backend.repository.ArticleMediaFileRepository;
import com.blockix.backend.repository.ArticleRepository;
import com.blockix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final ArticleMediaFileRepository articleMediaFileRepository;
    private final UserRepository userRepository;

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
            article = articleRepository.save(article);
        } catch (IOException e) {
            throw new CustomInternalServerException("Unable to copy file");
        }

        return articleMapper.mapToCreateArticleResponse(article);
    }


    public Page<GetArticleResponse> getAllArticles(GetArticlesArgs args) {
        return articleRepository
                .findByTitleContaining(args.getTitle(), PageRequest.of(args.getPage(), args.getSize(), Sort.by("id")))
                .map(articleMapper::mapToGetArticleResponse);
    }

    public GetArticleResponse findArticleById(Long id) {
        Article article = this.getArticleById(id);
        return articleMapper.mapToGetArticleResponse(article);
    }

    public DeleteArticleResponse deleteArticleById(Long id) {
        articleRepository.deleteById(id);
        return articleMapper.mapToDeleteArticleResponse(id);
    }

    public UpdateArticleResponse updateArticleById(Long id, UpdateArticleRequest body) {
        Article article = this.getArticleById(id);

        if (body.title() != null) {
            article.setTitle(body.title());
        }

        if (body.body() != null) {
            article.setBody(body.body());
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.mapToUpdateArticleResponse(updatedArticle);
    }

    public UpdateArticleResponse shareArticleById(Long id) {
        Article article = this.getArticleById(id);

        article.setSharedCount(article.getSharedCount() + 1);
        Article updatedArticle = articleRepository.save(article);

        return articleMapper.mapToUpdateArticleResponse(updatedArticle);
    }

    public UpdateArticleResponse likeArticleWithUserId(Long articleId, Long userId) {
        Article article = this.getArticleById(articleId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        // Проверяем, поставил ли пользователь уже лайк для этой статьи
        if (article.getUserLikes().stream().anyMatch(u -> u.getId().equals(userId))) {
            throw new CustomBadRequestException("User already liked this article");
        }

        // Добавляем лайк пользователя к статье
        article.getUserLikes().add(user);
        article = articleRepository.save(article);

        // Возвращаем обновленную информацию о статье
        return articleMapper.mapToUpdateArticleResponse(article);
    }


    public UpdateArticleResponse likeArticle(Long articleId, Authentication authentication) {
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new CustomNotFoundException("User not found, the problem might be with token"));

            return this.likeArticleWithUserId(articleId, user.getId());
        }

        throw new CustomBadRequestException("User is not authenticated, the problem might be with token");
    }

    public UpdateArticleResponse viewArticle(Long articleId, ViewArticleRequest body) {
        Article article = this.getArticleById(articleId);

        if (body.anonymous()) {
            UserView view = UserView.builder()
                    .anonymousUser(true)
                    .viewedArticles(new LinkedList<>(Collections.singletonList(article)))
                    .build();

            article.getViews().add(view);
        } else {
            User user = userRepository.findById(body.userId())
                    .orElseThrow(() -> new CustomNotFoundException("User with id " + body.userId() + " not found"));

            boolean alreadyViewed = user.getViewedArticles()
                    .stream()
                    .anyMatch(
                            userView -> userView.getViewedArticles()
                                    .stream()
                                    .anyMatch(viewedArticle -> viewedArticle.getId().equals(articleId))
                    );

            if (alreadyViewed) {
                throw new CustomBadRequestException("User already viewed this article");
            }

            UserView view = UserView.builder()
                    .anonymousUser(false)
                    .user(user)
                    .viewedArticles(new LinkedList<>(Collections.singletonList(article)))
                    .build();

            user.getViewedArticles().add(view);
            article.getViews().add(view);
        }

        article = articleRepository.save(article);
        return articleMapper.mapToUpdateArticleResponse(article);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomNotFoundException("Article with id " + articleId + " not found"));
    }
}
