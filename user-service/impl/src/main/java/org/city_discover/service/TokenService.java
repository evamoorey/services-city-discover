package org.city_discover.service;

import org.city_discover.dto.TokenDto;
import org.city_discover.entity.TokenEntity;

import java.util.UUID;

public interface TokenService {
    TokenDto create(UUID id);

    void verifyToken(String token);

    void verifyAdmin(String authorization);

    TokenEntity findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
