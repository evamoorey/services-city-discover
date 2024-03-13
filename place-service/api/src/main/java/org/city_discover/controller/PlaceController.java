package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.dto.PlaceCardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Карточки мест", description = "Контроллер для работы с карточками мест")
@Validated
public interface PlaceController {

    @PostMapping(path = ControllerUrls.PLACE_URL)
    @Operation(summary = "Создать карточку места")
    ResponseEntity<?> create(@RequestBody @Validated PlaceCardDto placeCardDto,
                             BindingResult bindingResult);

    @GetMapping(path = ControllerUrls.PLACE_ID_URL)
    @Operation(summary = "Получить карточку места")
    ResponseEntity<PlaceCardDto> findById(@Parameter(description = "ID карточки места")
                                          @PathVariable UUID id);
}
