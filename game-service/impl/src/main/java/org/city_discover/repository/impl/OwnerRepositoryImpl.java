package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.entity.KittyEntity;
import org.city_discover.entity.OwnerEntity;
import org.city_discover.repository.OwnerRepository;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.city_discover.domain.jooq.tables.Kitty.KITTY;
import static org.city_discover.domain.jooq.tables.Owner.OWNER;

@Repository
@AllArgsConstructor
@Slf4j
public class OwnerRepositoryImpl implements OwnerRepository {

    private final DSLContext dsl;

    @Override
    public OwnerEntity insert(OwnerEntity entity) {
        return dsl.insertInto(OWNER)
                .set(OWNER.OWNER_ID, entity.getOwner())
                .set(OWNER.KITTY_ID, entity.getKitty())
                .returning()
                .fetchOptional()
                .orElseThrow(() -> {
                    log.error("Error inserting user subscription: [{}]", entity.getOwner());
                    return new DataAccessException("Ошибка при получении кота");
                })
                .into(OwnerEntity.class);
    }

    @Override
    public Page<KittyEntity> findByOwnerId(UUID user, Pageable pageable) {
        List<KittyEntity> data = dsl.select(
                        KITTY.ID,
                        KITTY.NAME,
                        KITTY.LATITUDE,
                        KITTY.LONGITUDE,
                        KITTY.PHOTO_ID
                )
                .from(KITTY)
                .join(OWNER)
                .on(OWNER.KITTY_ID.eq(KITTY.ID))
                .where(OWNER.OWNER_ID.eq(user))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(KittyEntity.class);

        int total = dsl.fetchCount(dsl.select(
                        KITTY.ID,
                        KITTY.NAME,
                        KITTY.LATITUDE,
                        KITTY.LONGITUDE,
                        KITTY.PHOTO_ID
                )
                .from(KITTY)
                .join(OWNER)
                .on(OWNER.KITTY_ID.eq(KITTY.ID))
                .where(OWNER.OWNER_ID.eq(user)));
        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public Optional<OwnerEntity> findOwnerKitty(UUID user, UUID kitty) {
        return dsl.selectFrom(OWNER)
                .where(OWNER.OWNER_ID.eq(user))
                .and(OWNER.KITTY_ID.eq(kitty))
                .fetchOptionalInto(OwnerEntity.class);
    }

    @Override
    public void delete(UUID user, UUID kitty) {
        dsl.deleteFrom(OWNER)
                .where(OWNER.OWNER_ID.eq(user))
                .and(OWNER.KITTY_ID.eq(kitty))
                .execute();
    }

    @Override
    public void deleteForAll(UUID kitty) {
        dsl.deleteFrom(OWNER)
                .where(OWNER.KITTY_ID.eq(kitty))
                .execute();
    }
}
