package org.user_service.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.user_service.exception.UnauthorizedException;
import org.user_service.service.TokenService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class SecurityConfig extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    private static final Set<String> whitelistURI = Set.of("/user-service/auth/code", "/user-service/auth/login");

    public SecurityConfig(TokenService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            if (!checkWhitelabelURI(request.getRequestURI())) {
                String authorization = request.getHeader("Authorization");

                tokenService.verifyToken(authorization);

                Map<String, Object> tokenInfo = decodeToken(authorization);
                Object userId = tokenInfo.get("sub");

                request.setAttribute("id", userId);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Can't filter user by headers");
            resolver.resolveException(request, response, null, new UnauthorizedException(e.getMessage()));
        }
    }

    private boolean checkWhitelabelURI(String requestURI) {
        return whitelistURI.contains(requestURI);
    }

    private Map<String, Object> decodeToken(String authorization) {
        Map<String, Object> tokenInfo = null;

        if (authorization != null && !authorization.isBlank()) {
            try {
                String jwtUserPart = authorization.split("\\.")[1];
                String info = new String(Base64.getDecoder().decode(jwtUserPart), StandardCharsets.UTF_8);
                tokenInfo = objectMapper.readValue(info, new TypeReference<>() {
                });
            } catch (RuntimeException | JsonProcessingException e) {
                log.error("Token can't parse", e);
                throw new UnauthorizedException("Token can't parse: [%s]".formatted(e.getMessage()));
            }
        }

        return tokenInfo;
    }
}
