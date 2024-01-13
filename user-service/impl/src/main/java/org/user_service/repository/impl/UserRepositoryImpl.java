package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.user_service.entity.UserEntity;
import org.user_service.repository.UserRepository;

import java.util.Optional;

import static org.user_service.domain.jooq.tables.User.USER;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    @Override
    public void insert(UserEntity entity) {
        try {
            dsl.insertInto(USER)
                    .set(dsl.newRecord(USER, entity))
                    .execute();
        } catch (Exception e) {
            log.error("Error inserting user with email: [{}]", entity.getEmail());
            throw new DataAccessException("Error inserting user with email: [%s]".formatted(entity.getEmail()));
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOptionalInto(UserEntity.class);
    }
}
