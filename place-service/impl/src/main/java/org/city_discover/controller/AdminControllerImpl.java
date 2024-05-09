package org.city_discover.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.PlaceCardCreateDto;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.city_discover.service.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.city_discover.utill.Converter.getErrorsMap;

@RestController
@AllArgsConstructor
@Slf4j
public class AdminControllerImpl implements AdminController {

    private final PlaceService placeService;

    @Override
    public ResponseEntity<?> create(PlaceCardCreateDto placeCardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        PlaceCardDto placeCard = placeService.create("system", placeCardDto);

        return new ResponseEntity<>(placeCard, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> delete(UUID id) {
        placeService.deleteAdmin(id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
