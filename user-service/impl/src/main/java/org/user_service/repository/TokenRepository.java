package org.user_service.repository;

import org.user_service.entity.TokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    void insert(TokenEntity entity);

    void deleteTokenByUserId(UUID userId);

    Optional<TokenEntity> findById(UUID userId);
}
