package io.nwdaf.eventsubscription.repository.redis;

import io.nwdaf.eventsubscription.repository.redis.entities.NnwdafEventsSubscriptionNotificationCached;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("redisNotification")
@EntityScan("io.nwdaf.eventsubscription.repository.redis")
public interface RedisNotificationRepository  extends JpaRepository<NnwdafEventsSubscriptionNotificationCached,String> {
    
}