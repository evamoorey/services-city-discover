package org.city_discover.repository;

import org.city_discover.entity.AuthCodeEntity;

import java.util.Optional;

public interface AuthCodeRepository {
    void insert(AuthCodeEntity entity);

    Optional<AuthCodeEntity> findByEmail(String email);

    void deleteAllCodes(String email);
}
