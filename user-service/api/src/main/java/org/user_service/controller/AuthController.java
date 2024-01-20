package org.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.user_service.constants.ControllerUrls;
import org.user_service.dto.AuthCodeDto;

@Tag(name = "Авторизация", description = "Контроллер для работы с авторизацией и регистрацией пользователей")
@Validated
public interface AuthController {

    @PostMapping(path = ControllerUrls.LOGIN_CODE_URL)
    @Operation(summary = "Отправка кода на почту")
    ResponseEntity<?> sendCode(@RequestParam(value = "email") String email);

    @GetMapping(path = ControllerUrls.LOGIN_URL)
    @Operation(summary = "Логин пользователя")
    ResponseEntity<?> login(@RequestBody @Validated AuthCodeDto authCodeDto, BindingResult bindingResult);

    @GetMapping(path = ControllerUrls.REFRESH_URL)
    @Operation(summary = "Рефреш токена")
    ResponseEntity<?> refresh(@RequestParam(value = "refresh") String refresh);
}
