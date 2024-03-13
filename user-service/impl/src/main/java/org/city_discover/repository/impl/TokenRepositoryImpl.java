package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.domain.jooq.tables.Token;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.city_discover.entity.TokenEntity;
import org.city_discover.repository.TokenRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class TokenRepositoryImpl implements TokenRepository {

    private final DSLContext dsl;

    @Override
    public void insert(TokenEntity entity) {
        try {
            dsl.insertInto(Token.TOKEN)
                    .set(dsl.newRecord(Token.TOKEN, entity))
                    .execute();
        } catch (Exception e) {
            log.error("Error inserting refresh with user id: [{}]", entity.getUserId());
            throw new DataAccessException("Ошибка записи refresh токена");
        }
    }

    @Override
    public void deleteTokenByUserId(UUID userId) {
        try {
            dsl.deleteFrom(Token.TOKEN)
                    .where(Token.TOKEN.USER_ID.eq(userId))
                    .execute();
        } catch (Exception e) {
            log.error("Error delete tokens for user id: [{}]", userId);
            throw new DataAccessException("Ошибка при удалении токенов пользователя");
        }
    }

    @Override
    public Optional<TokenEntity> findById(UUID userId) {
        return dsl.selectFrom(Token.TOKEN)
                .where(Token.TOKEN.USER_ID.eq(userId))
                .fetchOptionalInto(TokenEntity.class);
    }
}
