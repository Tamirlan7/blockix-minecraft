package com.blockix.backend.jwt;

import com.blockix.backend.config.JwtAuthenticationUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

@RequiredArgsConstructor
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtAuthenticationUserDetailsService userDetailsService;

    @Override
    public void init(HttpSecurity builder) throws Exception {
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var authenticationFilter = new AuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                jwtAuthenticationConverter
        );
        authenticationFilter.setSuccessHandler((request, response, authentication) ->
                response.setStatus(200)
        );

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(userDetailsService);

        builder.addFilterBefore(authenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
