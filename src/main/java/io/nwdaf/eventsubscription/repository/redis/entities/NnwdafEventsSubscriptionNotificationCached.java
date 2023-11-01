package io.nwdaf.eventsubscription.repository.redis.entities;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.utilities.ParserUtil;

import lombok.Getter;
import lombok.Setter;

@RedisHash("NnwdafEventsSubscriptionNotification")
@Getter @Setter
public class NnwdafEventsSubscriptionNotificationCached implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();

    private NnwdafEventsSubscriptionNotification notification;

    public String toString() {
        return ParserUtil.safeParseString(this.notification);
    }
}