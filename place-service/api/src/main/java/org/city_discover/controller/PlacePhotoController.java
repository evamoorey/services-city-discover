package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Tag(name = "Фото карточек мест", description = "Контроллер для работы с фото мест")
public interface PlacePhotoController {

    @PostMapping(path = ControllerUrls.PLACE_PHOTO_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить фото места")
    ResponseEntity<UUID> upload(@RequestPart("file") MultipartFile file);


    @GetMapping(path = ControllerUrls.PLACE_PHOTO_URL)
    @Operation(summary = "Скачать фото места")
    ResponseEntity<Mono<InputStreamResource>> upload(@RequestParam(value = "fileId") UUID id);
}
