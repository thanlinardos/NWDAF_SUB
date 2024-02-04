package io.nwdaf.eventsubscription.config;

import lombok.Getter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Component
@RefreshScope
@Getter
public class ValueRefreshConfigBean {

    public ValueRefreshConfigBean() {
    }
}
