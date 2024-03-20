package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import org.city_discover.dto.user.UserDto;
import org.city_discover.properties.RecommendationServiceProperties;
import org.city_discover.service.RecommendationExternalService;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RecommendationExternalServiceImpl implements RecommendationExternalService {
    private final RestTemplate restTemplate;
    private final RecommendationServiceProperties recommendationProperties;

    private void saveUser(UserDto userDto) {
        String fooResourceUrl = "http://" + recommendationProperties.getHost() + ":" +
                recommendationProperties.getPort() + "/submit_preferences";

        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        String response = restTemplate.postForObject(fooResourceUrl, request, String.class);
    }

}
