package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.city_discover.dto.UserExternalDto;
import org.city_discover.entity.UserEntity;
import org.city_discover.exception.ExternalServiceException;
import org.city_discover.properties.RecommendationServiceProperties;
import org.city_discover.service.RecommendationExternalService;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.city_discover.utill.EntityConverter.mapUserEntityToUserExternalDto;

@Service
@AllArgsConstructor
@Slf4j
public class RecommendationExternalServiceImpl implements RecommendationExternalService {
    private final RestTemplate restTemplate;
    private final RecommendationServiceProperties recommendationProperties;

    @Override
    public void saveUser(UserEntity entity) {
        UserExternalDto dto = mapUserEntityToUserExternalDto(entity);

        String resourceUrl = "http://" + recommendationProperties.getHost() + ":" +
                recommendationProperties.getPort() + "/submit_preferences";

        HttpEntity<UserExternalDto> request = new HttpEntity<>(dto);
        Response response = restTemplate.postForObject(resourceUrl, request, Response.class);

        if (response == null || response.getStatus() != 200) {
            log.error("Recommendation service error: {}", response);
            throw new ExternalServiceException("Сервис рекоммендаций временно недоступен");
        }
    }
}