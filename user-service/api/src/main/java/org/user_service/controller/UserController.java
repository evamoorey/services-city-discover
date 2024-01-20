package org.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.user_service.constants.ControllerUrls;
import org.user_service.dto.UserUpdateDto;

@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
@Validated
public interface UserController {

    @PostMapping(path = ControllerUrls.USER_URL)
    @Operation(summary = "Обновление пользователя")
    ResponseEntity<?> update(@RequestBody @Validated UserUpdateDto userUpdateDto,
                             BindingResult bindingResult);
}
