package org.user_service.repository;

import org.user_service.entity.SubscriptionEntity;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {
    SubscriptionEntity insert(SubscriptionEntity entity);

    Optional<SubscriptionEntity> findBy(UUID subscriber, UUID publisher);

    void deleteBy(UUID subscriber, UUID publisher);
}
