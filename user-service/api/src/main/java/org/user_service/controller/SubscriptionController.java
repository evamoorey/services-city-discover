package org.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.user_service.constants.ControllerUrls;
import org.user_service.dto.SubscriptionDto;

import java.util.UUID;

import static org.user_service.constants.SwaggerDefaultInformation.*;

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

    @GetMapping(path = ControllerUrls.SUBSCRIBERS_URL)
    @Parameter(in = ParameterIn.QUERY, description = PAGE_DESCRIPTION, name = PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SIZE_DESCRIPTION, name = SIZE_NAME)
    @Operation(summary = "Получить подписчиков пользователя")
    ResponseEntity<Page<SubscriptionDto>> findSubscribers(@Parameter(description = "ID пользователя")
                                                          @PathVariable UUID id,
                                                          @Parameter(hidden = true)
                                                          @PageableDefault(size = 100) Pageable pageable);

    @GetMapping(path = ControllerUrls.SUBSCRIPTIONS_URL)
    @Parameter(in = ParameterIn.QUERY, description = PAGE_DESCRIPTION, name = PAGE_NAME)
    @Parameter(in = ParameterIn.QUERY, description = SIZE_DESCRIPTION, name = SIZE_NAME)
    @Operation(summary = "Получить подписки пользователя")
    ResponseEntity<Page<SubscriptionDto>> findSubscriptions(@Parameter(description = "ID пользователя")
                                                            @PathVariable UUID id,
                                                            @Parameter(hidden = true)
                                                            @PageableDefault(size = 100) Pageable pageable);
}
