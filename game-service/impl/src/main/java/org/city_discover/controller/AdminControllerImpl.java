package org.city_discover.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.KittyCreateDto;
import org.city_discover.dto.KittyDto;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.city_discover.service.KittyService;
import org.city_discover.service.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.city_discover.utill.Converter.getErrorsMap;

@RestController
@AllArgsConstructor
@Slf4j
public class AdminControllerImpl implements AdminController {

    private final OwnerService ownerService;
    private final KittyService kittyService;

    @Override
    public ResponseEntity<?> createKitty(KittyCreateDto kittyCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        KittyDto dto = kittyService.create(kittyCreateDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> giveKitty(UUID id, UUID kitty) {
        ownerService.giveKitty(id, kitty);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> deleteUserKitty(UUID id, UUID kitty) {
        ownerService.deleteKitty(id, kitty);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> deleteKitty(UUID kitty) {
        ownerService.deleteKittyForAllUsers(kitty);
        kittyService.delete(kitty);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
