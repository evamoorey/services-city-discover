package org.city_discover.service;

import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PlaceService {
    PlaceCardDto create(UUID userId, PlaceCardUserDto placeCardDto);

    PlaceCardDto findById(UUID id);

    Page<PlaceCardDto> findByUserId(UUID user, Pageable pageable);
}
