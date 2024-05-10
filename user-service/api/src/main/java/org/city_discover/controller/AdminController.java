package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Админские эндпоинты", description = "Контроллер для работы админа")
@Validated
public interface AdminController {

    @DeleteMapping(path = ControllerUrls.ADMIN_USER_ID_URL)
    @Operation(summary = "Удалить текущего пользователя")
    ResponseEntity<Boolean> delete(@Parameter(description = "ID пользователя")
                                   @PathVariable UUID id);
}
