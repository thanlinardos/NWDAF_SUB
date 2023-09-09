package io.nwdaf.eventsubscription.notify;


import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.NotificationUtil;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.RestTemplateFactoryConfig;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

import org.springframework.core.io.Resource;


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

	@Autowired
	ObjectMapper objectMapper;

	// @Autowired
	RestTemplate template;

	@Value("${trust.store}")
    private Resource trustStore;
    @Value("${trust.store.password}")
    private String trustStorePassword;
	
	RestTemplateFactoryConfig restTemplateFactoryConfig;


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
    	//builder for converting data to notification objects
    	NotificationBuilder notifBuilder = new NotificationBuilder();
    	Integer c = 0;
    	for(int i=0;i<subs.size();i++) {
    		for(int j=0;j<subs.get(i).getEventSubscriptions().size();j++) {
    			Integer period = NotificationUtil.needsServing(subs.get(i),j);
		    	if(period!=null) {
			    	c++;
			    	repPeriods.put(Pair.of(subs.get(i).getId(), j),period);
			    	lastNotifTimes.put(Pair.of(subs.get(i).getId(), j),OffsetDateTime.now());
			    	subIndexes.put(subs.get(i).getId(), i);
		    	}
    		}
    		
    	}
    	System.out.println("no_subs="+subs.size());
    	System.out.println("no_Ssubs="+c);
		restTemplateFactoryConfig = new RestTemplateFactoryConfig(trustStore, trustStorePassword);
    	long start,prom_delay,client_delay;
    	while(c>0) {
			// check if the notifier has been stopped manually
			synchronized(notifLock){
				if(no_notifEventListeners == 0){
					logger.info("NotifyListener stopped!");
					return;
				}
			}
    		start = System.nanoTime();
    		prom_delay=0;
    		client_delay=0;
			// loop through the event subscriptions and the dates of last producing a notification for each of them
    		for(Map.Entry<Pair<Long,Integer>,OffsetDateTime> entry:lastNotifTimes.entrySet()) {
				// match the id to the subscription
    			Long id = entry.getKey().getFirst();
				int eventIndex = entry.getKey().getSecond();
				NnwdafEventsSubscription sub = subs.get(subIndexes.get(id));
				// match the event index to the event subscription
    			EventSubscription event = sub.getEventSubscriptions().get(eventIndex);
    			Integer repPeriod = NotificationUtil.needsServing(sub, eventIndex);
				// build the notification
    			NnwdafEventsSubscriptionNotification notification = notifBuilder.initNotification(id);
    			long st  = System.nanoTime();
    			try {
    				notification = NotificationUtil.getNotification(sub, eventIndex, notification, metricsService);
    			}catch(JsonMappingException e) {
    				logger.error("Error building the notification for sub: "+id+". Data is no longer available for this event",e);
    				// add failureEventInfo
    				sub.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
    				continue;
    			}catch(JsonProcessingException e) {
    				logger.error("Error building the notification for sub: "+id+". Data is no longer available for this event",e);
    				// add failureEventInfo
    				sub.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
    				continue;
    			}
    			catch(Exception e) {
    				logger.error("Failed to collect data for event(timescaledb error)",e);
    				synchronized (notifLock) {
    	        		no_notifEventListeners--;
    	        	}
    	    		return;	
    			}
    			if(notification==null) {
    				continue;
    			}
        		prom_delay += (System.nanoTime()-st) / 1000000l;
    			//save the sent notification to a second database
    			notificationService.create(notification);
    			
    			//check if period has passed -> notify client (or if threshold has been reached)
				if(thresholdReached(event, notification)){
					System.out.println("threshold: "+thresholdReached(event,notification));
				}
    			if((repPeriod==0 && thresholdReached(event,notification)) || (repPeriod!=0 && OffsetDateTime.now().compareTo(entry.getValue().plusSeconds((long) repPeriod))>0)) {
	            	st = System.nanoTime();
	        		HttpEntity<NnwdafEventsSubscriptionNotification> client_request = new HttpEntity<>(notification);
	        		ResponseEntity<NnwdafEventsSubscriptionNotification> client_response=null;
	        		try {
						template = new RestTemplate(restTemplateFactoryConfig.createRestTemplateFactory());
	        			client_response = template.postForEntity(sub.getNotificationURI()+"/notify",client_request, NnwdafEventsSubscriptionNotification.class);
						System.out.println("sent");
					}catch(RestClientException e) {
	        			logger.error("Error connecting to client "+sub.getNotificationURI());
						logger.info(e.toString());
	        		}
	        		client_delay += (System.nanoTime()-st)/1000000l;
					// if notifying the client was successful update the map with the current time
	    			if(client_response!=null) {
		        		if(client_response.getStatusCode().is2xxSuccessful()) {
		    				lastNotifTimes.put(Pair.of(entry.getKey().getFirst(), eventIndex), OffsetDateTime.now());
		    			}
	    			}
	    			
    			}
    		}
    		try {
   			long st_sub = System.nanoTime();
    			subs = subscriptionService.findAll();
   			// System.out.println("sub query time: "+(System.nanoTime()-st_sub)/1000000l+"ms");
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
        			Integer period = NotificationUtil.needsServing(subs.get(i),j);
    		    	if(period!=null) {
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
        		}
        	}
        	// System.out.print("no_subs="+subs.size());
        	// System.out.print(" || no_Ssubs="+c);
        	// System.out.print(" || timescaledb req delay: "+prom_delay+"ms");
    		// System.out.print(" || sending notif to client delay:"+client_delay+"ms");
        	long diff = (System.nanoTime()-start) / 1000000l;
        	// System.out.print(" || total delay: "+diff+"ms\n");
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

	private boolean thresholdReached(EventSubscription event, NnwdafEventsSubscriptionNotification notification) {
		NwdafEventEnum eType = event.getEvent().getEvent();
		
		switch(eType){
			case NF_LOAD:
				List<ThresholdLevel> thresholdLevels = event.getNfLoadLvlThds();
				if(thresholdLevels==null || thresholdLevels.isEmpty()){
					return false;
				}
				int size = Math.min(notification.getEventNotifications().get(0).getNfLoadLevelInfos().size(), thresholdLevels.size());
				List<NfLoadLevelInformation> presentNfLevelInformations = ParserUtil.parsePresentNfLoadLevelInformations(notification.getEventNotifications().get(0).getNfLoadLevelInfos());
				for(int i=0;i<size;i++){
					NfLoadLevelInformation nfLoadLevelInformation = presentNfLevelInformations.get(i);
					if(thresholdLevels.get(i)==null){
						continue;
					}
					Boolean[] isThresholdBooleans = {(thresholdLevels.get(i).getNfCpuUsage()!=null && nfLoadLevelInformation.getNfCpuUsage()>=thresholdLevels.get(i).getNfCpuUsage()),
					(thresholdLevels.get(i).getNfMemoryUsage()!=null && nfLoadLevelInformation.getNfMemoryUsage()>=thresholdLevels.get(i).getNfMemoryUsage()),
					(thresholdLevels.get(i).getNfStorageUsage()!=null && nfLoadLevelInformation.getNfStorageUsage()>=thresholdLevels.get(i).getNfStorageUsage()),
					(thresholdLevels.get(i).getNfLoadLevel()!=null && nfLoadLevelInformation.getNfLoadLevelAverage()>=thresholdLevels.get(i).getNfLoadLevel())};
					String[] propertyNames = {"nfCpuUsage","nfMemoryUsage","nfStorageUsage","nfLoadLevelAverage"};
					if((isThresholdBooleans[0] || isThresholdBooleans[1] || isThresholdBooleans[2] || isThresholdBooleans[3]
					)){
						Integer index = notification.getEventNotifications().get(0).getNfLoadLevelInfos().indexOf(nfLoadLevelInformation);
						Integer booleanIndex = 0;
						Integer[] propertyValues = {notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).getNfCpuUsage(),notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).getNfMemoryUsage(),notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).getNfStorageUsage(),notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).getNfLoadLevelAverage()};
						for(int j=0;j<isThresholdBooleans.length;j++){
							if(isThresholdBooleans[j]){
								booleanIndex = j;
								break;
							}
						}
						notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).setThresholdProperty(propertyNames[booleanIndex]);
						notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index).setThresholdValue(propertyValues[booleanIndex]);
						return true;
					}
				}
				break;
			default:
				break;
		}
		return false;
	}
	
}