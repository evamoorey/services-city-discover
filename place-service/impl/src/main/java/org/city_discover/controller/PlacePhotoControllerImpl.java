package org.city_discover.controller;

import lombok.AllArgsConstructor;
import org.city_discover.service.MinioAdapter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class PlacePhotoControllerImpl implements PlacePhotoController {

    private final MinioAdapter minioAdapter;

    @Override
    public ResponseEntity<UUID> upload(MultipartFile file) {
        return ResponseEntity.ok().body(minioAdapter.save(file));
    }

    @Override
    public ResponseEntity<byte[]> download(UUID id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(minioAdapter.download(id));
    }
}
