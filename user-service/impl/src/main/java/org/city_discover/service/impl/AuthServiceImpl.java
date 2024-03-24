package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.AuthCodeDto;
import org.city_discover.dto.TokenDto;
import org.city_discover.dto.TokenUserDto;
import org.city_discover.dto.user.UserDto;
import org.city_discover.entity.AuthCodeEntity;
import org.city_discover.entity.TokenEntity;
import org.city_discover.exception.TooMuchRequestsException;
import org.city_discover.exception.UnauthorizedException;
import org.city_discover.repository.AuthCodeRepository;
import org.city_discover.service.AuthService;
import org.city_discover.service.EmailService;
import org.city_discover.service.TokenService;
import org.city_discover.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.city_discover.utill.RandomGenerator.generateCode;
import static org.city_discover.utill.TokenUtil.getUserFromToken;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthCodeRepository authCodeRepository;

    private static final Integer MIN_NUM = 100_000;
    private static final Integer MAX_NUM = 999_999;

    @Override
    @Transactional
    public void sendEmailCode(String email) {
        String code = generateCode(MIN_NUM, MAX_NUM);
        AuthCodeEntity authCode = AuthCodeEntity.builder()
                .email(email)
                .code(code)
                .build();

        Optional<AuthCodeEntity> current = authCodeRepository.findByEmail(email);
        if (current.isPresent()) {
            if (current.get().getCreationDate().plus(2, TimeUnit.MINUTES.toChronoUnit())
                    .isAfter(Instant.now())) {
                log.error("User with email [{}] already has code", email);
                throw new TooMuchRequestsException("Код для авторизации уже отправлен");
            }
            authCodeRepository.deleteAllCodes(authCode.getEmail());
        }

        authCodeRepository.insert(authCode);
        emailService.sendMessage(email, code);
    }

    @Override
    @Transactional
    public TokenUserDto login(AuthCodeDto authCodeDto) {
        String email = authCodeDto.getEmail();
        String code = authCodeDto.getCode();

        AuthCodeEntity authCode = authCodeRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("No such codes for email: [{}]", email);
                    return new UnauthorizedException("Нет активных кодов для авторизации");
                });

        checkAuthCode(authCode, code);
        authCodeRepository.deleteAllCodes(authCode.getEmail());

        UserDto user = userService.create(authCode.getEmail());
        TokenDto tokens = tokenService.create(user.getId());
        return new TokenUserDto(user, tokens);
    }

    @Override
    public TokenDto refresh(String refresh) {
        tokenService.verifyToken(refresh);

        UUID userId = UUID.fromString(getUserFromToken(refresh));
        TokenEntity token = tokenService.findByUserId(userId);

        if (token.getRefreshToken().equals(refresh)) {
            return tokenService.create(userId);
        }

        tokenService.deleteByUserId(userId);

        log.error("Incorrect refresh token");
        throw new UnauthorizedException("Refresh token не валиден");
    }

    private static void checkAuthCode(AuthCodeEntity entity, String code) {
        String email = entity.getEmail();

        if (!entity.getCode().equals(code) ||
                entity.getCreationDate().plus(10, TimeUnit.MINUTES.toChronoUnit()).isBefore(Instant.now())) {
            log.error("Incorrect code for email: [{}]", email);
            throw new UnauthorizedException("Код некорректный");
        }
    }
}
