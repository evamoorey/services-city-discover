package org.user_service.service;

import org.user_service.dto.TokenDto;

import java.util.UUID;

public interface TokenService {
    TokenDto create(UUID id);

    void verifyToken(String token);
}
