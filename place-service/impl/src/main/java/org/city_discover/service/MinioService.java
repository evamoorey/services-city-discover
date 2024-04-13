package org.city_discover.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MinioService {

    UUID save(MultipartFile file);

    Mono<InputStreamResource> download(UUID id);
}
