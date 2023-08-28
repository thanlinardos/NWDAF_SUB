package io.nwdaf.eventsubscription.notify;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.requestbuilders.PrometheusRequestBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class DataCollectionListener {
	private static Integer no_dataCollectionEventListeners = 0;
	private static final Object dataCollectionLock = new Object();
	private static Boolean started_saving_data = false;
	private static final Object started_saving_data_lock = new Object();

	@Autowired
	MetricsService metricsService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    void excecuteDataCollection(String param) {
    	synchronized (dataCollectionLock) {
			if(no_dataCollectionEventListeners<1) {
				no_dataCollectionEventListeners++;
			}
			else {
				return;
			}
		}
    	Logger logger = NwdafSubApplication.getLogger();
    	logger.info("collecting data...");
    	long start,prom_delay;
    	List<NwdafEventEnum> suppEvents = Constants.supportedEvents;
    	while(true) {
    		start = System.nanoTime();
    		prom_delay = 0l;
    		for(int i=0;i<suppEvents.size();i++) {
    			NwdafEventEnum eType = suppEvents.get(i);
    			if(eType.equals(NwdafEventEnum.NF_LOAD)) {
    				List<NfLoadLevelInformation> nfloadinfos=new ArrayList<>();
    				try {
    					long t = System.nanoTime();
						nfloadinfos = new PrometheusRequestBuilder().execute(eType, env.getProperty("nnwdaf-eventsubscription.prometheus_url"));
						prom_delay += (System.nanoTime() - t) / 1000000l;
    				} catch (JsonProcessingException e) {
						logger.error("Failed to collect data for event: "+eType,e);
						synchronized (dataCollectionLock) {
				    		no_dataCollectionEventListeners--;
				    	}
						synchronized(started_saving_data_lock){
							started_saving_data = false;
						}
						return;
					}
    				if(nfloadinfos==null || nfloadinfos.size()==0) {
    					logger.error("Failed to collect data for event: "+eType);
    					synchronized (dataCollectionLock) {
				    		no_dataCollectionEventListeners--;
				    	}
						synchronized(started_saving_data_lock){
							started_saving_data = false;
						}
						return;
    				}
    				else {
    					for(int j=0;j<nfloadinfos.size();j++) {
    						try {
    							// System.out.println("nfloadinfo"+j+": "+nfloadinfos.get(j));
    							metricsService.create(nfloadinfos.get(j));
								synchronized(started_saving_data_lock){
									started_saving_data = true;
								}
    						}
    						catch(Exception e) {
    							logger.error("Failed to save nfloadlevelinfo to timescaledb",e);
    							synchronized (dataCollectionLock) {
    					    		no_dataCollectionEventListeners--;
    					    	}
								synchronized(started_saving_data_lock){
									started_saving_data = false;
								}
    							return;
    						}
    					}
    				}
    			}
    		}
    		long diff = (System.nanoTime()-start) / 1000000l;
    		long wait_time = (long)Constants.MIN_PERIOD_SECONDS*1000l;
    		if(diff<wait_time) {
	    		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
					synchronized (dataCollectionLock) {
			    		no_dataCollectionEventListeners--;
			    	}
					synchronized(started_saving_data_lock){
						started_saving_data = false;
					}
					return;
				}
    		}
    		logger.info("prom request delay = "+prom_delay+"ms");
    		logger.info("data coll total delay = "+diff+"ms");
    	}
    	
    }
	public static Object getDatacollectionlock() {
		return dataCollectionLock;
	}
	public static Integer getNo_dataCollectionEventListeners() {
		return no_dataCollectionEventListeners;
	}
	public static Object getStartedSavingDataLock() {
		return started_saving_data_lock;
	}
	public static Boolean getStarted_saving_data() {
		return started_saving_data;
	}
}
