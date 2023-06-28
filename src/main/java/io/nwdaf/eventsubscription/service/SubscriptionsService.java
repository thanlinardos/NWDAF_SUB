package io.nwdaf.eventsubscription.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.SubscriptionRepository;

@Service
public class SubscriptionsService {
	
	private final SubscriptionRepository repository;
	
	@Autowired
	public SubscriptionsService(SubscriptionRepository repository) {
		this.repository = repository;
	}
	
	public NnwdafEventsSubscription create(NnwdafEventsSubscription body) {
		return repository.save(body);
	}
	
	public NnwdafEventsSubscription findOneByNotifURI(String clientURI) {
		return repository.findOneByNotificationURIEquals(clientURI);
	}
}
