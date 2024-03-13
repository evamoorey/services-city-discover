package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.city_discover.dto.user.UserPublicDto;

import java.util.UUID;

@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
@Validated
public interface UserController {

    @PostMapping(path = ControllerUrls.USER_URL)
    @Operation(summary = "Обновить текущего пользователя")
    ResponseEntity<?> update(@RequestBody @Validated UserUpdateDto userUpdateDto,
                             BindingResult bindingResult);

    @GetMapping(path = ControllerUrls.USER_URL)
    @Operation(summary = "Получить данные текущего пользователя")
    ResponseEntity<UserDto> findInfo();

    @DeleteMapping(path = ControllerUrls.USER_URL)
    @Operation(summary = "Удалить текущего пользователя")
    ResponseEntity<Boolean> delete();

    @GetMapping(path = ControllerUrls.USER_ID_URL)
    @Operation(summary = "Получить пользователя")
    ResponseEntity<UserPublicDto> findById(@Parameter(description = "ID пользователя")
                                           @PathVariable UUID id);
}