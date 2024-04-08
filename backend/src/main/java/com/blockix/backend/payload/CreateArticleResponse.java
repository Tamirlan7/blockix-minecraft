package com.blockix.backend.payload;

import com.blockix.backend.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateArticleResponse {
    private ArticleDto article;
}
