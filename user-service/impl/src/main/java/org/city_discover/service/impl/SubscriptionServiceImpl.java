package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.SubscriptionDto;
import org.city_discover.entity.SubscriptionEntity;
import org.city_discover.exception.UnprocessableActionException;
import org.city_discover.repository.SubscriptionRepository;
import org.city_discover.service.SubscriptionService;
import org.city_discover.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Override
    public void subscribe(UUID subscriber, UUID publisher) {
        userService.findBy(subscriber);
        userService.findBy(publisher);

        Optional<SubscriptionEntity> subscription = subscriptionRepository.findBy(subscriber, publisher);
        if (subscription.isPresent()) {
            log.error("User [{}] already subscribed on [{}]", subscriber, publisher);
            throw new UnprocessableActionException("Подписка на пользователя уже есть");
        }

        SubscriptionEntity entity = SubscriptionEntity.builder()
                .subscriber(subscriber)
                .publisher(publisher)
                .build();

        subscriptionRepository.insert(entity);
    }

    @Override
    public void unsubscribe(UUID subscriber, UUID publisher) {
        userService.findBy(subscriber);
        userService.findBy(publisher);

        subscriptionRepository.findBy(subscriber, publisher).orElseThrow(() -> {
            log.error("User [{}] not subscribed on [{}]", subscriber, publisher);
            return new UnprocessableActionException("Нет подписки на пользователя");
        });

        subscriptionRepository.deleteBy(subscriber, publisher);
    }

    @Override
    public Page<SubscriptionDto> findSubscribers(UUID id, Pageable pageable) {
        userService.findBy(id);

        return subscriptionRepository.findSubscribers(id, pageable);
    }

    @Override
    public Page<SubscriptionDto> findSubscriptions(UUID id, Pageable pageable) {
        userService.findBy(id);

        return subscriptionRepository.findSubscriptions(id, pageable);
    }

}
