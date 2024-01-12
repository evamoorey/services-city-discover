package org.user_service.repository;

import org.user_service.entity.AuthCodeEntity;

import java.util.Optional;

public interface AuthCodeRepository {
    void insert(AuthCodeEntity entity);

    Optional<AuthCodeEntity> findByEmail(String email);

    void deleteAllCodes(String email);
}
