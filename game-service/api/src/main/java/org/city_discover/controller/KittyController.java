package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constant.ControllerUrls;
import org.city_discover.constant.SwaggerDefaultInformation;
import org.city_discover.dto.KittyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Игровые персонажи", description = "Контроллер для работы с персонажами")
public interface KittyController {

    @GetMapping(path = ControllerUrls.KITTY_URL)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.PAGE_DESCRIPTION, name = SwaggerDefaultInformation.PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.SIZE_DESCRIPTION, name = SwaggerDefaultInformation.SIZE_NAME)
    @Operation(summary = "Получить персонажей поблизости", description = "Получить персонажей, находящихся близко к координатам")
    ResponseEntity<Page<KittyDto>> findNear(@Parameter(description = "Широта нахождения персонажа")
                                            @RequestParam Double latitude,
                                            @Parameter(description = "Долгота нахождения персонажа")
                                            @RequestParam Double longitude,
                                            @Parameter(hidden = true)
                                            @PageableDefault(size = 100) Pageable pageable);


}
