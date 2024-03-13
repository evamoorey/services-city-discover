package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.PlaceCardDto;
import org.city_discover.entity.PlaceEntity;
import org.city_discover.repository.PlaceRepository;
import org.city_discover.service.PlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PlaceCardDto create(PlaceCardDto placeCardDto) {
        PlaceEntity entity = modelMapper.map(placeCardDto, PlaceEntity.class);


        PlaceEntity created = placeRepository.insert(entity);
        return modelMapper.map(created, PlaceCardDto.class);
    }
}
