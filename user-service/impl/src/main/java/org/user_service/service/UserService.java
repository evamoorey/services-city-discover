package org.user_service.service;


import org.user_service.dto.user.UserDto;
import org.user_service.dto.user.UserPublicDto;
import org.user_service.dto.user.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDto create(String email);

    UserDto update(UUID id, UserUpdateDto userUpdateDto);

    UserDto findPrivateBy(UUID id);

    UserPublicDto findBy(UUID id);

    void delete(UUID userId);
}
