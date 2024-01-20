package org.user_service.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.user_service.exception.UnauthorizedException;
import org.user_service.service.TokenService;

import java.util.Set;

import static org.user_service.utill.TokenUtil.getUserFromToken;

@Component
@Slf4j
public class SecurityConfig extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final TokenService tokenService;
    private static final Set<String> whitelistURI = Set.of(
            "/user-service/auth/code",
            "/user-service/auth/login",
            "/user-service/auth/refresh");

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            if (!checkWhitelabelURI(request.getRequestURI())) {
                String authorization = request.getHeader("Authorization");

                tokenService.verifyToken(authorization);

                Object userId = getUserFromToken(authorization);

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
}
