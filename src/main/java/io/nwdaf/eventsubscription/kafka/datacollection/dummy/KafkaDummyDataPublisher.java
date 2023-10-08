package io.nwdaf.eventsubscription.kafka.datacollection.dummy;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaDummyDataPublisher {
    private final ApplicationEventPublisher eventPublisher;

	KafkaDummyDataPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishDataCollection(final String message) {
        final KafkaDummyDataEvent e = new KafkaDummyDataEvent(this, message);
        eventPublisher.publishEvent(e);
    }
}
