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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    private final JWSSigner jwsSigner;
    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSVerifier jwsVerifier;
    private final Duration tokenTtl;

    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.token-expiration}") String tokenExpiration
    ) throws ParseException, JOSEException {
        this.SECRET_KEY = secretKey;

        jwsSigner = new MACSigner(OctetSequenceKey.parse(SECRET_KEY));
        jwsVerifier = new MACVerifier(OctetSequenceKey.parse(SECRET_KEY));

        int duration = Integer.parseInt(tokenExpiration.substring(0, tokenExpiration.length() - 1));

        switch (tokenExpiration.charAt(tokenExpiration.length() - 1)) {
            case 'm':
                this.tokenTtl = Duration.ofMinutes(duration);
                break;
            case 'h':
                this.tokenTtl = Duration.ofHours(duration);
                break;
            case 'd':
                this.tokenTtl = Duration.ofDays(duration);
                break;
            default:
                // if in the application.yml jwt.access-token-expiration is not specified
                this.tokenTtl = Duration.ofMinutes(15);
        }

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
        Date now = new Date(System.currentTimeMillis());

        List<String> authorities = new ArrayList<>(user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Token.builder()
                .id(UUID.randomUUID().toString())
                .roles(authorities)
                .issuedAt(now)
                .expiration(Date.from(now.toInstant().plus(tokenTtl)))
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
                        .expiration(claimsSet.getExpirationTime())
                        .issuedAt(claimsSet.getIssueTime())
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
                .expirationTime(token.getExpiration())
                .issueTime(token.getIssuedAt())
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

}
