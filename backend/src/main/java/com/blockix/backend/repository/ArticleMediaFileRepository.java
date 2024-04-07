package com.blockix.backend.repository;

import com.blockix.backend.model.ArticleMediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMediaFileRepository extends JpaRepository<ArticleMediaFile, Long> {
}
