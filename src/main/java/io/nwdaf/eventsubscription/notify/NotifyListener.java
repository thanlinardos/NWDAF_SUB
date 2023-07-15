package io.nwdaf.eventsubscription.notify;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.requestbuilders.PrometheusRequestBuilder;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@Component
public class NotifyListener {
	
	private Integer no_notifEventListeners = 0;
	private final Object notifLock = new Object();
	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    void sendNotifications(Long subId) throws JsonMappingException, JsonProcessingException, InterruptedException {
    	synchronized (notifLock) {
			if(no_notifEventListeners<1) {
				no_notifEventListeners++;
			}
			else {
				return;
			}
		}
    	List<NnwdafEventsSubscription> subs = subscriptionService.findAll();
    	//map with key each served event (pair of sub id,event index)
    	//and value being the last time a notification was sent for this event to the corresponding client
    	Map<Pair<Long,Integer>,OffsetDateTime> lastNotifTimes = new HashMap<>();
    	//repetition period for each event
    	Map<Pair<Long,Integer>,Integer> repPeriods = new HashMap<>();
    	Map<Pair<Long,Integer>,OffsetDateTime> oldNotifTimes = new HashMap<>();
    	Map<Long,Integer> subIndexes = new HashMap<>();
    	RestTemplate template = new RestTemplate();
    	//builder for prometheus data requests
    	PrometheusRequestBuilder promReqBuilder = new PrometheusRequestBuilder();
    	//builder for converting data to notification objects
    	NotificationBuilder notifBuilder = new NotificationBuilder();
    	Integer c = 0;
    	for(int i=0;i<subs.size();i++) {
    		for(int j=0;j<subs.get(i).getEventSubscriptions().size();j++) {
    			Integer period = needsServing(subs.get(i),j);
		    	if(period!=null && period!=0) {
		    		c++;
		    		repPeriods.put(Pair.of(subs.get(i).getId(), j),period);
		    		lastNotifTimes.put(Pair.of(subs.get(i).getId(), j),OffsetDateTime.now());
		    		subIndexes.put(subs.get(i).getId(), i);
		    	}
		    	if(period==0) {
		    		//threshold
		    	}
    		}
    		
    	}
    	System.out.println("no_subs="+subs.size());
    	System.out.println("no_Ssubs="+c);
    	
    	long start;
    	while(c>0) {
    		start = System.nanoTime();
    		for(Map.Entry<Pair<Long,Integer>,OffsetDateTime> entry:lastNotifTimes.entrySet()) {
    			Long id = entry.getKey().getFirst();
    			Integer repPeriod = subs.get(subIndexes.get(id)).getEventSubscriptions().get(entry.getKey().getSecond()).getRepetitionPeriod();
    			if(subs.get(subIndexes.get(id)).getEvtReq()!=null) {
	    			if(subs.get(subIndexes.get(id)).getEvtReq().getRepPeriod()!=null) {
	    				repPeriod = subs.get(subIndexes.get(id)).getEvtReq().getRepPeriod();
	    			}
    			}
//    			System.out.println("id: "+id+", now: "+OffsetDateTime.now());
//    			System.out.println("lastNotif+Rep: "+entry.getValue().plusSeconds((long) repPeriod));
    			if(OffsetDateTime.now().compareTo(entry.getValue().plusSeconds((long) repPeriod))>0) {
//    				long st  = System.nanoTime();
	    			NnwdafEventsSubscriptionNotification notification = notifBuilder.initNotification(id);
	        		notification = promReqBuilder.execute(subs.get(subIndexes.get(id)).getEventSubscriptions().get(entry.getKey().getSecond()), id,notification,env.getProperty("nnwdaf-eventsubscription.prometheus_url"),env.getProperty("nnwdaf-eventsubscription.containerNames"));
//	        		long diff = (System.nanoTime()-st) / 1000000l;
//	            	System.out.println("prometheus req delay: "+diff+"ms");
//	            	st = System.nanoTime();
	        		HttpEntity<NnwdafEventsSubscriptionNotification> req = new HttpEntity<>(notification);
	    			ResponseEntity<NnwdafEventsSubscriptionNotification> res = template.postForEntity(subs.get(subIndexes.get(id)).getNotificationURI()+"/notify",req, NnwdafEventsSubscriptionNotification.class);
//	    			System.out.println("sending notif to client delay:"+(System.nanoTime()-st)/1000000l);
//	    			System.out.println("NotifListener: subId="+res.getBody().getSubscriptionId()+", cpu_load="+res.getBody().getEventNotifications().get(0).getNfLoadLevelInfos().get(0));
	    			if(res.getStatusCode().is2xxSuccessful()) {
	    				lastNotifTimes.put(Pair.of(entry.getKey().getFirst(), entry.getKey().getSecond()), OffsetDateTime.now());
	    			}
	    			//save the sent notification to a second database
//	    			st = System.nanoTime();
	    			notificationService.create(notification);
//	    			System.out.println("notif save delay: "+(System.nanoTime()-st)/1000000l+"ms");
    			}
    		}
    		
//    		subs.clear();
    		subs = subscriptionService.findAll();
    		//make a copy of the map
        	oldNotifTimes.clear();
        	for(Map.Entry<Pair<Long,Integer>,OffsetDateTime> entry:lastNotifTimes.entrySet()) {
        		oldNotifTimes.put(Pair.of(entry.getKey().getFirst(), entry.getKey().getSecond()),entry.getValue());
        	}
        	lastNotifTimes.clear();
        	repPeriods.clear();
        	subIndexes.clear();
        	c=0;
        	for(int i=0;i<subs.size();i++) {
        		Long id = subs.get(i).getId();
        		for(int j=0;j<subs.get(i).getEventSubscriptions().size();j++) {
        			Integer period = needsServing(subs.get(i),j);
        			Pair<Long,Integer> p = Pair.of(id, j);
    		    	if(period!=null && period!=0) {
    		    		c++;
    		    		repPeriods.put(Pair.of(id, j),period);
    		    		if(oldNotifTimes.get(Pair.of(id, j))==null){
    		    			lastNotifTimes.put(Pair.of(id,j),OffsetDateTime.now());
    		    		}
    		    		else {
    		    			lastNotifTimes.put(Pair.of(id, j),oldNotifTimes.get(Pair.of(id, j)));
    		    		}
    		    		subIndexes.put(subs.get(i).getId(), i);
    		    	}
    		    	if(period==0) {
    		    		//threshold
    		    	}
        		}
        	}
        	System.out.println("no_subs="+subs.size());
        	System.out.println("no_Ssubs="+c);
        	//wait till one second passes (10^9 nanoseconds)
        	long diff = (System.nanoTime()-start) / 1000000l;
        	System.out.println("total delay: "+diff+"ms");
        	if(diff<250l) {
        		Thread.sleep(250l-diff);
        	}
    	}
    	System.out.println("==NotifListener("+no_notifEventListeners+") finished===");
    	synchronized (notifLock) {
    		no_notifEventListeners--;
    	}
    }

    //check if subscription needs to be served and return the repetition period (0 for threshold)
	private Integer needsServing(NnwdafEventsSubscription body, Integer eventIndex) {
		NotificationMethodEnum notificationMethod=null;
		Integer repetitionPeriod=null;
		
		if(body.getEventSubscriptions().get(eventIndex)!=null) {
			if(body.getEventSubscriptions().get(eventIndex).getNotificationMethod()!=null) {
				notificationMethod = body.getEventSubscriptions().get(eventIndex).getNotificationMethod().getNotifMethod();
				if(notificationMethod.equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEventSubscriptions().get(eventIndex).getRepetitionPeriod();
				}
			}
		}
		
		if(body.getEvtReq()!=null) {
			if(body.getEvtReq().getNotifFlag()!=null) {
				if(body.getEvtReq().getNotifFlag().getNotifFlag()!=null) {
					if(body.getEvtReq().getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE)) {
						return null;
					}
				}
			}
			if(body.getEvtReq().getNotifMethod()!=null) {
				notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
				if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEvtReq().getRepPeriod();
				}
			}
		}
		if(notificationMethod==null) {
			return null;
		}
		if(notificationMethod.equals(NotificationMethodEnum.THRESHOLD)) {
			return 0;
		}
		return repetitionPeriod;
	}



}
