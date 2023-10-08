package io.nwdaf.eventsubscription.datacollection.dummy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.DummyDataGenerator;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class DummyDataProducerListener{
    private static Integer no_dummyDataProducerEventListeners = 0;
	private static final Object dummyDataProducerLock = new Object();
	private static Boolean startedSavingData = false;
	private static final Object startedSavingDataLock = new Object();
    private static Logger logger = LoggerFactory.getLogger(DummyDataProducerListener.class);
    private List<NfLoadLevelInformation> nfloadinfos;
    private List<UeMobility> ueMobilities;

	@Autowired
	MetricsService metricsService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    public void onApplicationEvent(final DummyDataProducerEvent event){
        start();
        if(no_dummyDataProducerEventListeners>0){
            nfloadinfos=DummyDataGenerator.generateDummyNfloadLevelInfo(10);
            ueMobilities = DummyDataGenerator.generateDummyUeMobilities(10);
        }
        long start;
        while(no_dummyDataProducerEventListeners>0){
            start = System.nanoTime();
            for(NwdafEventEnum eType : NwdafEventEnum.values()){
                switch(eType){
                    case NF_LOAD:
                        nfloadinfos = DummyDataGenerator.changeNfLoadTimeDependentProperties(nfloadinfos);
                        for(int k=0;k<nfloadinfos.size();k++) {
                            try {
                                metricsService.create(nfloadinfos.get(k));
                                synchronized(startedSavingDataLock){
                                    startedSavingData = true;
                                }
                            }
                            catch(Exception e) {
                                logger.error("Failed to save dummy nfloadlevelinfo to timescaledb",e);
                                stop();
                                continue;
                            }
                        }
                        break;
                    case UE_MOBILITY:
                        ueMobilities = DummyDataGenerator.changeUeMobilitiesTimeDependentProperties(ueMobilities);
                        for(int k=0;k<ueMobilities.size();k++) {
                            try {
                                metricsService.create(ueMobilities.get(k));
                                synchronized(startedSavingDataLock){
                                    startedSavingData = true;
                                }
                            }
                            catch(Exception e) {
                                logger.error("Failed to save dummy ueMobilities to timescaledb",e);
                                stop();
                                continue;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            long diff = (System.nanoTime()-start) / 1000000l;
    		long wait_time = (long)Constants.MIN_PERIOD_SECONDS*1000l;
            if(diff<wait_time) {
	    		try {
					Thread.sleep(wait_time-diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();
                    continue;
				}
    		}
        }
        logger.info("Dummy Data Production stopped!");
        return;
    }

    public static Object getDummyDataProducerLock() {
        return dummyDataProducerLock;
    }
    public static Integer getNo_dummyDataProducerEventListeners() {
        return no_dummyDataProducerEventListeners;
    }
    public static Object getStartedSavingDataLock() {
        return startedSavingDataLock;
    }
    public static Boolean getStartedSavingData() {
        return startedSavingData;
    }

    public static void stop(){
        synchronized (dummyDataProducerLock) {
			no_dummyDataProducerEventListeners--;
		}
		synchronized(startedSavingDataLock){
			startedSavingData = false;
		}
    }
    public static void start(){
        synchronized (dummyDataProducerLock) {
			if(no_dummyDataProducerEventListeners<1) {
				no_dummyDataProducerEventListeners++;
                logger.info("producing dummy data...");
			}
		}
    }
}
