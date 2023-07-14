package io.nwdaf.eventsubscription.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.EventNotification;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.repository.eventnotification.NnwdafNotificationTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.requestbuilders.PrometheusRequestBuilder;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RestController
public class SubscriptionsController implements SubscriptionsApi{
	
	@Autowired
	private Environment env;
	
	@Autowired
	private NotifyPublisher notifyPublisher;
	
	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(
			@Valid NnwdafEventsSubscription body) {
		String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
		//System.out.println(body.toString());
		System.out.println("Controller logic...");
		HttpHeaders responseHeaders = new HttpHeaders();
		
		if(body==null) {
			ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeaders).body(body);
			return response;
		}
		Integer repetionPeriod = null;
		NotificationMethodEnum notificationMethod = null;
		Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
		Map<Integer,Integer> eventIndexToRepPeriodMap = new HashMap<>();
		Boolean muted=false;
		if(body.getEvtReq()!=null) {
			if(body.getEvtReq().getNotifMethod()!=null) {
				notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
				if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetionPeriod = body.getEvtReq().getRepPeriod();
				}
				if(body.getEvtReq().getNotifFlag()!=null) {
					muted=body.getEvtReq().getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE);
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
		PrometheusRequestBuilder promReqBuilder = new PrometheusRequestBuilder();
		List<Boolean> canServeSubscription = new ArrayList<>();
		for(int i=0;i<no_events;i++) {
			canServeSubscription.add(false);
		}
		NotificationBuilder notifBuilder = new NotificationBuilder();
		for(int i=0;i<no_events;i++) {
			if(eventIndexToRepPeriodMap.get(i)!=null && eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
				if(eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS) {
					eventIndexToRepPeriodMap.put(i, Constants.MIN_PERIOD_SECONDS);
				}
				if(eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS) {
					eventIndexToRepPeriodMap.put(i, Constants.MAX_PERIOD_SECONDS);
				}
				//check whether data is available to be gathered
				NnwdafEventsSubscriptionNotification notification = null;
				try {
					notification = notifBuilder.initNotification(0l);
					notification = promReqBuilder.execute(body.getEventSubscriptions().get(i), 0l,notification,env.getProperty("nnwdaf-eventsubscription.prometheus_url"),env.getProperty("nnwdaf-eventsubscription.containerNames"));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				if(notification!=null) {
					canServeSubscription.set(i, true);
					System.out.println("avg_cpu="+notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(0).getNfCpuUsage());
				}
				System.out.println("notifMethod="+eventIndexToNotifMethodMap.get(i)+", repPeriod="+eventIndexToRepPeriodMap.get(i));
				}
		}
		Integer count_of_notif = 0;
		List<Integer> periods_to_serve = new ArrayList<>();
		for(int i=0;i<canServeSubscription.size();i++) {
			if(canServeSubscription.get(i)) {
				count_of_notif++;
				periods_to_serve.add(eventIndexToRepPeriodMap.get(i));
			}
		}
		List<Integer> periods_to_serve_copy = new ArrayList<>(periods_to_serve);
		Collections.sort(periods_to_serve_copy);
		Integer periods_to_serve_min=null;
		Integer periods_to_serve_max=null;
		
		if(periods_to_serve_copy.size()>0) {
			periods_to_serve_min = periods_to_serve_copy.get(0);
			periods_to_serve_max = periods_to_serve_copy.get(periods_to_serve_copy.size()-1);
		}
		if(count_of_notif==0) {
			if(body.getEvtReq()==null) {
				body.setEvtReq(new ReportingInformation());
			}
			body.setEvtReq(body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE)));
		}
		
		NnwdafEventsSubscriptionTable res = subscriptionService.create(body);
		Long id = 0l;
		id=res.getId();
		body.setId(id);
		
		//notify about new saved subscription
		if(count_of_notif>0 && !muted) {
			if(periods_to_serve_min!=null&&periods_to_serve_max!=null) {
				if(periods_to_serve_max-periods_to_serve_min<=60) {
					//only 1 notify thread
					notifyPublisher.publishNotification(id);
				}
			}
			else {
				
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
