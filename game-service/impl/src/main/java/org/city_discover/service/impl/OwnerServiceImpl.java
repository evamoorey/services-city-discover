package org.city_discover.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.dto.KittyDto;
import org.city_discover.entity.KittyEntity;
import org.city_discover.entity.OwnerEntity;
import org.city_discover.exception.UnprocessableActionException;
import org.city_discover.repository.OwnerRepository;
import org.city_discover.service.OwnerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void giveKitty(UUID user, UUID kitty) {
        Optional<KittyEntity> kittyEntity = repository.findByOwnerKitty(user, kitty);

        if (kittyEntity.isPresent()) {
            log.error("User [{}] already has a kitty [{}]", user, kitty);
            throw new UnprocessableActionException("Котенок уже есть в коллекции");
        }

        OwnerEntity entity = new OwnerEntity(user, kitty);
        repository.insert(entity);
    }

    @Override
    public Page<KittyDto> findByOwnerId(UUID user, Pageable pageable) {
        return repository.findByOwnerId(user, pageable)
                .map(kitty -> modelMapper.map(kitty, KittyDto.class));
    }
}