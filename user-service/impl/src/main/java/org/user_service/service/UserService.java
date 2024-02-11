package org.user_service.service;


import org.user_service.dto.UserDto;
import org.user_service.dto.UserUpdateDto;

import java.util.UUID;

public interface UserService {
    UserDto create(String email);

    UserDto update(UUID id, UserUpdateDto userUpdateDto);
}
