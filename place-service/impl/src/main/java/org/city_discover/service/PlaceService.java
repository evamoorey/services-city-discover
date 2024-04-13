package org.city_discover.service;

import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardCreateDto;
import org.city_discover.dto.PlaceCardUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PlaceService {
    PlaceCardDto create(UUID userId, PlaceCardCreateDto placeCardDto);

    PlaceCardDto findById(UUID id);

    Page<PlaceCardDto> findByUserId(UUID user, Pageable pageable);

    PlaceCardDto update(PlaceCardUpdateDto dto, UUID user);
}
