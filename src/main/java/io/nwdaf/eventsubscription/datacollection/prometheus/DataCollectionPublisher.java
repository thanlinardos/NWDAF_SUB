package io.nwdaf.eventsubscription.datacollection.prometheus;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionPublisher {
	private final ApplicationEventPublisher eventPublisher;

	DataCollectionPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishDataCollection(final String message) {
        final DataCollectionEvent e = new DataCollectionEvent(this, message); 
        eventPublisher.publishEvent(e);
    }
}
