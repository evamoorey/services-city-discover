package org.city_discover.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.SubscriptionDto;
import org.city_discover.service.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class SubscriptionControllerImpl implements SubscriptionController {

    private final HttpServletRequest request;
    private final SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<Boolean> subscribe(UUID id) {
        UUID subscriber = UUID.fromString((String) request.getAttribute("id"));
        subscriptionService.subscribe(subscriber, id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> unsubscribe(UUID id) {
        UUID subscriber = UUID.fromString((String) request.getAttribute("id"));
        subscriptionService.unsubscribe(subscriber, id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<SubscriptionDto>> findSubscribers(UUID id, Pageable pageable) {
        Page<SubscriptionDto> subscribers = subscriptionService.findSubscribers(id, pageable);

        return new ResponseEntity<>(subscribers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<SubscriptionDto>> findSubscriptions(UUID id, Pageable pageable) {
        Page<SubscriptionDto> subscribers = subscriptionService.findSubscriptions(id, pageable);

        return new ResponseEntity<>(subscribers, HttpStatus.OK);
    }
}