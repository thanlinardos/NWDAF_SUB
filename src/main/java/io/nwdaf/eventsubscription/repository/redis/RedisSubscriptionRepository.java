package io.nwdaf.eventsubscription.repository.redis;

import io.nwdaf.eventsubscription.repository.redis.entities.NnwdafEventsSubscriptionCached;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("redisSubscription")
@EntityScan("io.nwdaf.eventsubscription.repository.redis")
public interface RedisSubscriptionRepository  extends JpaRepository<NnwdafEventsSubscriptionCached,String> {
    
}