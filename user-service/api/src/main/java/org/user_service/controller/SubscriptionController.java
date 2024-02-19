package org.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.user_service.constants.ControllerUrls;

import java.util.UUID;

@Tag(name = "Подписки", description = "Контроллер для работы с подписками")
@Validated
public interface SubscriptionController {

    @PostMapping(path = ControllerUrls.SUBSCRIBE_URL)
    @Operation(summary = "Подписать текущего пользователя на другого")
    ResponseEntity<Boolean> subscribe(@Parameter(description = "ID пользователя издателя")
                                      @PathVariable UUID id);

    @PostMapping(path = ControllerUrls.UNSUBSCRIBE_URL)
    @Operation(summary = "Отписать текущего пользователя от другого")
    ResponseEntity<Boolean> unsubscribe(@Parameter(description = "ID пользователя издателя")
                                        @PathVariable UUID id);
}
