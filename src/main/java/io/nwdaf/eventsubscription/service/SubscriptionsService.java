package io.nwdaf.eventsubscription.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;

@Service
public class SubscriptionsService {
	
	private final SubscriptionRepository repository;
	
	@Autowired
	public SubscriptionsService(SubscriptionRepository repository) {
		this.repository = repository;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	

	public NnwdafEventsSubscriptionTable create(NnwdafEventsSubscription body) {
		NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
		body_table.setSub(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		return repository.save(body_table);
	}
	
//	public NnwdafEventsSubscription findOneByNotifURI(String clientURI) {
//		return repository.findOneByNotificationURIEquals(clientURI);
//	}
}
