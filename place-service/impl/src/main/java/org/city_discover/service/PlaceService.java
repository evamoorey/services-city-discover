package org.city_discover.service;

import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardUserDto;

import java.util.UUID;

public interface PlaceService {
    PlaceCardDto create(PlaceCardUserDto placeCardDto);

    PlaceCardDto findById(UUID id);
}
