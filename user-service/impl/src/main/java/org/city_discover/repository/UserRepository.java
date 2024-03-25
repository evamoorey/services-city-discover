package org.city_discover.repository;

import org.city_discover.dto.user.UserPublicDto;
import org.city_discover.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UserEntity insert(UserEntity entity);

    UserEntity update(UserEntity entity);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void delete(UUID userId);

    Page<UserPublicDto> findAll(String username, Pageable pageable);
}
