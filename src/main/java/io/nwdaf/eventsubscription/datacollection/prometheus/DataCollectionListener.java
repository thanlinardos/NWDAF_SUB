package io.nwdaf.eventsubscription.datacollection.prometheus;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.requestbuilders.PrometheusRequestBuilder;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.utilities.Constants;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataCollectionListener {
	@Getter
	private static Integer no_dataCollectionEventListeners = 0;
	@Getter
	private static final Object dataCollectionLock = new Object();
	@Getter
	private static Boolean startedSavingData = false;
	@Getter
	private static final Object startedSavingDataLock = new Object();
	private static final Logger logger = LoggerFactory.getLogger(DataCollectionListener.class);
	
	final MetricsService metricsService;
	
	final MetricsCacheService metricsCacheService;

	final Environment env;

	public DataCollectionListener(MetricsService metricsService, MetricsCacheService metricsCacheService, Environment env) {
		this.metricsService = metricsService;
		this.metricsCacheService = metricsCacheService;
		this.env = env;
	}

	@Async
    @EventListener
    void excecuteDataCollection(String param) {
    	if(!start()) {
			return;
		}
    	while(no_dataCollectionEventListeners>0) {
			long start,prom_delay,diff,wait_time;
    		start = System.nanoTime();
			prom_delay = 0L;
    		for(NwdafEventEnum eType : Constants.supportedEvents) {
				switch(eType){
					case NF_LOAD:
                        List<NfLoadLevelInformation> nfloadinfos;
                        try {
                            long t = System.nanoTime();
                            nfloadinfos = new PrometheusRequestBuilder().execute(eType, env.getProperty("nnwdaf-eventsubscription.prometheus_url"));
                            prom_delay += (System.nanoTime() - t) / 1000000L;
                        } catch (JsonProcessingException e) {
                            logger.error("Failed to collect data for event: "+eType,e);
                            stop();
                            continue;
                        }
                        if(nfloadinfos==null || nfloadinfos.isEmpty()) {
                            logger.error("Failed to collect data for event: "+eType);
                            stop();
                            continue;
                        }
                        else {
                            for (NfLoadLevelInformation nfloadinfo : nfloadinfos) {
                                try {
                                    // System.out.println("nfloadinfo"+j+": "+nfloadinfos.get(j));
                                    metricsCacheService.create(nfloadinfo);
                                    synchronized (startedSavingDataLock) {
                                        startedSavingData = true;
                                    }
                                } catch (Exception e) {
                                    logger.error("Failed to save nfloadlevelinfo to timescaledb", e);
                                    stop();
                                    continue;
                                }
                            }
                        }
                        break;
					default:
						break;
				}
    		}
    		diff = (System.nanoTime()-start) / 1000000L;
    		wait_time = (long)Constants.MIN_PERIOD_SECONDS* 1000L;
    		if(diff<wait_time) {
	    		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					logger.error("Failed to wait for thread...",e);
					stop();
					continue;
				}
    		}
    		logger.info("prom request delay = "+prom_delay+"ms");
    		logger.info("data coll total delay = "+diff+"ms");
    	}
    	logger.info("Prometheus Data Collection stopped!");
    }

	public static void stop(){
		synchronized (dataCollectionLock) {
    		no_dataCollectionEventListeners--;
    	}
		synchronized(startedSavingDataLock){
			startedSavingData = false;
		}
	}
	public static boolean start(){
		synchronized (dataCollectionLock) {
			if(no_dataCollectionEventListeners<1) {
				no_dataCollectionEventListeners++;
				logger.info("collecting data...");
				return true;
			}
		}
		return false;
	}
}
