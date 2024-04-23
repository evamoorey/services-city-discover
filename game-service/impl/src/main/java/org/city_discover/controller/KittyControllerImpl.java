package org.city_discover.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.KittyDto;
import org.city_discover.service.KittyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class KittyControllerImpl implements KittyController {

    private final KittyService kittyService;

    @Override
    public ResponseEntity<Page<KittyDto>> findNear(Double latitude, Double longitude, Pageable pageable) {
        Page<KittyDto> page = kittyService.findNear(latitude, longitude, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
