package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.user_service.entity.SubscriptionEntity;
import org.user_service.repository.SubscriptionRepository;

import java.util.Optional;
import java.util.UUID;

import static org.user_service.domain.jooq.tables.Subscription.SUBSCRIPTION;

@Repository
@AllArgsConstructor
@Slf4j
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final DSLContext dsl;

    @Override
    public SubscriptionEntity insert(SubscriptionEntity entity) {
        return dsl.insertInto(SUBSCRIPTION)
                .set(SUBSCRIPTION.SUBSCRIBER_ID, entity.getSubscriber())
                .set(SUBSCRIPTION.PUBLISHER_ID, entity.getPublisher())
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting user subscription: [%s]"
                        .formatted(entity.getSubscriber())))
                .into(SubscriptionEntity.class);
    }

    @Override
    public Optional<SubscriptionEntity> findBy(UUID subscriber, UUID publisher) {
        return dsl.selectFrom(SUBSCRIPTION)
                .where(SUBSCRIPTION.SUBSCRIBER_ID.eq(subscriber))
                .and(SUBSCRIPTION.PUBLISHER_ID.eq(publisher))
                .fetchOptionalInto(SubscriptionEntity.class);
    }

    @Override
    public void deleteBy(UUID subscriber, UUID publisher) {
        dsl.deleteFrom(SUBSCRIPTION)
                .where(SUBSCRIPTION.SUBSCRIBER_ID.eq(subscriber))
                .and(SUBSCRIPTION.PUBLISHER_ID.eq(publisher))
                .execute();
    }
}
