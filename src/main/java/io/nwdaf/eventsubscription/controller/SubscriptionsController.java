package io.nwdaf.eventsubscription.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionPublisher;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.kafka.datacollection.dummy.KafkaDummyDataPublisher;
import io.nwdaf.eventsubscription.kafka.datacollection.prometheus.KafkaDataCollectionPublisher;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.notify.NotificationUtil;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.notify.responsetypes.GetGlobalNotifMethodAndRepPeriodResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetNotifMethodAndRepPeriodsResponse;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
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

	@Autowired
	MetricsCacheService metricsCacheService;

	private Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);
	
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(@Valid NnwdafEventsSubscription body) {
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
		System.out.println("Controller logic...");
		HttpHeaders responseHeaders = new HttpHeaders();
		Integer count_of_notif = 0;
		List<Integer> periods_to_serve = new ArrayList<>();
		if(body==null || !CheckUtil.safeCheckListNotEmpty(body.getEventSubscriptions())) {
			ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeaders).body(body);
			return response;
		}
		List<Integer> negotiatedFeaturesList = NotificationUtil.negotiateSupportedFeatures(body);
		logger.info("negotiatedFeaturesList:" + negotiatedFeaturesList.toString());

		GetGlobalNotifMethodAndRepPeriodResponse globalResponse = NotificationUtil.getGlobalNotifMethodAndRepPeriod(body.getEvtReq());
		GetNotifMethodAndRepPeriodsResponse getResponse = NotificationUtil.getNotifMethodAndRepPeriods(
			body.getEventSubscriptions(),
			globalResponse.getNotificationMethod(),
			globalResponse.getRepetionPeriod());
		for(int i=0;i<getResponse.getInvalid_events().size();i++){
			body.getEventSubscriptions().remove((int)getResponse.getInvalid_events().get(i));
		}
		List<Boolean> canServeSubscription = NotificationUtil.checkCanServeSubscriptions(getResponse.getNo_valid_events(), body,
			getResponse.getEventIndexToNotifMethodMap(), getResponse.getEventIndexToRepPeriodMap(),
			dataCollectionPublisher, dummyDataProducerPublisher, kafkaDummyDataPublisher,
			kafkaDataCollectionPublisher, kafkaProducer, objectMapper, metricsService, metricsCacheService,
			globalResponse.getImmRep(), 0l);
		// check the amount of subscriptions that need to be notifed
		for(int i=0;i<canServeSubscription.size();i++) {
			if(canServeSubscription.get(i)) {
				count_of_notif++;
				periods_to_serve.add(getResponse.getEventIndexToRepPeriodMap().get(i));
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

		body.setId(res.getId());
		//notify about new saved subscription
		if(count_of_notif>0 && !globalResponse.getMuted()) {
			notifyPublisher.publishNotification(body.getId());
		}

		System.out.println("id="+body.getId());
		System.out.println(body);
		//set the location header to the subscription uri
		responseHeaders.set("Location",subsUri+"/"+body.getId());
		return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(body);
	}

	@Override
	public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
			@Valid NnwdafEventsSubscription body) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions"+"/"+subscriptionId;
		
		Integer count_of_notif = 0;
		List<Integer> periods_to_serve = new ArrayList<>();
		if(body==null || !CheckUtil.safeCheckListNotEmpty(body.getEventSubscriptions())) {
			ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeaders).body(body);
			return response;
		}
		List<Integer> negotiatedFeaturesList = NotificationUtil.negotiateSupportedFeatures(body);
		logger.info("negotiatedFeaturesList:" + negotiatedFeaturesList.toString());

		GetGlobalNotifMethodAndRepPeriodResponse globalResponse = NotificationUtil.getGlobalNotifMethodAndRepPeriod(body.getEvtReq());
		GetNotifMethodAndRepPeriodsResponse getResponse = NotificationUtil.getNotifMethodAndRepPeriods(
			body.getEventSubscriptions(),
			globalResponse.getNotificationMethod(),
			globalResponse.getRepetionPeriod());
		for(int i=0;i<getResponse.getInvalid_events().size();i++){
			body.getEventSubscriptions().remove((int)getResponse.getInvalid_events().get(i));
		}
		List<Boolean> canServeSubscription = NotificationUtil.checkCanServeSubscriptions(getResponse.getNo_valid_events(), body,
			getResponse.getEventIndexToNotifMethodMap(), getResponse.getEventIndexToRepPeriodMap(),
			dataCollectionPublisher, dummyDataProducerPublisher, kafkaDummyDataPublisher,
			kafkaDataCollectionPublisher, kafkaProducer, objectMapper, metricsService, metricsCacheService,
			globalResponse.getImmRep(), 0l);
		// check the amount of subscriptions that need to be notifed
		for(int i=0;i<canServeSubscription.size();i++) {
			if(canServeSubscription.get(i)) {
				count_of_notif++;
				periods_to_serve.add(getResponse.getEventIndexToRepPeriodMap().get(i));
			}
		}
		//if no subscriptions need notifying mute the subscription
		if(count_of_notif==0) {
			if(body.getEvtReq()==null) {
				body.setEvtReq(new ReportingInformation());
			}
			body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE));
		}

		subscriptionService.update(ParserUtil.safeParseLong(subscriptionId),body);
		//notify about updated subscription
		if(count_of_notif>0 && !globalResponse.getMuted()) {
			notifyPublisher.publishNotification(body.getId());
		}
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
