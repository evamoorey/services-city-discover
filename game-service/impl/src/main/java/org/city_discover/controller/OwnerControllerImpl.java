package org.city_discover.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.KittyDto;
import org.city_discover.service.OwnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class OwnerControllerImpl implements OwnerController {

    private final OwnerService ownerService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<Page<KittyDto>> findByOwnerId(UUID id, Pageable pageable) {
        Page<KittyDto> places = ownerService.findByOwnerId(id, pageable);
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> giveKitty(UUID kitty) {
        UUID user = UUID.fromString((String) request.getAttribute("id"));
        ownerService.giveKitty(user, kitty);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
