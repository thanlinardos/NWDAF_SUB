package io.nwdaf.eventsubscription.notify;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class NotifyPublisher {

    private final ApplicationEventPublisher eventPublisher;

    NotifyPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishNotification(Long subId) {
        eventPublisher.publishEvent(subId);
    }

}
