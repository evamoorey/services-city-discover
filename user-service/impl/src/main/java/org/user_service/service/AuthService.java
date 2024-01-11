package org.user_service.service;

import org.user_service.dto.AuthCodeDto;

public interface AuthService {
    void sendEmailCode(String email);

    boolean checkEmailCode(AuthCodeDto authCodeDto);
}
