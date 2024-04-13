package org.city_discover.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    String login;
    String password;
    String minioUrl;
    String bucketName;
    String baseFolder;
}
