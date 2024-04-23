package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.KittyDto;
import org.city_discover.repository.KittyRepository;
import org.city_discover.service.KittyService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KittyServiceImpl implements KittyService {

    private final KittyRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public Page<KittyDto> findNear(Double latitude, Double longitude, Pageable pageable) {
        return repository.findNear(latitude, longitude, pageable)
                .map(kitty -> modelMapper.map(kitty, KittyDto.class));
    }
}
