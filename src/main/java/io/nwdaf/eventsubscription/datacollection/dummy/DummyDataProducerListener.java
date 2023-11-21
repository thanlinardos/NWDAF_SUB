package io.nwdaf.eventsubscription.datacollection.dummy;

import java.util.List;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.DummyDataGenerator;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class DummyDataProducerListener{
    @Getter
    private static Integer no_dummyDataProducerEventListeners = 0;
	@Getter
    private static final Object dummyDataProducerLock = new Object();
	@Getter
    private static Boolean startedSavingData = false;
	@Getter
    private static final Object startedSavingDataLock = new Object();
    private static final Logger logger = LoggerFactory.getLogger(DummyDataProducerListener.class);
    private List<NfLoadLevelInformation> nfloadinfos;
    private List<UeMobility> ueMobilities;

	final MetricsService metricsService;

    final MetricsCacheService metricsCacheService;
	
	final Environment env;

    public DummyDataProducerListener(MetricsService metricsService, MetricsCacheService metricsCacheService, Environment env) {
        this.metricsService = metricsService;
        this.metricsCacheService = metricsCacheService;
        this.env = env;
    }

    @Async
    @EventListener
    public void onApplicationEvent(final DummyDataProducerEvent event){
        if(!start()) {
			return;
		}
        if(no_dummyDataProducerEventListeners>0){
            nfloadinfos=DummyDataGenerator.generateDummyNfLoadLevelInfo(10);
            ueMobilities = DummyDataGenerator.generateDummyUeMobilities(10);
        }
        long start;
        while(no_dummyDataProducerEventListeners>0){
            start = System.nanoTime();
            for(NwdafEventEnum eType : NwdafEventEnum.values()){
                switch(eType){
                    case NF_LOAD:
                        nfloadinfos = DummyDataGenerator.changeNfLoadTimeDependentProperties(nfloadinfos);
                        for (NfLoadLevelInformation nfloadinfo : nfloadinfos) {
                            try {
                                metricsService.createNfload(nfloadinfo);
                                synchronized (startedSavingDataLock) {
                                    startedSavingData = true;
                                }
                            } catch (Exception e) {
                                logger.error("Failed to save dummy nfloadlevelinfo to timescaledb", e);
                                stop();
                                continue;
                            }
                        }
                        break;
                    case UE_MOBILITY:
                        ueMobilities = DummyDataGenerator.changeUeMobilitiesTimeDependentProperties(ueMobilities);
                        for (UeMobility ueMobility : ueMobilities) {
                            try {
                                metricsService.createUeMob(ueMobility);
                                synchronized (startedSavingDataLock) {
                                    startedSavingData = true;
                                }
                            } catch (Exception e) {
                                logger.error("Failed to save dummy ueMobilities to timescaledb", e);
                                stop();
                                continue;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            long diff = (System.nanoTime()-start) / 1000000L;
    		long wait_time = (long)Constants.MIN_PERIOD_SECONDS* 1000L;
            if(diff<wait_time) {
	    		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					logger.error("Failed to wait for thread...",e);
					stop();
                    continue;
				}
    		}
        }
        logger.info("Dummy Data Production stopped!");
    }

    public static void stop(){
        synchronized (dummyDataProducerLock) {
			no_dummyDataProducerEventListeners--;
		}
		synchronized(startedSavingDataLock){
			startedSavingData = false;
		}
    }
    
    public static boolean start() {
        synchronized (dummyDataProducerLock) {
			if(no_dummyDataProducerEventListeners<1) {
				no_dummyDataProducerEventListeners++;
                logger.info("producing dummy data...");
                return true;
			}
		}
        return false;
    }
}
