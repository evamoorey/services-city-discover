package org.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.user_service.service.SubscriptionService;

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
}
