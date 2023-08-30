package io.nwdaf.eventsubscription.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NotificationUtil;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.ParserUtil;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.notify.DataCollectionListener;
import io.nwdaf.eventsubscription.notify.DataCollectionPublisher;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RestController
public class SubscriptionsController implements SubscriptionsApi{
	
	@Autowired
	private Environment env;
	
	@Autowired
	private NotifyPublisher notifyPublisher;
	
	@Autowired
	private DataCollectionPublisher dataCollectionPublisher;

	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	MetricsService metricsService;
	
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(@Valid NnwdafEventsSubscription body) {
		Logger logger = NwdafSubApplication.getLogger();
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
		System.out.println("Controller logic...");
		HttpHeaders responseHeaders = new HttpHeaders();
		Integer repetionPeriod = null;
		NotificationMethodEnum notificationMethod = null;
		Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
		Map<Integer,Integer> eventIndexToRepPeriodMap = new HashMap<>();
		List<Boolean> canServeSubscription = new ArrayList<>();
		Boolean muted=false;
		Boolean immRep=false;
		Integer count_of_notif = 0;
		Long id = 0l;
		List<Integer> periods_to_serve = new ArrayList<>();
		List<Integer> negotiatedFeaturesList = new ArrayList<>();
		if(body==null) {
			ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeaders).body(body);
			return response;
		}
		
		if(body.getSupportedFeatures()!=null) {
			if(!Constants.supportedFeatures.equals(body.getSupportedFeatures())) {
				negotiatedFeaturesList = ParserUtil.convertFeaturesToList(body.getSupportedFeatures());
			}
		}
		else {
			body.setSupportedFeatures(Constants.supportedFeatures);
			negotiatedFeaturesList = Constants.supportedFeaturesList;
		}
		
		if(body.getEvtReq()!=null) {
			// get global notification method and period if they exist
			if(body.getEvtReq().getNotifMethod()!=null) {
				notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
				if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetionPeriod = body.getEvtReq().getRepPeriod();
				}
				if(body.getEvtReq().getNotifFlag()!=null) {
					muted=body.getEvtReq().getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE);
				}
			}
			if(body.getEvtReq().isImmRep()!=null) {
				immRep=body.getEvtReq().isImmRep();
			}
			
		}
		Integer no_valid_events = 0;
		List<Integer> invalid_events = new ArrayList<>();
		//get period and notification method for each event
		if(body.getEventSubscriptions()!=null) {
			no_valid_events = body.getEventSubscriptions().size();
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
					
					if(eventIndexToNotifMethodMap.get(i)!=null) {
						if(eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
							if(repetionPeriod==null) {
								eventIndexToRepPeriodMap.put(i,e.getRepetitionPeriod());
							}
							else{
								eventIndexToRepPeriodMap.put(i,repetionPeriod);
							}
								
						}
					}
				e = ParserUtil.setShapes(e);
				body.getEventSubscriptions().set(i, e);
				}
				else {
					no_valid_events--;
					invalid_events.add(i);
				}				
			}
			for(int i=0;i<invalid_events.size();i++){
				body.getEventSubscriptions().remove((int)invalid_events.get(i));
			}
		}
		
		// check which subscriptions can be served
		for(int i=0;i<no_valid_events;i++) {
			canServeSubscription.add(false);
		}
		for(int i=0;i<no_valid_events;i++) {
			EventSubscription event = body.getEventSubscriptions().get(i);
			NwdafEventEnum eType = event.getEvent().getEvent();
			
			if(eventIndexToRepPeriodMap.get(i)!=null && eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
				Boolean failed_notif = false;
				NwdafFailureCodeEnum failCode = null;
				//check if eventType is supported
				if(!Constants.supportedEvents.contains(eType)) {
					failed_notif=true;
					failCode=NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
				//check if period is valid (between 1sec and 10mins) and if not add failureReport
				if(eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS||eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS) {
					failed_notif = true;
					failCode = NwdafFailureCodeEnum.OTHER;
				}
				//check if for this event subscription the requested area of interest 
				// is inside (or equals to) the service area of this NWDAF instance
				if(event.getNetworkArea()!=null){
					if(!Constants.ServingAreaOfInterest.containsArea(event.getNetworkArea())){
						failed_notif = true;
						failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
					}
				}
				//check whether data is available to be gathered
				NotificationBuilder notifBuilder = new NotificationBuilder();
				NnwdafEventsSubscriptionNotification notification = notifBuilder.initNotification(id);
				try {
					// check if it needs to wake up data collector
					if(DataCollectionListener.getNo_dataCollectionEventListeners()==0){
						dataCollectionPublisher.publishDataCollection("");
						//wait for data collection to start
						Thread.sleep(50);
						while((!DataCollectionListener.getStarted_saving_data()) && DataCollectionListener.getNo_dataCollectionEventListeners()>0){
							Thread.sleep(50);
						}
						Thread.sleep(50);
					}
					notification=NotificationUtil.getNotification(body, i, notification, metricsService);
				} catch (JsonProcessingException e) {
					failed_notif=true;
					logger.error("Failed to collect data for event: "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				} catch(InterruptedException e){
					logger.error("Thread failed to wait for datacollection to start for event: "+eType,e);
				} catch(Exception e) {
					failed_notif=true;
					logger.error("Failed to collect data for event(couldnt connect to timescaledb): "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
				if(notification==null) {
					logger.error("Failed to collect data for event: "+eType);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
				// add failureEventInfo
				if(notification==null || failed_notif) {
    				body.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(failCode)));
				}
				else {
					canServeSubscription.set(i, true);
					if(immRep) {
						body.addEventNotificationsItem(notification.getEventNotifications().get(0));
					}
				}
				logger.info("notifMethod="+eventIndexToNotifMethodMap.get(i)+", repPeriod="+eventIndexToRepPeriodMap.get(i));
				}
		}
		//check the amount of subscriptions that need to be notifed
		for(int i=0;i<canServeSubscription.size();i++) {
			if(canServeSubscription.get(i)) {
				count_of_notif++;
				periods_to_serve.add(eventIndexToRepPeriodMap.get(i));
			}
		}
		//if no subscriptions need notifying mute the subscription
		if(count_of_notif==0) {
			if(body.getEvtReq()==null) {
				body.setEvtReq(new ReportingInformation());
			}
			body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE));
		}
		//save sub to db and get back the created id
		NnwdafEventsSubscriptionTable res = null;
		try {
			res = subscriptionService.create(body);
		}catch(Exception e) {
			logger.error("Couldn't save sub to db",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(responseHeaders).body(body);
		}
		if(res==null) {
			logger.error("Couldn't save sub to db (service returned null)");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(responseHeaders).body(body);
		}
		
		id = res.getId();
		body.setId(id);
		String params="";
		//notify about new saved subscription and start collecting data
		if(count_of_notif>0 && !muted) {
			dataCollectionPublisher.publishDataCollection(params);
			notifyPublisher.publishNotification(id);
		}

		System.out.println("id="+id);
		System.out.println(body);
		//set the location header to the subscription uri
		subsUri+="/"+id;
		responseHeaders.set("Location",subsUri);
		ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(body);
		return response;
	}

	@Override
	public ResponseEntity<Void> deleteNWDAFEventsSubscription(String subscriptionId) {
		try{
			subscriptionService.delete(ParserUtil.safeParseLong(subscriptionId));
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Override
	public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
			@Valid NnwdafEventsSubscription body) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions"+"/"+subscriptionId;
		
		subscriptionService.update(ParserUtil.safeParseLong(subscriptionId),body);
		responseHeaders.set("Location",subsUri);
		return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(body);
	}
	
}
