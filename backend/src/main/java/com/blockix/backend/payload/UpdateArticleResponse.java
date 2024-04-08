package com.blockix.backend.payload;

import com.blockix.backend.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateArticleResponse {
    private ArticleDto article;
}
