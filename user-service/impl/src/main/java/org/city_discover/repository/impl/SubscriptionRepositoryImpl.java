package org.city_discover.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.city_discover.domain.jooq.tables.Subscription;
import org.city_discover.domain.jooq.tables.User;
import org.city_discover.dto.SubscriptionDto;
import org.city_discover.entity.SubscriptionEntity;
import org.city_discover.repository.SubscriptionRepository;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final DSLContext dsl;

    @Override
    public SubscriptionEntity insert(SubscriptionEntity entity) {
        return dsl.insertInto(Subscription.SUBSCRIPTION)
                .set(Subscription.SUBSCRIPTION.SUBSCRIBER_ID, entity.getSubscriber())
                .set(Subscription.SUBSCRIPTION.PUBLISHER_ID, entity.getPublisher())
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
        return dsl.selectFrom(Subscription.SUBSCRIPTION)
                .where(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(subscriber))
                .and(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(publisher))
                .fetchOptionalInto(SubscriptionEntity.class);
    }

    @Override
    public Page<SubscriptionDto> findSubscriptions(UUID id, Pageable pageable) {
        List<SubscriptionDto> data = dsl.select(
                        User.USER.ID,
                        User.USER.USERNAME)
                .from(Subscription.SUBSCRIPTION)
                .join(User.USER)
                .on(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(User.USER.ID))
                .where(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(SubscriptionDto.class);

        int total = dsl.fetchCount(dsl.select(
                        User.USER.ID,
                        User.USER.USERNAME)
                .from(Subscription.SUBSCRIPTION)
                .join(User.USER)
                .on(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(User.USER.ID))
                .where(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(id)));

        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public Page<SubscriptionDto> findSubscribers(UUID id, Pageable pageable) {
        List<SubscriptionDto> data = dsl.select(
                        User.USER.ID,
                        User.USER.USERNAME)
                .from(Subscription.SUBSCRIPTION)
                .join(User.USER)
                .on(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(User.USER.ID))
                .where(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(id))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(SubscriptionDto.class);

        int total = dsl.fetchCount(dsl.select(User.USER.USERNAME)
                .from(Subscription.SUBSCRIPTION)
                .join(User.USER)
                .on(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(User.USER.ID))
                .where(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(id)));

        return new PageImpl<>(data, pageable, total);
    }

    @Override
    public void deleteBy(UUID subscriber, UUID publisher) {
        dsl.deleteFrom(Subscription.SUBSCRIPTION)
                .where(Subscription.SUBSCRIPTION.SUBSCRIBER_ID.eq(subscriber))
                .and(Subscription.SUBSCRIPTION.PUBLISHER_ID.eq(publisher))
                .execute();
    }
}
