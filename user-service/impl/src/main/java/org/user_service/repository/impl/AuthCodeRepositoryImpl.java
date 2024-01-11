package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.user_service.entity.AuthCodeEntity;
import org.user_service.exception.NoSuchEntityException;
import org.user_service.repository.AuthCodeRepository;

import static org.user_service.domain.jooq.tables.AuthCode.AUTH_CODE;

@Repository
@AllArgsConstructor
public class AuthCodeRepositoryImpl implements AuthCodeRepository {

    private final DSLContext dsl;

    @Override
    public AuthCodeEntity insert(AuthCodeEntity entity) {
        return dsl.insertInto(AUTH_CODE)
                .set(dsl.newRecord(AUTH_CODE, entity))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException("Error inserting code with email: [%s]".formatted(entity.getEmail())))
                .into(AuthCodeEntity.class);
    }

    @Override
    public AuthCodeEntity findByEmail(String email) {
        return dsl.selectFrom(AUTH_CODE)
                .where(AUTH_CODE.EMAIL.eq(email))
                .orderBy(AUTH_CODE.CREATION_DATE.desc())
                .fetchOptional()
                .orElseThrow(() -> new NoSuchEntityException("No such codes for email: [%s]".formatted(email)))
                .into(AuthCodeEntity.class);
    }

    @Override
    public void delete(AuthCodeEntity entity) {
        dsl.deleteFrom(AUTH_CODE)
                .where(AUTH_CODE.EMAIL.eq(entity.getEmail()))
                .and(AUTH_CODE.CODE.eq(entity.getCode()))
                .execute();
    }
}
