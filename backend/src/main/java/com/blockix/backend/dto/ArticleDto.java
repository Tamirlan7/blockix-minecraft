package com.blockix.backend.dto;

import com.blockix.backend.model.ArticleMediaFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private long id;
    private String title;
    private String body;
    private long sharedCount;
    private long views;
    private long likes;
    private List<ArticleMediaFile> mediaFiles;
    private List<ArticleMessageDto> comments;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
