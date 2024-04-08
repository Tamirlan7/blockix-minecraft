package com.blockix.backend.repository;

import com.blockix.backend.model.UserArticleMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleMessageRepository extends JpaRepository<UserArticleMessage, Long> {
}
