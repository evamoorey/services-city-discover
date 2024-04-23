package org.city_discover.repository;

import org.city_discover.entity.KittyEntity;
import org.city_discover.entity.OwnerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OwnerRepository {
    OwnerEntity insert(OwnerEntity entity);

    Page<KittyEntity> findByOwnerId(UUID user, Pageable pageable);
}
