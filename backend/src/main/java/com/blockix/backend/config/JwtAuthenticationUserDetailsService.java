package com.blockix.backend.config;

import com.blockix.backend.exception.CustomNotFoundException;
import com.blockix.backend.jwt.model.Token;
import com.blockix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        if (token.getPrincipal() instanceof Token accessToken) {
            return userRepository.findById(accessToken.getUserId())
                    .orElseThrow(() -> new CustomNotFoundException("User with id + " + accessToken.getUserId() + " not found"));
        }

        return null;
    }
}
