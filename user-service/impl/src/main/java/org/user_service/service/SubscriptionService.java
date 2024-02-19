package org.user_service.service;


import java.util.UUID;

public interface SubscriptionService {
    void subscribe(UUID subscriber, UUID publisher);

    void unsubscribe(UUID subscriber, UUID publisher);
}
