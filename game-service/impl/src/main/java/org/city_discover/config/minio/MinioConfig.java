package org.city_discover.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.city_discover.exception.MinioAccessException;
import org.city_discover.properties.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES)
                    .build();

            MinioClient client = MinioClient.builder()
                    .endpoint(properties.getMinioUrl())
                    .httpClient(httpClient)
                    .credentials(properties.getLogin(), properties.getPassword())
                    .build();

            if (!client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build())) {
                client.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(properties.getBucketName())
                                .build());
            }
            return client;
        } catch (Exception e) {
            throw new MinioAccessException("Ошибка создания клиента MiniO");
        }
    }
}
