package org.city_discover.repository;

import org.city_discover.entity.TokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    void insert(TokenEntity entity);

    void deleteTokenByUserId(UUID userId);

    Optional<TokenEntity> findById(UUID userId);
}
