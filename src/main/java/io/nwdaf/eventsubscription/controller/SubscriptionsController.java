package io.nwdaf.eventsubscription.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
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
import io.nwdaf.eventsubscription.requestbuilders.PrometheusRequestBuilder;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;
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
	MetricsService metricsService;
	
	
	@Override
	public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(
			@Valid NnwdafEventsSubscription body) {
		Logger logger = NwdafSubApplication.getLogger();
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
						
				}
				else {
					no_events--;
				}				
			}
		}
		
		PrometheusRequestBuilder promReqBuilder = new PrometheusRequestBuilder();
		List<Boolean> canServeSubscription = new ArrayList<>();
		for(int i=0;i<no_events;i++) {
			canServeSubscription.add(false);
		}
		NotificationBuilder notifBuilder = new NotificationBuilder();
		for(int i=0;i<no_events;i++) {
			EventSubscription event = body.getEventSubscriptions().get(i);
			NwdafEventEnum eType = event.getEvent().getEvent();
			//check if period is valid (between 1sec and 10mins) and if not add failureReport
			if(eventIndexToRepPeriodMap.get(i)!=null && eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
				Boolean failed_notif = false;
				NwdafFailureCodeEnum failCode = null;
				if(eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS||eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS) {
					failed_notif = true;
					failCode = NwdafFailureCodeEnum.OTHER;
				}
				//check whether data is available to be gathered
				List<NfLoadLevelInformation> nfloadinfos=new ArrayList<>();
				try {
					nfloadinfos = new PrometheusRequestBuilder().execute(eType, env.getProperty("nnwdaf-eventsubscription.prometheus_url"), env.getProperty("nnwdaf-eventsubscription.containerNames"));
				} catch (JsonProcessingException e) {
					failed_notif=true;
					logger.error("Failed to collect data for event: "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
				if(nfloadinfos.size()==0) {
					logger.error("Failed to collect data for event: "+eType);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
				// add failureEventInfo
				if(nfloadinfos.size()==0 || failed_notif) {
    				body.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(failCode)));
				}
				else {
					canServeSubscription.set(i, true);
					logger.info("avg_cpu="+nfloadinfos.get(0).getNfCpuUsage());
				}
				logger.info("notifMethod="+eventIndexToNotifMethodMap.get(i)+", repPeriod="+eventIndexToRepPeriodMap.get(i));
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
			notifyPublisher.publishNotification(id);
		}

		System.out.println("id="+id);
		System.out.println(body);
		responseHeaders.set("Location",subsUri+"/"+id);
		ResponseEntity<NnwdafEventsSubscription> response = ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(body);
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
