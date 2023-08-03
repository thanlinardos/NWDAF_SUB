package io.nwdaf.eventsubscription.notify;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;
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
	MetricsService metricsService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    void sendNotifications(Long subId){
    	synchronized (notifLock) {
			if(no_notifEventListeners<1) {
				no_notifEventListeners++;
			}
			else {
				return;
			}
		}
    	Logger logger = NwdafSubApplication.getLogger();
    	List<NnwdafEventsSubscription> subs = null;
    	try {
    		subs = subscriptionService.findAll();
    	}catch(Exception e) {
    		logger.error("Error with find subs in subscriptionService", e);
    		synchronized (notifLock) {
        		no_notifEventListeners--;
        	}
    		return;
    	}
    	//map with key each served event (pair of sub id,event index)
    	//and value being the last time a notification was sent for this event to the corresponding client
    	Map<Pair<Long,Integer>,OffsetDateTime> lastNotifTimes = new HashMap<>();
    	//repetition period for each event
    	Map<Pair<Long,Integer>,Integer> repPeriods = new HashMap<>();
    	Map<Pair<Long,Integer>,OffsetDateTime> oldNotifTimes = new HashMap<>();
    	Map<Long,Integer> subIndexes = new HashMap<>();
    	RestTemplate template = new RestTemplate();
    	//builder for converting data to notification objects
    	NotificationBuilder notifBuilder = new NotificationBuilder();
    	Integer c = 0;
    	for(int i=0;i<subs.size();i++) {
    		for(int j=0;j<subs.get(i).getEventSubscriptions().size();j++) {
    			Integer period = needsServing(subs.get(i),j);
		    	if(period!=null) {
		    		if(period!=0) {
			    		c++;
			    		repPeriods.put(Pair.of(subs.get(i).getId(), j),period);
			    		lastNotifTimes.put(Pair.of(subs.get(i).getId(), j),OffsetDateTime.now());
			    		subIndexes.put(subs.get(i).getId(), i);
		    		}
		    		else {
		    			//threshold
		    		}
		    	}
    		}
    		
    	}
    	System.out.println("no_subs="+subs.size());
    	System.out.println("no_Ssubs="+c);
    	
    	long start,prom_delay,notif_save_delay,client_delay;
    	while(c>0) {
    		start = System.nanoTime();
    		prom_delay=0;
    		notif_save_delay=0;
    		client_delay=0;
    		for(Map.Entry<Pair<Long,Integer>,OffsetDateTime> entry:lastNotifTimes.entrySet()) {
    			Long id = entry.getKey().getFirst();
    			EventSubscription event = subs.get(subIndexes.get(id)).getEventSubscriptions().get(entry.getKey().getSecond());
    			Integer repPeriod = event.getRepetitionPeriod();
    			if(subs.get(subIndexes.get(id)).getEvtReq()!=null) {
	    			if(subs.get(subIndexes.get(id)).getEvtReq().getRepPeriod()!=null) {
	    				repPeriod = subs.get(subIndexes.get(id)).getEvtReq().getRepPeriod();
	    			}
    			}
    			NnwdafEventsSubscriptionNotification notification = notifBuilder.initNotification(id);
    			long st  = System.nanoTime();
    			try {
    				notification = getNotification(event, notification);
    			}catch(JsonMappingException e) {
    				logger.error("Error building the notification for sub: "+id+". Data is no longer available for this event",e);
    				// add failureEventInfo
    				subs.get(subIndexes.get(id)).addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
    				continue;
    			}catch(JsonProcessingException e) {
    				logger.error("Error building the notification for sub: "+id+". Data is no longer available for this event",e);
    				// add failureEventInfo
    				subs.get(subIndexes.get(id)).addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
    				continue;
    			}
    			catch(Exception e) {
    				logger.error("Error connecting to timescale db");
    				synchronized (notifLock) {
    	        		no_notifEventListeners--;
    	        	}
    	    		return;	
    			}
    			if(notification==null) {
//    				logger.error("Error building the notification for sub: "+id+". Data is no longer available for this event");
    				// add failureEventInfo
//    				subs.get(subIndexes.get(id)).addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
    				continue;
    			}
        		prom_delay += (System.nanoTime()-st) / 1000000l;
    			//save the sent notification to a second database
    			st = System.nanoTime();
    			notificationService.create(notification);
    			notif_save_delay += (System.nanoTime()-st)/1000000l;
    			
    			//check if period has passed -> notify client
    			if(OffsetDateTime.now().compareTo(entry.getValue().plusSeconds((long) repPeriod))>0) {
	    			
	            	st = System.nanoTime();
	        		HttpEntity<NnwdafEventsSubscriptionNotification> client_request = new HttpEntity<>(notification);
	        		ResponseEntity<NnwdafEventsSubscriptionNotification> client_response=null;
	        		try {
	        			client_response = template.postForEntity(subs.get(subIndexes.get(id)).getNotificationURI()+"/notify",client_request, NnwdafEventsSubscriptionNotification.class);
	        		}catch(RestClientException e) {
	        			logger.error("Error connecting to client "+subs.get(subIndexes.get(id)).getNotificationURI());
	        		}
	        		client_delay += (System.nanoTime()-st)/1000000l;
	    			if(client_response!=null) {
		        		if(client_response.getStatusCode().is2xxSuccessful()) {
		    				lastNotifTimes.put(Pair.of(entry.getKey().getFirst(), entry.getKey().getSecond()), OffsetDateTime.now());
		    			}
	    			}
	    			
    			}
    		}
    		try {
   			long st_sub = System.nanoTime();
    			subs = subscriptionService.findAll();
   			System.out.println("sub query time: "+(System.nanoTime()-st_sub)/1000000l);
    		}catch(Exception e) {
        		logger.error("Error with find subs in subscriptionService", e);
        		synchronized (notifLock) {
            		no_notifEventListeners--;
            	}
        		return;
        	}
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
    		    	if(period!=null) {
    		    		if(period!=0) {
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
        	}
        	System.out.println("no_subs="+subs.size());
        	System.out.println("no_Ssubs="+c);
        	System.out.println("timescaledb req delay: "+prom_delay+"ms");
    		// System.out.println("notif save delay: "+notif_save_delay+"ms");
    		System.out.println("sending notif to client delay:"+client_delay+"ms");
        	long diff = (System.nanoTime()-start) / 1000000l;
        	System.out.println("total delay: "+diff+"ms");
        	//wait till one quarter of a second (min period) passes (10^9/4 nanoseconds)
        	long wait_time = (long)Constants.MIN_PERIOD_SECONDS*250l;
        	if(diff<wait_time) {
        		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
					synchronized (notifLock) {
	            		no_notifEventListeners--;
	            	}
	        		return;
				}
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
	
	private NnwdafEventsSubscriptionNotification getNotification(EventSubscription eventSub,NnwdafEventsSubscriptionNotification notification) throws JsonMappingException, JsonProcessingException, Exception {
		OffsetDateTime now = OffsetDateTime.now();
		NwdafEventEnum eType = eventSub.getEvent().getEvent();
		NotificationBuilder notifBuilder = new NotificationBuilder();
		switch(eType) {
		case NF_LOAD:
			List<NfLoadLevelInformation> nfloadlevels = new ArrayList<>();
			try{
				nfloadlevels = metricsService.findAllInLastSecond();
			} catch(Exception e){
				System.out.println("Cant find metrics from database");
				return null;
			}
			if(nfloadlevels==null || nfloadlevels.size()==0) {
				return null;
			}
			notification = notifBuilder.addEvent(notification, NwdafEventEnum.NF_LOAD, null, null, now, null, null, null, nfloadlevels);	
			break;
		default:
			break;
		}
		
		return notification;
	}
}
