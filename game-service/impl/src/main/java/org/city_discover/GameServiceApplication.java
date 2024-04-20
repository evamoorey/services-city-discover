package org.city_discover;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition
@EnableConfigurationProperties
public class GameServiceApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!" );
    }
}