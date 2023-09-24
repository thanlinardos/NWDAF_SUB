package io.nwdaf.eventsubscription.kafka.datacollection.prometheus;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaDataCollectionPublisher {
	private final ApplicationEventPublisher eventPublisher;

	KafkaDataCollectionPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishDataCollection(final String message) {
        final KafkaDataCollectionEvent e = new KafkaDataCollectionEvent(this, message); 
        eventPublisher.publishEvent(e);
    }
}
