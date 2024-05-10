package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.dto.AuthCodeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Авторизация", description = "Контроллер для работы с авторизацией и регистрацией пользователей")
@Validated
public interface AuthController {

    @PostMapping(path = ControllerUrls.LOGIN_CODE_URL)
    @Operation(summary = "Отправка кода на почту", description = "Отправка одноразового кода для авторизации")
    ResponseEntity<?> sendCode(@RequestParam(value = "email") String email);

    @PostMapping(path = ControllerUrls.LOGIN_URL)
    @Operation(summary = "Логин пользователя", description = "Авторизация пользователя по одноразовому коду")
    ResponseEntity<?> login(@RequestBody @Validated AuthCodeDto authCodeDto, BindingResult bindingResult);

    @GetMapping(path = ControllerUrls.REFRESH_URL)
    @Operation(summary = "Refresh токена", description = "Выдача нового токена доступа в обмен на refresh токен")
    ResponseEntity<?> refresh(@RequestParam(value = "refresh") String refresh);
}
