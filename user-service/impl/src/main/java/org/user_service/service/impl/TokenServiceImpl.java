package org.user_service.service.impl;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.user_service.dto.TokenDto;
import org.user_service.entity.TokenEntity;
import org.user_service.exception.UnauthorizedException;
import org.user_service.properties.JWTProperties;
import org.user_service.repository.TokenRepository;
import org.user_service.service.TokenService;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final JWTProperties jwtProperties;

    @Override
    public TokenDto create(UUID id) {
        Instant timeNow = Instant.now();
        SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getSecret()),
                SignatureAlgorithm.HS256.getJcaName());

        String access = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(String.valueOf(id))
                .issuedAt(Date.from(timeNow))
                .expiration(Date.from(timeNow.plus(15L, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
        String refresh = String.valueOf(UUID.randomUUID());

        TokenEntity token = TokenEntity.builder()
                .userId(id)
                .refreshToken(refresh)
                .build();

        tokenRepository.deleteAllTokens(id);
        tokenRepository.insert(token);
        return new TokenDto(access, refresh);
    }

    @Override
    public void verifyToken(String token) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getSecret()),
                SignatureAlgorithm.HS256.getJcaName());

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();
        try {
            jwtParser.parse(token);
        } catch (Exception e) {
            log.error("Could not verify JWT token");
            throw new UnauthorizedException("Could not verify JWT token");
        }
    }
}
