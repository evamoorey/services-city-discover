package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.entity.KittyEntity;
import org.city_discover.repository.KittyRepository;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static org.city_discover.domain.jooq.tables.Kitty.KITTY;

@Repository
@AllArgsConstructor
@Slf4j
public class KittyRepositoryImpl implements KittyRepository {

    private final DSLContext dsl;

    private final Double DIF_LONG = 0.00138889;
    private final Double DIF_LAT = 0.00239667;

    @Override
    public KittyEntity insert(KittyEntity entity) {
        return dsl.insertInto(KITTY)
                .set(dsl.newRecord(KITTY, entity))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> {
                    log.error("Error inserting place with name: [{}]", entity.getName());
                    return new DataAccessException("Ошибка при добавлении кота с именем: [%s]"
                            .formatted(entity.getName()));
                })
                .into(KittyEntity.class);
    }

    @Override
    public Page<KittyEntity> findNear300(Double latitude, Double longitude, Pageable pageable) {
        List<KittyEntity> data = dsl.selectFrom(KITTY)
                .where(KITTY.LATITUDE.between(latitude - DIF_LAT, latitude + DIF_LAT))
                .and(KITTY.LONGITUDE.between(longitude - DIF_LONG, longitude + DIF_LONG))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(KittyEntity.class);

        int total = dsl.fetchCount(dsl.selectFrom(KITTY)
                .where(KITTY.LATITUDE.eq(latitude))
                .and(KITTY.LONGITUDE.eq(longitude)));
        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public void delete(UUID kitty) {
        dsl.deleteFrom(KITTY)
                .where(KITTY.ID.eq(kitty))
                .execute();
    }
}
