package org.user_service.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.user_service.dto.TokenDto;
import org.user_service.entity.TokenEntity;
import org.user_service.properties.JWTProperties;
import org.user_service.repository.TokenRepository;
import org.user_service.service.TokenService;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final JWTProperties jwtProperties;

    @Override
    public TokenDto create(UUID id) {
        Instant timeNow = Instant.now();
        Key key = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getSecret()),
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
}
