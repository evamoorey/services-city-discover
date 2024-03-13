package org.city_discover.service.impl;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.city_discover.dto.TokenDto;
import org.city_discover.entity.TokenEntity;
import org.city_discover.exception.UnauthorizedException;
import org.city_discover.properties.JWTProperties;
import org.city_discover.repository.TokenRepository;
import org.city_discover.service.TokenService;

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
        String refresh = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(String.valueOf(id))
                .issuedAt(Date.from(timeNow))
                .expiration(Date.from(timeNow.plus(30L, ChronoUnit.DAYS)))
                .signWith(key)
                .compact();

        TokenEntity token = TokenEntity.builder()
                .userId(id)
                .refreshToken(refresh)
                .build();

        tokenRepository.deleteTokenByUserId(id);
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
            throw new UnauthorizedException("Невозможно валидировать JWT token");
        }
    }

    @Override
    public TokenEntity findByUserId(UUID userId) {
        return tokenRepository.findById(userId).orElseThrow(() -> {
            log.error("No such tokens for user: [{}]", userId);
            return new UnauthorizedException("Нет токенов для пользователя");
        });
    }

    @Override
    public void deleteByUserId(UUID userId) {
        tokenRepository.deleteTokenByUserId(userId);
    }
}
