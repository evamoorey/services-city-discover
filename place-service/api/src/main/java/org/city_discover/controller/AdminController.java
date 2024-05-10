package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.dto.PlaceCardCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Админские эндпоинты", description = "Контроллер для работы админа")
@Validated
public interface AdminController {

    @PostMapping(path = ControllerUrls.ADMIN_PLACE_URL)
    @Operation(summary = "Создать карточку места", description = "Создать карточку места системно")
    ResponseEntity<?> create(@RequestBody @Validated PlaceCardCreateDto placeCardDto,
                             BindingResult bindingResult);

    @DeleteMapping(path = ControllerUrls.ADMIN_PLACE_ID_URL)
    @Operation(summary = "Удалить карточку места", description = "Удалить любую карточку места по ID")
    ResponseEntity<Boolean> delete(@Parameter(description = "ID карточки места")
                                   @PathVariable UUID id);
}
