package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.constants.SwaggerDefaultInformation;
import org.city_discover.dto.user.UserDto;
import org.city_discover.dto.user.UserPublicDto;
import org.city_discover.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = ControllerUrls.USERS_URL)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.PAGE_DESCRIPTION, name = SwaggerDefaultInformation.PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.SIZE_DESCRIPTION, name = SwaggerDefaultInformation.SIZE_NAME)
    @Operation(summary = "Получить всех пользователей")
    ResponseEntity<Page<UserPublicDto>> findAll(@Parameter(description = "Username пользователя")
                                                @RequestParam(required = false) String username,
                                                @Parameter(hidden = true)
                                                @PageableDefault(size = 100) Pageable pageable
    );
}
