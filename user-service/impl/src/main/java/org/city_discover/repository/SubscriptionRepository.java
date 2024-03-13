package org.city_discover.repository;

import org.city_discover.dto.SubscriptionDto;
import org.city_discover.entity.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {
    SubscriptionEntity insert(SubscriptionEntity entity);

    Optional<SubscriptionEntity> findBy(UUID subscriber, UUID publisher);

    Page<SubscriptionDto> findSubscriptions(UUID id, Pageable pageable);

    Page<SubscriptionDto> findSubscribers(UUID id, Pageable pageable);

    void deleteBy(UUID subscriber, UUID publisher);
}
