package org.city_discover.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.city_discover.exception.MinioAccessException;
import org.city_discover.properties.MinioProperties;
import org.city_discover.service.MinioAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class MinioAdapterImpl implements MinioAdapter {
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
            throw new MinioAccessException("Ошибка сохранения файла" );
        }
    }

    @Override
    public byte[] download(UUID id) {
        try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(properties.getBucketName())
                .object(id.toString())
                .build())) {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            throw new MinioAccessException("Ошибка загрузки файла" );
        }
    }
}
