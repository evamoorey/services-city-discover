package org.city_discover.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MinioAdapter {

    UUID save(MultipartFile file);

    byte[] download(UUID id);
}
