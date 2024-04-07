package com.blockix.backend.jwt.service;

import com.blockix.backend.jwt.model.Token;
import com.blockix.backend.jwt.model.TokenType;
import com.blockix.backend.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    private final JWSSigner jwsSigner;
    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSVerifier jwsVerifier;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;

    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-token-expiration}") String accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") String refreshTokenExpiration
    ) throws ParseException, JOSEException {
        jwsSigner = new MACSigner(OctetSequenceKey.parse(secretKey));
        jwsVerifier = new MACVerifier(OctetSequenceKey.parse(secretKey));

        this.accessTokenTtl = this.parseDuration(accessTokenExpiration);
        this.refreshTokenTtl = this.parseDuration(refreshTokenExpiration);
    }

    public String generateAccessToken(User user) {
        return this.serializeToken(
                this.createToken(user, TokenType.ACCESS_TOKEN)
        );
    }

    public String generateRefreshToken(User user) {
        return this.serializeToken(
                this.createToken(user, TokenType.REFRESH_TOKEN)
        );
    }

    private Token createToken(User user, TokenType tokenType) {
        Instant now = Instant.now();

        Instant expiration = tokenType == TokenType.ACCESS_TOKEN
                ? now.plus(accessTokenTtl)
                : now.plus(refreshTokenTtl);

        return Token.builder()
                .id(UUID.randomUUID().toString())
                .roles(List.of(user.getRole().name()))
                .issuedAt(now)
                .expiration(expiration)
                .userId(user.getId())
                .tokenType(tokenType)
                .build();
    }

    public Token deserializeToken(String strToken) {
        try {
            var signedJWT = SignedJWT.parse(strToken);

            if (signedJWT.verify(jwsVerifier)) {
                var claimsSet = signedJWT.getJWTClaimsSet();

                return Token.builder()
                        .id(claimsSet.getJWTID())
                        .expiration(claimsSet.getExpirationTime().toInstant())
                        .issuedAt(claimsSet.getIssueTime().toInstant())
                        .roles(claimsSet.getStringListClaim("roles"))
                        .userId(claimsSet.getLongClaim("userId"))
                        .tokenType(TokenType.valueOf(claimsSet.getStringClaim("tokenType")))
                        .build();
            }
        } catch (JOSEException | ParseException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    private String serializeToken(Token token) {
        JWSHeader jwsHeader = new JWSHeader.Builder(jwsAlgorithm)
                .keyID(token.getId())
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .expirationTime(Date.from(token.getExpiration()))
                .issueTime(Date.from(token.getIssuedAt()))
                .jwtID(token.getId())
                .claim("tokenType", token.getTokenType().toString())
                .claim("roles", token.getRoles())
                .claim("userId", token.getUserId())
                .build();

        var signedJwt = new SignedJWT(jwsHeader, claimsSet);

        try {
            signedJwt.sign(this.jwsSigner);
            return signedJwt.serialize();
        } catch (JOSEException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    public boolean isValid(Token token) {
        return !this.isExpired(token);
    }

    private boolean isExpired(Token token) {
        return token.getExpiration().isBefore(Instant.now());
    }

    private Duration parseDuration(String tokenDuration) {

        int duration = Integer.parseInt(tokenDuration.substring(0, tokenDuration.length() - 1));

        return switch (tokenDuration.charAt(tokenDuration.length() - 1)) {
            case 'm' -> Duration.ofMinutes(duration);
            case 'h' -> Duration.ofHours(duration);
            case 'd' -> Duration.ofDays(duration);
            default ->
                // if in the application.yml jwt.access-token-expiration is not specified
                    Duration.ofMinutes(15);
        };
    }
}
