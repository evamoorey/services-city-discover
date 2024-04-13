package org.city_discover.repository;

import org.city_discover.entity.PlaceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository {
    PlaceEntity insert(PlaceEntity entity);

    Optional<PlaceEntity> findById(UUID id);

    Optional<PlaceEntity> findByName(String name);

    Page<PlaceEntity> findByUserId(UUID user, Pageable pageable);

    PlaceEntity update(PlaceEntity entity);
}
