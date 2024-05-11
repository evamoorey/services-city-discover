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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Владельцы игровых персонажей", description = "Контроллер для работы с владельцами персонажей")
public interface OwnerController {

    @GetMapping(path = ControllerUrls.KITTY_OWNER_ID_URL)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.PAGE_DESCRIPTION, name = SwaggerDefaultInformation.PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SwaggerDefaultInformation.SIZE_DESCRIPTION, name = SwaggerDefaultInformation.SIZE_NAME)
    @Operation(summary = "Получить персонажей пользователя", description = "Получить персонажей пользователя по его ID")
    ResponseEntity<Page<KittyDto>> findByOwnerId(@Parameter(description = "ID пользователя")
                                                 @PathVariable UUID id,
                                                 @Parameter(hidden = true)
                                                 @PageableDefault(size = 100) Pageable pageable);

    @PostMapping(path = ControllerUrls.KITTY_OWNER_URL)
    @Operation(summary = "Выдать игрового персонажа пользователю", description = "Выдать текущему пользователю игрового персонажа")
    ResponseEntity<Boolean> giveKitty(@Parameter(description = "ID игрового персонажа")
                                      @RequestParam UUID kitty);

}
