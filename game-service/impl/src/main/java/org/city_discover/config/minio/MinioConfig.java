package org.city_discover.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.city_discover.properties.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient generateMinioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
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
    }
}
