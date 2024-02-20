package org.user_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.user_service.dto.SubscriptionDto;
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
        userService.findBy(subscriber);
        userService.findBy(publisher);

        Optional<SubscriptionEntity> subscription = subscriptionRepository.findBy(subscriber, publisher);
        if (subscription.isPresent()) {
            log.error("User [{}] already subscribed on [{}]", subscriber, publisher);
            throw new UnprocessableActionException("User already subscribed");
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
            return new UnprocessableActionException("User not subscribed");
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
