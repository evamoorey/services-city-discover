package org.user_service.service;

import org.user_service.dto.AuthCodeDto;
import org.user_service.dto.TokenDto;

public interface AuthService {
    void sendEmailCode(String email);

    TokenDto login(AuthCodeDto authCodeDto);

    TokenDto refresh(String refresh);
}
