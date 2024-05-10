package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constants.ControllerUrls;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Фото карточек мест", description = "Контроллер для работы с фото мест")
public interface PlacePhotoController {

    @PostMapping(path = ControllerUrls.PLACE_PHOTO_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить изображение места",
            description = "Загрузить изображение места в формате MultipartFile")
    ResponseEntity<UUID> upload(@RequestPart("file") MultipartFile file);


    @GetMapping(path = ControllerUrls.PLACE_PHOTO_URL)
    @Operation(summary = "Скачать изображение места",
            description = "Скачать изображение места в формате массива байт")
    ResponseEntity<byte[]> download(@RequestParam(value = "fileId") UUID id);
}
