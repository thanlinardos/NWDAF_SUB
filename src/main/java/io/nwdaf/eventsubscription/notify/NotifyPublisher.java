package io.nwdaf.eventsubscription.notify;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.notifier", havingValue = "true")
@Component
public class NotifyPublisher {

    private final ApplicationEventPublisher eventPublisher;

    NotifyPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishNotification(final String message, Long subId) {
        final NotificationEvent event = new NotificationEvent(this, message, subId);
        eventPublisher.publishEvent(event);
    }

}
