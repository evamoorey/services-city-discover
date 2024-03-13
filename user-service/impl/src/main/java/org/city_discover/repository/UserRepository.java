package org.city_discover.repository;

import org.city_discover.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UserEntity insert(UserEntity entity);

    UserEntity update(UserEntity entity);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void delete(UUID userId);
}
