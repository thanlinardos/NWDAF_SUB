package io.nwdaf.eventsubscription.repository.redis.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import lombok.Getter;
import lombok.Setter;

@RedisHash("NnwdafEventsSubscription")
@Getter @Setter
public class NnwdafEventsSubscriptionCached implements Serializable {
    @Id
    private String id;

    private NnwdafEventsSubscription sub;

    public String getId() {
        if(id==null && this.sub!=null) {
            this.id = ParserUtil.safeParseString(this.sub.getId());
        }
        return id;
    }

    public void setId(String id) {
        if(this.sub!=null && ParserUtil.safeParseLong(id)!=null) {
            this.id = id;
            this.sub.setId(ParserUtil.safeParseLong(id));
        }
    }

    public void setSub(NnwdafEventsSubscription sub) {
        this.sub = sub;
        if(sub!=null) {
            this.id = ParserUtil.safeParseString(this.sub.getId());
        }
    }

    public String toString() {
        return ParserUtil.safeParseString(this.sub);
    }
}