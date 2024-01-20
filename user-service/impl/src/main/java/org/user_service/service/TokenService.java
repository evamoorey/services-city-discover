package org.user_service.service;

import org.user_service.dto.TokenDto;
import org.user_service.entity.TokenEntity;

import java.util.UUID;

public interface TokenService {
    TokenDto create(UUID id);

    void verifyToken(String token);

    TokenEntity findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
