package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.user_service.entity.SubscriptionEntity;
import org.user_service.exception.UnprocessableActionException;
import org.user_service.repository.SubscriptionRepository;
import org.user_service.service.SubscriptionService;
import org.user_service.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public void subscribe(UUID subscriber, UUID publisher) {
        Optional<SubscriptionEntity> subscription = subscriptionRepository.findBy(subscriber, publisher);
        if (subscription.isPresent()) {
            log.error("User [{}] already subscribed on [{}]", subscriber, publisher);
            throw new UnprocessableActionException("User already subscribed");
        }

        userService.findBy(subscriber);
        userService.findBy(publisher);

        SubscriptionEntity entity = SubscriptionEntity.builder()
                .subscriber(subscriber)
                .publisher(publisher)
                .build();

        subscriptionRepository.insert(entity);
    }

    @Override
    public void unsubscribe(UUID subscriber, UUID publisher) {
        subscriptionRepository.findBy(subscriber, publisher).orElseThrow(() -> {
            log.error("User [{}] not subscribed on [{}]", subscriber, publisher);
            return new UnprocessableActionException("User not subscribed");
        });

        userService.findBy(subscriber);
        userService.findBy(publisher);

        subscriptionRepository.deleteBy(subscriber, publisher);
    }
}
