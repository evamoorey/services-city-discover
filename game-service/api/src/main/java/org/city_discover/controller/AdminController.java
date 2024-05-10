package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constant.ControllerUrls;
import org.city_discover.dto.KittyCreateDto;
import org.city_discover.dto.KittyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Контроллер админа", description = "Контроллер для работы администратора")
public interface AdminController {

    @PostMapping(path = ControllerUrls.ADMIN_KITTY_URL)
    @Operation(summary = "Создать кота")
    ResponseEntity<KittyDto> createKitty(@RequestBody @Validated KittyCreateDto kittyCreateDto,
                                         BindingResult bindingResult);

    @PostMapping(path = ControllerUrls.ADMIN_KITTY_OWNER_URL)
    @Operation(summary = "Выдать кота пользователю")
    ResponseEntity<Boolean> giveKitty(@Parameter(description = "ID пользователя")
                                      @RequestParam UUID id,
                                      @Parameter(description = "ID кота")
                                      @RequestParam UUID kitty);

    @DeleteMapping(path = ControllerUrls.ADMIN_KITTY_OWNER_URL)
    @Operation(summary = "Забрать кота у пользователя")
    ResponseEntity<Boolean> deleteUserKitty(@Parameter(description = "ID пользователя")
                                            @RequestParam UUID id,
                                            @Parameter(description = "ID кота")
                                            @RequestParam UUID kitty);

    @DeleteMapping(path = ControllerUrls.ADMIN_KITTY_URL)
    @Operation(summary = "Удалить кота из системы")
    ResponseEntity<Boolean> deleteKitty(@Parameter(description = "ID кота")
                                        @RequestParam UUID kitty);
}
