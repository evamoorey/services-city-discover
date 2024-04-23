package org.city_discover.service;

import org.city_discover.dto.KittyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KittyService {
    Page<KittyDto> findNear(Double latitude, Double longitude, Pageable pageable);
}
