package org.city_discover.service;

import org.city_discover.dto.KittyCreateDto;
import org.city_discover.dto.KittyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface KittyService {
    KittyDto create(KittyCreateDto dto);

    Page<KittyDto> findNear(Double latitude, Double longitude, Pageable pageable);

    void delete(UUID kitty);
}
