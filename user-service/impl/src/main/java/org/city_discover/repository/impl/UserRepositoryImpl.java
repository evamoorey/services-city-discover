package org.city_discover.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.entity.UserEntity;
import org.city_discover.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.city_discover.domain.jooq.tables.User.USER;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    @Override
    public UserEntity insert(UserEntity entity) {
        return dsl.insertInto(USER)
                .set(dsl.newRecord(USER, entity))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> {
                    log.error("Error inserting user with email: [{}]", entity.getEmail());
                    return new DataAccessException("Ошибка при добавлении пользователя с email: [%s]"
                            .formatted(entity.getEmail()));
                })
                .into(UserEntity.class);
    }

    @Override
    public UserEntity update(UserEntity entity) {
        return dsl.update(USER)
                .set(dsl.newRecord(USER, entity))
                .where(USER.ID.eq(entity.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> {
                    log.error("Error updating user with email: [{}]", entity.getEmail());
                    return new DataAccessException("Ошибка при обновлении пользователя с email: [%s]."
                            .formatted(entity.getEmail()));
                })
                .into(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return dsl.selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return dsl.selectFrom(USER)
                .where(USER.USERNAME.eq(username))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public void delete(UUID id) {
        dsl.deleteFrom(USER)
                .where(USER.ID.eq(id))
                .execute();
    }
}
