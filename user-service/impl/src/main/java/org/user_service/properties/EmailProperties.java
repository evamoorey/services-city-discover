package org.user_service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private String port;
    private String protocol;

    @Bean
    @Primary
    public Properties emailConfig() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.protocol", protocol);
//        prop.put("mail.debug", "true");
        prop.put("mail.smtp.ssl.enable", "true");

        return prop;
    }
}
