package org.city_discover.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.exception.UnauthorizedException;
import org.city_discover.service.TokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Set;

import static org.city_discover.utill.TokenUtil.getUserFromToken;

@Component
@Slf4j
public class SecurityConfig extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final TokenService tokenService;
    private static final Set<String> whitelistURI = Set.of(
            "/place-service"
    );

    private static final Set<String> swaggerURI = Set.of(
            "/game-service/swagger-ui",
            "/game-service/v3/api-docs");

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String authorization = request.getHeader("Authorization");
            tokenService.verifyToken(authorization);

            Object userId = getUserFromToken(authorization);
            request.setAttribute("id", userId);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Can't filter user by headers");
            resolver.resolveException(request, response, null, new UnauthorizedException(e.getMessage()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return swaggerURI.stream().anyMatch(request.getRequestURI()::contains)
                || whitelistURI.stream().anyMatch(request.getRequestURI()::contains);
    }
}
