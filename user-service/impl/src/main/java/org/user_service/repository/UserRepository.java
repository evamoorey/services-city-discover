package org.user_service.repository;

import org.user_service.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void insert(UserEntity entity);

    UserEntity update(UserEntity entity);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID id);
}
