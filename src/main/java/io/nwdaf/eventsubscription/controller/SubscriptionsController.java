package io.nwdaf.eventsubscription.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import io.nwdaf.eventsubscription.utilities.OtherUtil;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.customModel.DiscoverMessage;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerListener;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionListener;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionPublisher;
import io.nwdaf.eventsubscription.kafka.KafkaConsumer;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.kafka.datacollection.dummy.KafkaDummyDataListener;
import io.nwdaf.eventsubscription.kafka.datacollection.dummy.KafkaDummyDataPublisher;
import io.nwdaf.eventsubscription.kafka.datacollection.prometheus.KafkaDataCollectionListener;
import io.nwdaf.eventsubscription.kafka.datacollection.prometheus.KafkaDataCollectionPublisher;
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
import io.nwdaf.eventsubscription.notify.NotificationUtil;
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
	private KafkaDummyDataPublisher kafkaDummyDataPublisher;

	@Autowired
	private KafkaDataCollectionPublisher kafkaDataCollectionPublisher;

	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	MetricsService metricsService;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	ObjectMapper objectMapper;

	private Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);
	
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(@Valid NnwdafEventsSubscription body) {
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
			EventSubscription eventSubscription = body.getEventSubscriptions().get(i);
			NwdafEventEnum eType = eventSubscription.getEvent().getEvent();
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
			if(eventSubscription.getNetworkArea().getId() != null){
				matchingArea = Constants.ExampleAOIsMap.get(eventSubscription.getNetworkArea().getId());
				insideServiceArea = Constants.ServingAreaOfInterest.containsArea(matchingArea);
			}
			Constants.ExampleAOIsMap.get(eventSubscription.getNetworkArea().getId());
			if(eventSubscription.getNetworkArea()!=null && (CheckUtil.safeCheckNetworkAreaNotEmpty(eventSubscription.getNetworkArea()) || eventSubscription.getNetworkArea().getId() != null)){
				if(!(CheckUtil.safeCheckNetworkAreaNotEmpty(eventSubscription.getNetworkArea()) && Constants.ServingAreaOfInterest.containsArea(eventSubscription.getNetworkArea()))
					&& (matchingArea == null || !insideServiceArea)){
					failed_notif = true;
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
					System.out.println("not inside serving aoi");
				}
				else{
					// check if its lists equal one of the known AOIs -> set the id
					if(Constants.ExampleAOIsToUUIDsMap.containsKey(eventSubscription.getNetworkArea())){
						eventSubscription.getNetworkArea().id(Constants.ExampleAOIsToUUIDsMap.get(eventSubscription.getNetworkArea()));
						System.out.println("same as aoi with id: "+eventSubscription.getNetworkArea().getId());
					}
					// check if id equals to a known AOI -> set the area lists
					else if(eventSubscription.getNetworkArea().getId() != null && Constants.ExampleAOIsMap.containsKey(eventSubscription.getNetworkArea().getId())){
						NetworkAreaInfo matchingAOI = Constants.ExampleAOIsMap.get(eventSubscription.getNetworkArea().getId());
						eventSubscription.getNetworkArea().ecgis(matchingAOI.getEcgis()).ncgis(matchingAOI.getNcgis())
								.gRanNodeIds(matchingAOI.getGRanNodeIds()).tais(matchingAOI.getTais());
					}
					// if new AOI create id if it doesnt have one and add it to the known AOIs
					else{
						if(eventSubscription.getNetworkArea().getId() == null){
							eventSubscription.getNetworkArea().id(UUID.randomUUID());
						}
						Constants.ExampleAOIsMap.put(eventSubscription.getNetworkArea().getId(), eventSubscription.getNetworkArea());
						Constants.ExampleAOIsToUUIDsMap.put(eventSubscription.getNetworkArea(), eventSubscription.getNetworkArea().getId());
					}
					// aggregate known areas that are inside of this area of interest
					for (Map.Entry<UUID,NetworkAreaInfo> entry : Constants.ExampleAOIsMap.entrySet()){
						UUID key = entry.getKey(); NetworkAreaInfo aoi = entry.getValue();
						if(eventSubscription.getNetworkArea().containsArea(aoi) || key.equals(eventSubscription.getNetworkArea().getId())){
							eventSubscription.getNetworkArea().addContainedAreaIdsItem(key);
						}
					}
					eventSubscription.getNetworkArea().setContainedAreaIds(ParserUtil.removeDuplicates(eventSubscription.getNetworkArea().getContainedAreaIds()));
				}
			}
			// find past/future offset requested by client:
			Integer[] findOffset = NotificationUtil.findRequestedDataOffset(eventSubscription);
			Integer no_secs = findOffset[0];
			// both future and past predictions not allowed
			if(findOffset[1]==2){
				body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.BOTH_STAT_PRED_NOT_ALLOWED)));
				continue;
			}
			// future data not implemented
			if(findOffset[1]==1){
				body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.OTHER)));
				continue;
			}
			//check whether data is available to be gathered
			NnwdafEventsSubscriptionNotification notification = new NotificationBuilder().build(id);
			boolean isDataAvailable=false;
			Integer expectedWaitTime=null;
			try {
				// wakeUpDataProducer("kafka_local_dummy", eType);
				// wakeUpDataProducer("kafka_local_prom", eType);
				Integer[] wakeUpResult = NotificationUtil.wakeUpDataProducer("kafka", eType, no_secs,dataCollectionPublisher,dummyDataProducerPublisher,
					kafkaDummyDataPublisher,kafkaDataCollectionPublisher,kafkaProducer,objectMapper);
				isDataAvailable = wakeUpResult[0]==1;
				expectedWaitTime = wakeUpResult[1];
			} catch(IOException e){
				failed_notif=true;
				logger.error("Couldn't connect to kafka topic WAKE_UP.", e);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			} catch(InterruptedException e){
				failed_notif=true;
				logger.error("Thread failed to wait for datacollection to start for event: "+eType,e);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			if(!isDataAvailable){
				failed_notif=true;
				logger.info("Informed by collector(s) that data is not available");
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			if(!failed_notif){
				try {
					notification=NotificationUtil.getNotification(body, i, notification, metricsService);
					if(body.getEvtReq()!=null && body.getEvtReq().isImmRep() && notification!=null){
						body.addEventNotificationsItem(notification.getEventNotifications().get(i));
					}
				} catch (JsonProcessingException e) {
					failed_notif=true;
					logger.error("Failed to collect data for event: "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				} catch(Exception e) {
					failed_notif=true;
					logger.error("Failed to collect data for event(timescaledb error): "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
			}
			if(notification==null) {
				logger.error("Failed to collect data for event: "+eType);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			// add failureEventInfo
			if(notification==null || failed_notif) {
    			body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(failCode)));
			}
			else {
				canServeSubscription.set(i, true);
				if(immRep) {
					body.addEventNotificationsItem(notification.getEventNotifications().get(0)
							.rvWaitTime(expectedWaitTime));
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
	public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
			@Valid NnwdafEventsSubscription body) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions"+"/"+subscriptionId;
		
		subscriptionService.update(ParserUtil.safeParseLong(subscriptionId),body);
		responseHeaders.set("Location",subsUri);
		return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(body);
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
}
