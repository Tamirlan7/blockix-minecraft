package com.blockix.backend.dto;

import com.blockix.backend.model.UserArticleMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetArticlesResponse {
    private long id;
    private String title;
    private String body;
    private long sharedCount;
    private long views;
    private long likes;
    private List<UserArticleMessage> comments;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
