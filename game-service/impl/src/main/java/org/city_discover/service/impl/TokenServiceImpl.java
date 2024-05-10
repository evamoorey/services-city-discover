package org.city_discover.service.impl;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.exception.UnauthorizedException;
import org.city_discover.properties.JWTProperties;
import org.city_discover.service.TokenService;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@AllArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JWTProperties jwtProperties;

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
    public void verifyAdmin(String authorization) {
        if (!authorization.equals(jwtProperties.getAdminSecret())) {
            log.error("User not admin");
            throw new UnauthorizedException("Невозможно валидировать JWT token админа");
        }
    }
}
