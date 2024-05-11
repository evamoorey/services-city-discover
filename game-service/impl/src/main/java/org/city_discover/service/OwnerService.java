package org.city_discover.service;

import org.city_discover.dto.KittyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OwnerService {
    void giveKitty(UUID user, UUID kitty);

    Page<KittyDto> findByOwnerId(UUID user, Pageable pageable);

    void deleteKitty(UUID user, UUID kitty);

    void deleteKittyForAllUsers(UUID kitty);
}
