package com.blockix.backend.service;

import com.blockix.backend.dto.CreateNewsRequest;
import com.blockix.backend.dto.CreateNewsResponse;
import com.blockix.backend.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public CreateNewsResponse createNews(CreateNewsRequest body) {
        return null;
    }
}
