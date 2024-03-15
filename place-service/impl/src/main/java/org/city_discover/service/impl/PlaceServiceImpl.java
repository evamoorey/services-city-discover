package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardUserDto;
import org.city_discover.entity.PlaceEntity;
import org.city_discover.exception.NoSuchEntityException;
import org.city_discover.exception.UnprocessableActionException;
import org.city_discover.repository.PlaceRepository;
import org.city_discover.service.PlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PlaceCardDto create(PlaceCardUserDto placeCardDto) {
        Optional<PlaceEntity> place = placeRepository.findByName(placeCardDto.getName());

        if (place.isPresent()) {
            log.error("Place already exists with name: [{}]", placeCardDto.getName());
            throw new UnprocessableActionException("Место с названием %s уже существует."
                    .formatted(placeCardDto.getName()));
        }

        PlaceEntity entity = modelMapper.map(placeCardDto, PlaceEntity.class);
        PlaceEntity created = placeRepository.insert(entity);
        return modelMapper.map(created, PlaceCardDto.class);
    }

    @Override
    public PlaceCardDto findById(UUID id) {
        PlaceEntity place = placeRepository.findById(id).orElseThrow(() -> {
            log.error("Place don't exists with id: [{}]", id);
            return new NoSuchEntityException("Место не существует.");
        });

        return modelMapper.map(place, PlaceCardDto.class);
    }
}
