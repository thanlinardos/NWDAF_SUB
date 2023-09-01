package io.nwdaf.eventsubscription.notify;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DummyDataProducerPublisher {
	private final ApplicationEventPublisher eventPublisher;

	DummyDataProducerPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishDataCollection(final String message) {
        final DummyDataProducerEvent e = new DummyDataProducerEvent(this, message);
        eventPublisher.publishEvent(e);
    }
}