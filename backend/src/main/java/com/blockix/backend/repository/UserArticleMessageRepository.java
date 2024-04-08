package com.blockix.backend.repository;

import com.blockix.backend.model.ArticleMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleMessageRepository extends JpaRepository<ArticleMessage, Long> {
}
