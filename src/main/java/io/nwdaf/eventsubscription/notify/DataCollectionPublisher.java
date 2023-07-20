package io.nwdaf.eventsubscription.notify;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionPublisher {
	private final ApplicationEventPublisher eventPublisher;

	DataCollectionPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishDataCollection(String param) {
        eventPublisher.publishEvent(param);
    }
}
