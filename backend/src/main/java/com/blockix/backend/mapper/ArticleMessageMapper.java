package com.blockix.backend.mapper;

import com.blockix.backend.dto.ArticleMessageDto;
import com.blockix.backend.model.ArticleMessage;

public class ArticleMessageMapper {

    public static ArticleMessageDto mapToDto(ArticleMessage articleMessage) {
        return ArticleMessageDto.builder()
                .id(articleMessage.getId())
                .message(articleMessage.getMessage())
                .sender(UserMapper.mapToDto(articleMessage.getUser()))
                .build();
    }

}
