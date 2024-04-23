package org.city_discover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.city_discover.constant.ControllerUrls;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Tag(name = "Фото игровых персонажей", description = "Контроллер для работы с фото персонажей" )
public interface GameKittyPhotoController {

    @PostMapping(path = ControllerUrls.KITTY_PHOTO_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить фото места" )
    ResponseEntity<UUID> upload(@RequestPart("file" ) MultipartFile file);


    @GetMapping(path = ControllerUrls.KITTY_PHOTO_URL)
    @Operation(summary = "Скачать фото места" )
    ResponseEntity<byte[]> upload(@RequestParam(value = "fileId" ) UUID id);
}
