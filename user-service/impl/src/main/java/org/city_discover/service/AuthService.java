package org.city_discover.service;

import org.city_discover.dto.AuthCodeDto;
import org.city_discover.dto.TokenDto;
import org.city_discover.dto.TokenUserDto;

public interface AuthService {
    void sendEmailCode(String email);

    TokenUserDto login(AuthCodeDto authCodeDto);

    TokenDto refresh(String refresh);
}
