package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.user_service.entity.TokenEntity;
import org.user_service.repository.TokenRepository;

import static org.user_service.domain.jooq.tables.Token.TOKEN;

@Repository
@AllArgsConstructor
@Slf4j
public class TokenRepositoryImpl implements TokenRepository {

    private final DSLContext dsl;

    @Override
    public void insert(TokenEntity entity) {
        try {
            dsl.insertInto(TOKEN)
                    .set(dsl.newRecord(TOKEN, entity))
                    .execute();
        } catch (Exception e) {
            log.error("Error inserting refresh with user id: [{}]", entity.getUserId());
            throw new DataAccessException("Error inserting refresh with user id: [%s]".formatted(entity.getUserId()));
        }
    }
}
