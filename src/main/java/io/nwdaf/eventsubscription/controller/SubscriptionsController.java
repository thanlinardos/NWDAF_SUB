package io.nwdaf.eventsubscription.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RestController
public class SubscriptionsController implements SubscriptionsApi{
	
	@Autowired
	private Environment env;
	
	@Autowired
	SubscriptionsService subscriptionService;
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(
			@Valid NnwdafEventsSubscription body) {
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
		//System.out.println(body.toString());
		System.out.println("Controller logic...");
		HttpHeaders responseHeaders = new HttpHeaders();
//		NnwdafEventsSubscription res = subscriptionService.create(body);
		NnwdafEventsSubscription res = body;
		Integer id = 0;
//		if(res!=null) {
//			id=res.getId();
//		}
		res.setId(id);
		System.out.println(res);
//		System.out.println(subscriptionService.findOneByNotifURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url")).toString());
		responseHeaders.set("Location",subsUri+"/"+id);
		ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(body);
		return response;
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
