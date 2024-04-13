package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.city_discover.constants.SwaggerDefaultInformation;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardCreateDto;
import org.city_discover.dto.PlaceCardUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    ResponseEntity<?> create(@RequestBody @Validated PlaceCardCreateDto placeCardDto,
                             BindingResult bindingResult);

    @GetMapping(path = ControllerUrls.PLACE_ID_URL)
    @Operation(summary = "Получить карточку места")
    ResponseEntity<PlaceCardDto> findById(@Parameter(description = "ID карточки места")
                                          @PathVariable UUID id);

    @GetMapping(path = ControllerUrls.PLACE_USER_ID_URL)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.PAGE_DESCRIPTION, name = SwaggerDefaultInformation.PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.SIZE_DESCRIPTION, name = SwaggerDefaultInformation.SIZE_NAME)
    @Operation(summary = "Получить карточки мест пользователя")
    ResponseEntity<Page<PlaceCardDto>> findByUserId(@Parameter(description = "ID пользователя")
                                                    @PathVariable UUID id,
                                                    @Parameter(hidden = true)
                                                    @PageableDefault(size = 100) Pageable pageable);

    ResponseEntity<?> update(PlaceCardUpdateDto placeCardDto, BindingResult bindingResult);
}
