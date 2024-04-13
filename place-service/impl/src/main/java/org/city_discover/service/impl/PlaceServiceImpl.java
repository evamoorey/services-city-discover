package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.PlaceCardCreateDto;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.dto.PlaceCardUpdateDto;
import org.city_discover.entity.PlaceEntity;
import org.city_discover.exception.NoSuchEntityException;
import org.city_discover.exception.UnprocessableActionException;
import org.city_discover.repository.PlaceRepository;
import org.city_discover.service.PlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PlaceCardDto create(UUID user, PlaceCardCreateDto placeCardDto) {
        Optional<PlaceEntity> place = placeRepository.findByName(placeCardDto.getName());

        if (place.isPresent()) {
            log.error("Place already exists with name: [{}]", placeCardDto.getName());
            throw new UnprocessableActionException("Место с названием %s уже существует."
                    .formatted(placeCardDto.getName()));
        }

        PlaceEntity entity = modelMapper.map(placeCardDto, PlaceEntity.class);
        entity.setAuthor(String.valueOf(user));

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

    @Override
    public Page<PlaceCardDto> findByUserId(UUID user, Pageable pageable) {
        return placeRepository.findByUserId(user, pageable)
                .map(place -> modelMapper.map(place, PlaceCardDto.class));
    }

    @Override
    @Transactional
    public PlaceCardDto update(PlaceCardUpdateDto dto, UUID user) {
        PlaceEntity place = placeRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    log.error("Place not exists with name: [{}]", dto.getName());
                    return new UnprocessableActionException("Место с названием %s не существует."
                            .formatted(dto.getName()));
                });

        if (!place.getAuthor().equals(user.toString())) {
            log.error("User with id [{}] not author for place [{}]", user, dto.getName());
            throw new UnprocessableActionException("Нет прав для редактирвоания места.");
        }

        PlaceEntity entity = modelMapper.map(dto, PlaceEntity.class);
        entity.setName(place.getName());
        entity.setCreationDate(place.getCreationDate());
        entity.setModificationDate(Instant.now());

        PlaceEntity updated = placeRepository.update(entity);
        return modelMapper.map(updated, PlaceCardDto.class);
    }
}
