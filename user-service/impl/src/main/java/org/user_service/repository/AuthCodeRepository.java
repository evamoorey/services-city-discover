package org.user_service.repository;

import org.user_service.entity.AuthCodeEntity;

public interface AuthCodeRepository {
    AuthCodeEntity insert(AuthCodeEntity entity);

    AuthCodeEntity findByEmail(String email);

    void delete(AuthCodeEntity entity);
}
