package io.nwdaf.eventsubscription.notify;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private final String message;
    private final Long callerId;

    public NotificationEvent(Object source, String msg, Long callerId) {
        super(source);
        this.message = msg;
        this.callerId = callerId;
    }
}
