package io.nwdaf.eventsubscription.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import io.nwdaf.eventsubscription.utilities.OtherUtil;
import io.nwdaf.eventsubscription.NotificationUtil;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.datacollection.DataCollectionListener;
import io.nwdaf.eventsubscription.datacollection.DataCollectionPublisher;
import io.nwdaf.eventsubscription.datacollection.DummyDataProducerListener;
import io.nwdaf.eventsubscription.datacollection.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
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
	private DummyDataProducerPublisher dummyDataProducerPublisher;

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
		if(body==null || !CheckUtil.safeCheckListNotEmpty(body.getEventSubscriptions())) {
			ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeaders).body(body);
			return response;
		}
		// negotiate the supported features with the client
		if(body.getSupportedFeatures()!=null && !body.getSupportedFeatures().equals("")) {
			if(!Constants.supportedFeatures.equals(body.getSupportedFeatures()) && 
				CheckUtil.listInside(Constants.supportedFeaturesList, ConvertUtil.convertFeaturesToList(body.getSupportedFeatures()))) {
				negotiatedFeaturesList = ConvertUtil.convertFeaturesToList(body.getSupportedFeatures());
			}
			else{
				body.setSupportedFeatures(Constants.supportedFeatures);
				negotiatedFeaturesList = Constants.supportedFeaturesList;
			}
		}
		else {
			body.setSupportedFeatures(Constants.supportedFeatures);
			negotiatedFeaturesList = Constants.supportedFeaturesList;
		}
		logger.info("negotiatedFeaturesList:" + negotiatedFeaturesList.toString());
		if(body.getEvtReq()!=null) {
			// get global notification method and period if they exist
			if(body.getEvtReq().getNotifMethod()!=null && body.getEvtReq().getNotifMethod().getNotifMethod()!=null) {
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
		no_valid_events = body.getEventSubscriptions().size();
		for(int i=0;i<body.getEventSubscriptions().size();i++) {
			EventSubscription e = body.getEventSubscriptions().get(i);
			if(e==null) {
				no_valid_events--;
				invalid_events.add(i);
				continue;
			}
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
			e = OtherUtil.setShapes(e);
			body.getEventSubscriptions().set(i, e);			
		}
		for(int i=0;i<invalid_events.size();i++){
			body.getEventSubscriptions().remove((int)invalid_events.get(i));
		}
		
		// check which subscriptions can be served
		for(int i=0;i<no_valid_events;i++) {
			canServeSubscription.add(false);
		}
		for(int i=0;i<no_valid_events;i++) {
			EventSubscription event = body.getEventSubscriptions().get(i);
			NwdafEventEnum eType = event.getEvent().getEvent();
			Boolean periodic = false;
			if(eventIndexToRepPeriodMap.get(i)!=null) {
				periodic = eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC);
			}
			Boolean failed_notif = false;
			NwdafFailureCodeEnum failCode = null;
			//check if eventType is supported
			if(!Constants.supportedEvents.contains(eType)) {
				failed_notif=true;
				failCode=NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			//check if period is valid (between 1sec and 10mins) and if not add failureReport
			if(periodic && (eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS || eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS)) {
				failed_notif = true;
				failCode = NwdafFailureCodeEnum.OTHER;
			}
			// check if for this event subscription the requested area of interest 
			// is inside (or equals to) the service area of this NWDAF instance
			// the checks are for when the serializer initializes the lists inside aoi object with null
			NetworkAreaInfo matchingArea = null;
			Boolean insideServiceArea = false;
			if(event.getNetworkArea().getId() != null){
				matchingArea = Constants.ExampleAOIsMap.get(event.getNetworkArea().getId());
				insideServiceArea = Constants.ServingAreaOfInterest.containsArea(matchingArea);
			}
			Constants.ExampleAOIsMap.get(event.getNetworkArea().getId());
			if(event.getNetworkArea()!=null && (CheckUtil.safeCheckNetworkAreaNotEmpty(event.getNetworkArea()) || event.getNetworkArea().getId() != null)){
				if(!(CheckUtil.safeCheckNetworkAreaNotEmpty(event.getNetworkArea()) && Constants.ServingAreaOfInterest.containsArea(event.getNetworkArea()))
					&& (matchingArea == null || !insideServiceArea)){
					failed_notif = true;
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
					System.out.println("not inside serving aoi");
				}
				else{
					// check if its lists equal one of the known AOIs -> set the id
					if(Constants.ExampleAOIsToUUIDsMap.containsKey(event.getNetworkArea())){
						event.getNetworkArea().id(Constants.ExampleAOIsToUUIDsMap.get(event.getNetworkArea()));
						System.out.println("same as aoi with id: "+event.getNetworkArea().getId());
					}
					// check if id equals to a known AOI -> set the area lists
					else if(event.getNetworkArea().getId() != null && Constants.ExampleAOIsMap.containsKey(event.getNetworkArea().getId())){
						NetworkAreaInfo matchingAOI = Constants.ExampleAOIsMap.get(event.getNetworkArea().getId());
						event.getNetworkArea().ecgis(matchingAOI.getEcgis()).ncgis(matchingAOI.getNcgis())
								.gRanNodeIds(matchingAOI.getGRanNodeIds()).tais(matchingAOI.getTais());
					}
					// if new AOI create id if it doesnt have one and add it to the known AOIs
					else{
						if(event.getNetworkArea().getId() == null){
							event.getNetworkArea().id(UUID.randomUUID());
						}
						Constants.ExampleAOIsMap.put(event.getNetworkArea().getId(), event.getNetworkArea());
						Constants.ExampleAOIsToUUIDsMap.put(event.getNetworkArea(), event.getNetworkArea().getId());
					}
					// aggregate known areas that are inside of this area of interest
					for (Map.Entry<UUID,NetworkAreaInfo> entry : Constants.ExampleAOIsMap.entrySet()){
						UUID key = entry.getKey(); NetworkAreaInfo aoi = entry.getValue();
						if(event.getNetworkArea().containsArea(aoi) || key.equals(event.getNetworkArea().getId())){
							event.getNetworkArea().addContainedAreaIdsItem(key);
						}
					}
					event.getNetworkArea().setContainedAreaIds(ParserUtil.removeDuplicates(event.getNetworkArea().getContainedAreaIds()));
				}
			}
			//check whether data is available to be gathered
			NotificationBuilder notifBuilder = new NotificationBuilder();
			NnwdafEventsSubscriptionNotification notification = notifBuilder.initNotification(id);
			try {
				wakeUpDataProducer("dummy");
				// wakeUpDataProducer("prom");
				notification=NotificationUtil.getNotification(body, i, notification, metricsService);
				if(body.getEvtReq()!=null && body.getEvtReq().isImmRep() && notification!=null){
					body.addEventNotificationsItem(notification.getEventNotifications().get(i));
				}
			} catch (JsonProcessingException e) {
				failed_notif=true;
				logger.error("Failed to collect data for event: "+eType,e);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			} catch(InterruptedException e){
				logger.error("Thread failed to wait for datacollection to start for event: "+eType,e);
			} catch(Exception e) {
				failed_notif=true;
				logger.error("Failed to collect data for event(timescaledb error): "+eType,e);
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
		// check the amount of subscriptions that need to be notifed
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
	
	public void wakeUpDataProducer(String choise) throws InterruptedException{
		switch(choise){
			case "prom":
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
				break;
			case "dummy":
				// check if it needs to wake up dummy data producer
				if(DummyDataProducerListener.getNo_dummyDataProducerEventListeners()==0){
					dummyDataProducerPublisher.publishDataCollection("dummy data production");
					//wait for data publishing to start
					Thread.sleep(50);
					while((!DummyDataProducerListener.getStarted_saving_data()) && DummyDataProducerListener.getNo_dummyDataProducerEventListeners()>0){
						Thread.sleep(50);
					}
					Thread.sleep(50);
				}
				break;
			default:
				break;	
		}		
	}
}
