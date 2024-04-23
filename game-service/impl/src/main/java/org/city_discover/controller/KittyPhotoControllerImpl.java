package org.city_discover.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.service.MinioAdapter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class KittyPhotoControllerImpl implements KittyPhotoController {

    private final MinioAdapter minioAdapter;

    @Override
    public ResponseEntity<UUID> upload(MultipartFile file) {
        return ResponseEntity.ok().body(minioAdapter.save(file));
    }

    @Override
    public ResponseEntity<byte[]> upload(UUID id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(minioAdapter.download(id));
    }
}
