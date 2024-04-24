package org.city_discover.repository;

import org.city_discover.entity.KittyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KittyRepository {
    KittyEntity insert(KittyEntity entity);

    Page<KittyEntity> findNear(Double latitude, Double longitude, Pageable pageable);
}