package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.user_service.dto.AuthCodeDto;
import org.user_service.entity.AuthCodeEntity;
import org.user_service.repository.AuthCodeRepository;
import org.user_service.service.AuthService;
import org.user_service.service.EmailService;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.user_service.utill.RandomGenerator.generateCode;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;
    private final AuthCodeRepository authCodeRepository;

    private static final Integer MIN_NUM = 100_000;
    private static final Integer MAX_NUM = 999_999;

    @Override
    public void sendEmailCode(String email) {
        String authCode = generateCode(MIN_NUM, MAX_NUM);
        AuthCodeEntity entity = new AuthCodeEntity(email, authCode, Instant.now());

        authCodeRepository.insert(entity);
        emailService.sendMessage(email, authCode);
    }

    @Override
    public boolean checkEmailCode(AuthCodeDto authCodeDto) {
        String email = authCodeDto.getEmail();
        String code = authCodeDto.getCode();

        AuthCodeEntity entity = authCodeRepository.findByEmail(email);
        authCodeRepository.delete(entity);

        return entity.getCode().equals(code) &&
                entity.getCreationDate().plus(5, TimeUnit.MINUTES.toChronoUnit()).isAfter(Instant.now());
    }
}
