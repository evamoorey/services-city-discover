package org.city_discover.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.PlaceCardCreateDto;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardUpdateDto;
import org.city_discover.dto.wrapper.ErrorsMap;
import org.city_discover.service.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.city_discover.utill.Converter.getErrorsMap;

@RestController
@AllArgsConstructor
@Slf4j
public class PlaceControllerImpl implements PlaceController {

    private final PlaceService placeService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<?> create(PlaceCardCreateDto placeCardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        PlaceCardDto placeCard = placeService.create(userId, placeCardDto);

        return new ResponseEntity<>(placeCard, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlaceCardDto> findById(UUID id) {
        PlaceCardDto placeCard = placeService.findById(id);
        return new ResponseEntity<>(placeCard, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<PlaceCardDto>> findByUserId(UUID id, Pageable pageable) {
        Page<PlaceCardDto> places = placeService.findByUserId(id, pageable);
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(UUID id, PlaceCardUpdateDto placeCardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsMap errorsMap = getErrorsMap(bindingResult);
            return ResponseEntity.badRequest().body(errorsMap);
        }

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        PlaceCardDto placeCard = placeService.update(id, placeCardDto, userId);

        return new ResponseEntity<>(placeCard, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> delete(UUID id) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        placeService.delete(userId, id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> deleteAdmin(UUID id) {
        placeService.deleteAdmin(id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
