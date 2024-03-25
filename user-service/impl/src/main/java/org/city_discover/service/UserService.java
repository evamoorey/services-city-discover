package org.city_discover.service;


import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserPublicDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDto create(String email);

    UserDto update(UUID id, UserUpdateDto userUpdateDto);

    UserDto findPrivateBy(UUID id);

    UserPublicDto findBy(UUID id);

    void delete(UUID userId);

    Page<UserPublicDto> findAll(String username, Pageable pageable);
}
