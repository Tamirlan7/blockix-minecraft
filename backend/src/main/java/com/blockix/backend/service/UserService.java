package com.blockix.backend.service;

import com.blockix.backend.exception.CustomBadRequestException;
import com.blockix.backend.exception.CustomNotFoundException;
import com.blockix.backend.model.Article;
import com.blockix.backend.model.CustomUserDetails;
import com.blockix.backend.model.User;
import com.blockix.backend.model.UserView;
import com.blockix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public List<Article> findAllViewedArticles(Authentication authentication) {
        User user = this.extractUserFromAuthentication(authentication);

        List<Article> result = user.getViewedArticles().stream().map(UserView::getArticle).toList();
        return result;
    }

    private User extractUserFromAuthentication(Authentication authentication) {
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new CustomNotFoundException("User not found, the problem might be with token"));
        }

        throw new CustomBadRequestException("User is not authenticated, the problem might be with token");
    }
}
