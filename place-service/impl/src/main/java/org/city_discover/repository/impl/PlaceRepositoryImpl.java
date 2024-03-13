package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.entity.PlaceEntity;
import org.city_discover.repository.PlaceRepository;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;

import static org.city_discover.domain.jooq.tables.Place.PLACE;

@Repository
@AllArgsConstructor
@Slf4j
public class PlaceRepositoryImpl implements PlaceRepository {

    private final DSLContext dsl;

    @Override
    public PlaceEntity insert(PlaceEntity entity) {
        return dsl.insertInto(PLACE)
                .set(dsl.newRecord(PLACE, entity))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> {
                    log.error("Error inserting place with name: [{}]", entity.getName());
                    return new DataAccessException("Ошибка при добавлении места с названием: [%s]"
                            .formatted(entity.getName()));
                })
                .into(PlaceEntity.class);
    }
}
