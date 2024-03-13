package org.city_discover.service;


import org.city_discover.dto.SubscriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SubscriptionService {
    void subscribe(UUID subscriber, UUID publisher);

    void unsubscribe(UUID subscriber, UUID publisher);

    Page<SubscriptionDto> findSubscribers(UUID id, Pageable pageable);

    Page<SubscriptionDto> findSubscriptions(UUID id, Pageable pageable);
}
