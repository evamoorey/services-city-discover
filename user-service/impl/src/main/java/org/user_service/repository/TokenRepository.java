package org.user_service.repository;

import org.user_service.entity.TokenEntity;

import java.util.UUID;

public interface TokenRepository {
    void insert(TokenEntity entity);

    void deleteAllTokens(UUID userId);
}
