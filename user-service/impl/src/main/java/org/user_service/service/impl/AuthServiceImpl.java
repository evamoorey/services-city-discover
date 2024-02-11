package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.user_service.dto.AuthCodeDto;
import org.user_service.dto.TokenDto;
import org.user_service.dto.TokenUserDto;
import org.user_service.dto.UserDto;
import org.user_service.entity.AuthCodeEntity;
import org.user_service.entity.TokenEntity;
import org.user_service.exception.TooMuchRequestsException;
import org.user_service.exception.UnauthorizedException;
import org.user_service.repository.AuthCodeRepository;
import org.user_service.service.AuthService;
import org.user_service.service.EmailService;
import org.user_service.service.TokenService;
import org.user_service.service.UserService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.user_service.utill.RandomGenerator.generateCode;
import static org.user_service.utill.TokenUtil.getUserFromToken;

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
                throw new TooMuchRequestsException("User with email [%s] already has code".formatted(email));
            }
            authCodeRepository.deleteAllCodes(authCode.getEmail());
        }

        authCodeRepository.insert(authCode);
        emailService.sendMessage(email, code);
    }

    @Override
    public TokenUserDto login(AuthCodeDto authCodeDto) {
        String email = authCodeDto.getEmail();
        String code = authCodeDto.getCode();

        AuthCodeEntity authCode = authCodeRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("No such codes for email: [{}]", email);
                    return new UnauthorizedException("No such codes for email: [%s]".formatted(email));
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

        UUID userId = getUserFromToken(refresh);
        TokenEntity token = tokenService.findByUserId(userId);

        if (token.getRefreshToken().equals(refresh)) {
            return tokenService.create(userId);
        }

        tokenService.deleteByUserId(userId);
        throw new UnauthorizedException("Incorrect refresh token");
    }

    private static void checkAuthCode(AuthCodeEntity entity, String code) {
        String email = entity.getEmail();

        if (!entity.getCode().equals(code) ||
                entity.getCreationDate().plus(10, TimeUnit.MINUTES.toChronoUnit()).isBefore(Instant.now())) {
            log.error("Incorrect code for email: [{}]", email);
            throw new UnauthorizedException("Incorrect code for email: [%s]".formatted(email));
        }
    }
}
