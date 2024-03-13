package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.domain.jooq.tables.User;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.city_discover.entity.UserEntity;
import org.city_discover.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    @Override
    public UserEntity insert(UserEntity entity) {
        return dsl.insertInto(User.USER)
                .set(dsl.newRecord(User.USER, entity))
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
        return dsl.update(User.USER)
                .set(dsl.newRecord(User.USER, entity))
                .where(User.USER.ID.eq(entity.getId()))
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
        return dsl.selectFrom(User.USER)
                .where(User.USER.EMAIL.eq(email))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return dsl.selectFrom(User.USER)
                .where(User.USER.ID.eq(id))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return dsl.selectFrom(User.USER)
                .where(User.USER.USERNAME.eq(username))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public void delete(UUID id) {
        dsl.deleteFrom(User.USER)
                .where(User.USER.ID.eq(id))
                .execute();
    }
}
