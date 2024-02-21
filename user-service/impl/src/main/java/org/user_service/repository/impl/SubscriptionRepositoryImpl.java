package org.user_service.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.user_service.dto.SubscriptionDto;
import org.user_service.entity.SubscriptionEntity;
import org.user_service.repository.SubscriptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.user_service.domain.jooq.tables.Subscription.SUBSCRIPTION;
import static org.user_service.domain.jooq.tables.User.USER;

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
                .orElseThrow(() -> {
                    log.error("Error inserting user subscription: [{}]", entity.getSubscriber());
                    return new DataAccessException("Ошибка при подписке на другого пользователя");
                })
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
    public Page<SubscriptionDto> findSubscriptions(UUID id, Pageable pageable) {
        List<SubscriptionDto> data = dsl.select(
                        USER.ID,
                        USER.USERNAME)
                .from(SUBSCRIPTION)
                .join(USER)
                .on(SUBSCRIPTION.PUBLISHER_ID.eq(USER.ID))
                .where(SUBSCRIPTION.SUBSCRIBER_ID.eq(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(SubscriptionDto.class);

        int total = dsl.fetchCount(dsl.select(
                        USER.ID,
                        USER.USERNAME)
                .from(SUBSCRIPTION)
                .join(USER)
                .on(SUBSCRIPTION.PUBLISHER_ID.eq(USER.ID))
                .where(SUBSCRIPTION.SUBSCRIBER_ID.eq(id)));

        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public Page<SubscriptionDto> findSubscribers(UUID id, Pageable pageable) {
        List<SubscriptionDto> data = dsl.select(
                        USER.ID,
                        USER.USERNAME)
                .from(SUBSCRIPTION)
                .join(USER)
                .on(SUBSCRIPTION.SUBSCRIBER_ID.eq(USER.ID))
                .where(SUBSCRIPTION.PUBLISHER_ID.eq(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(SubscriptionDto.class);

        int total = dsl.fetchCount(dsl.select(USER.USERNAME)
                .from(SUBSCRIPTION)
                .join(USER)
                .on(SUBSCRIPTION.SUBSCRIBER_ID.eq(USER.ID))
                .where(SUBSCRIPTION.PUBLISHER_ID.eq(id)));

        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public void deleteBy(UUID subscriber, UUID publisher) {
        dsl.deleteFrom(SUBSCRIPTION)
                .where(SUBSCRIPTION.SUBSCRIBER_ID.eq(subscriber))
                .and(SUBSCRIPTION.PUBLISHER_ID.eq(publisher))
                .execute();
    }
}
