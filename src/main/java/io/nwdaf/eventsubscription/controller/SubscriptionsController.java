package io.nwdaf.eventsubscription.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RestController
public class SubscriptionsController implements SubscriptionsApi{
	SubscriptionsService subscriptionService = new SubscriptionsService();
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(
			@Valid NnwdafEventsSubscription body) {
		System.out.println(body.toString());
		subscriptionService.add(15, 2);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@Override
	public ResponseEntity<Void> deleteNWDAFEventsSubscription(String subscriptionId) {
		// TODO Auto-generated method stub
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
			@Valid NnwdafEventsSubscription body) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
