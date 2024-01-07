package org.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.user_service.constants.ControllerUrls;
import org.user_service.dto.UserCreateDto;

@Tag(name = "Авторизация", description = "Контроллер для работы с авторизацией и регистрацией пользователей")
@Validated
public interface AuthController {

    @PostMapping(path = ControllerUrls.USER_URL)
    @Operation(summary = "Регистрация пользователя")
    ResponseEntity<?> create(@RequestBody @Validated UserCreateDto userCreateDto,
                             BindingResult bindingResult);

    @PostMapping(path = ControllerUrls.CODE_URL)
    @Operation(summary = "Отправка кода на почту")
    ResponseEntity<?> sendCode(@RequestParam(value = "email") String email);

    @GetMapping(path = ControllerUrls.CODE_URL)
    @Operation(summary = "Проверка корректности кода авторизации")
    ResponseEntity<?> checkCode(@RequestParam(value = "email") String email);
}
