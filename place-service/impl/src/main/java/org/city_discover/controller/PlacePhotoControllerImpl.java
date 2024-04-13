package org.city_discover.controller;

import lombok.AllArgsConstructor;
import org.city_discover.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class PlacePhotoControllerImpl implements PlacePhotoController {

    private final MinioService minioService;

    @Override
    public ResponseEntity<UUID> upload(MultipartFile file) {
        return ResponseEntity.ok().body(minioService.save(file));
    }

    @Override
    public ResponseEntity<Mono<InputStreamResource>> upload(UUID id) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(minioService.download(id));
    }
}
