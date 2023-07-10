package io.nwdaf.eventsubscription.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.EventNotification;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.repository.eventnotification.NnwdafNotificationTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RestController
public class SubscriptionsController implements SubscriptionsApi{
	
	@Autowired
	private Environment env;
	
	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	NotificationService notificationService;
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(
			@Valid NnwdafEventsSubscription body) {
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
		//System.out.println(body.toString());
		System.out.println("Controller logic...");
		HttpHeaders responseHeaders = new HttpHeaders();
		
		NnwdafEventsSubscriptionTable res = subscriptionService.create(body);

		Long id = 0l;
		if(res!=null) {
			Integer repetionPeriod = null;
			NotificationMethodEnum notificationMethod = null;
			Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
			Map<Integer,Integer> eventIndexToRepPeriodMap = new HashMap<>();
			id=res.getId();
			if(body.getEvtReq()!=null) {
				if(body.getEvtReq().getNotifMethod()!=null) {
					notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
					if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
						repetionPeriod = body.getEvtReq().getRepPeriod();
					}
				}
			}
			Integer no_events = 0;
			//get period and notification method for each event
			if(body.getEventSubscriptions()!=null) {
				no_events = body.getEventSubscriptions().size();
				for(int i=0;i<body.getEventSubscriptions().size();i++) {
					EventSubscription e = body.getEventSubscriptions().get(i);
					if(e!=null) {
						if(notificationMethod==null) {
							if(e.getNotificationMethod()!=null) {
								eventIndexToNotifMethodMap.put(i, e.getNotificationMethod().getNotifMethod());
							}
							else {
								eventIndexToNotifMethodMap.put(i,NotificationMethodEnum.THRESHOLD);
							}
							
						}
						else {
							eventIndexToNotifMethodMap.put(i, notificationMethod);
						}
						
						if(eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
							if(repetionPeriod==null) {
								eventIndexToRepPeriodMap.put(i,e.getRepetitionPeriod());
							}
							else{
								eventIndexToRepPeriodMap.put(i,repetionPeriod);
							}
							
						}
						
						}
					else {
						no_events--;
					}
						
					
				}
			}
			//check if period is valid (between 1sec and 10mins) and if not change it to the corresponding boundary
			for(int i=0;i<no_events;i++) {
				if(eventIndexToRepPeriodMap.get(i)!=null && eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
					if(eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS) {
						eventIndexToRepPeriodMap.put(i, Constants.MIN_PERIOD_SECONDS);
					}
					if(eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS) {
						eventIndexToRepPeriodMap.put(i, Constants.MAX_PERIOD_SECONDS);
					}
				 
					NnwdafEventsSubscriptionNotification notification = new NnwdafEventsSubscriptionNotification();
					notification.subscriptionId(id.toString()).addEventNotificationsItem(new EventNotification().addNfLoadLevelInfosItem(new NfLoadLevelInformation().nfCpuUsage(100)));
					notificationService.create(notification);
				}
				System.out.println("notifMethod="+eventIndexToNotifMethodMap.get(i)+", repPeriod="+eventIndexToRepPeriodMap.get(i));
			}
			
		}

		System.out.println("id="+id);
		System.out.println(body);
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
