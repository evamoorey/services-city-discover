package org.city_discover.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.exception.MinioAccessException;
import org.city_discover.properties.MinioProperties;
import org.city_discover.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    private final MinioProperties properties;

    @Override
    @Transactional
    public UUID save(MultipartFile file) {
        UUID uuid = UUID.randomUUID();

        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(properties.getBucketName())
                            .object(uuid.toString())
                            .stream(file.getInputStream(), file.getSize(), 5242880)
                            .build()
            );
            return uuid;
        } catch (Exception e) {
            throw new MinioAccessException("Ошибка сохранения файла");
        }
    }

    @Override
    public Mono<InputStreamResource> download(UUID id) {
        return Mono.fromCallable(() -> {
            InputStream response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucketName())
                    .object(id.toString())
                    .build());
            return new InputStreamResource(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
