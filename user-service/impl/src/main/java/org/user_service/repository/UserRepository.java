package org.user_service.repository;

import org.user_service.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    void insert(UserEntity entity);

    Optional<UserEntity> findByEmail(String email);
}
