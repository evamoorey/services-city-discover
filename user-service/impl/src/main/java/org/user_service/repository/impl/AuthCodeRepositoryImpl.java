package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.user_service.entity.AuthCodeEntity;
import org.user_service.repository.AuthCodeRepository;

import java.util.Optional;

import static org.user_service.domain.jooq.tables.AuthCode.AUTH_CODE;

@Repository
@AllArgsConstructor
@Slf4j
public class AuthCodeRepositoryImpl implements AuthCodeRepository {

    private final DSLContext dsl;

    @Override
    public void insert(AuthCodeEntity entity) {
        try {
            dsl.insertInto(AUTH_CODE)
                    .set(dsl.newRecord(AUTH_CODE, entity))
                    .execute();
        } catch (Exception e) {
            log.error("Error inserting code with email: [{}]", entity.getEmail());
            throw new DataAccessException("Error inserting code with email: [%s]".formatted(entity.getEmail()));
        }
    }

    @Override
    public Optional<AuthCodeEntity> findByEmail(String email) {
        return dsl.selectFrom(AUTH_CODE)
                .where(AUTH_CODE.EMAIL.eq(email))
                .orderBy(AUTH_CODE.CREATION_DATE.desc())
                .fetchOptionalInto(AuthCodeEntity.class);
    }

    @Override
    public void deleteAllCodes(String email) {
        try {
            dsl.deleteFrom(AUTH_CODE)
                    .where(AUTH_CODE.EMAIL.eq(email))
                    .execute();
        } catch (Exception e) {
            log.error("Error delete codes for user with email: [{}]", email);
            throw new DataAccessException("Error delete codes for user with email: [%s]".formatted(email));
        }
    }
}
