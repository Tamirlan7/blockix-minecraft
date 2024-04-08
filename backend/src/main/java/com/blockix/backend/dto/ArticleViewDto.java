package com.blockix.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleViewDto {
    private long id;
    private boolean anonymous;
    private UserDto seenByUser;
}
