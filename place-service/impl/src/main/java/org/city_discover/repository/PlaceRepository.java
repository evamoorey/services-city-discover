package org.city_discover.repository;

import org.city_discover.entity.PlaceEntity;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository {
    PlaceEntity insert(PlaceEntity entity);

    Optional<PlaceEntity> findById(UUID id);

    Optional<PlaceEntity> findByName(String name);
}
