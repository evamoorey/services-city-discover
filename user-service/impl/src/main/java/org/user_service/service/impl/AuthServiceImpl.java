package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.user_service.service.AuthService;
import org.user_service.service.EmailService;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;

    @Override
    public void sendEmailCode(String email) {
        emailService.sendMessage(email, "123a");
    }
}
