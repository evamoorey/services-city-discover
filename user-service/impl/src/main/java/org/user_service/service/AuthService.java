package org.user_service.service;

import org.user_service.dto.AuthCodeDto;
import org.user_service.dto.TokenDto;
import org.user_service.dto.TokenUserDto;

public interface AuthService {
    void sendEmailCode(String email);

    TokenUserDto login(AuthCodeDto authCodeDto);

    TokenDto refresh(String refresh);
}
