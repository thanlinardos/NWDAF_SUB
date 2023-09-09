package io.nwdaf.eventsubscription.datacollection;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class DummyDataProducerListener{
    private static Integer no_dummyDataProducerEventListeners = 0;
	private static final Object dummyDataProducerLock = new Object();
	private static Boolean started_saving_data = false;
	private static final Object started_saving_data_lock = new Object();

	@Autowired
	MetricsService metricsService;
	
	@Autowired
	Environment env;
	
    @Async
    @EventListener
    public void onApplicationEvent(final DummyDataProducerEvent event){
        synchronized (dummyDataProducerLock) {
			if(no_dummyDataProducerEventListeners<1) {
				no_dummyDataProducerEventListeners++;
			}
			else {
				return;
			}
		}

        Logger logger = NwdafSubApplication.getLogger();
    	logger.info("producing dummy data...");
        long start;
        List<NfLoadLevelInformation> nfloadinfos=DummyDataGenerator.generateDummyNfloadLevelInfo(10);
        List<UeMobility> ueMobilities = DummyDataGenerator.generateDummyUeMobilities(10);
        while(true){
            start = System.nanoTime();
            synchronized(dummyDataProducerLock){
                if(no_dummyDataProducerEventListeners==0){
                    logger.info("Dummy Data Production stopped!");
                    return;
                }
            }
            for(int j=0;j<NwdafEventEnum.values().length;j++){
                NwdafEventEnum eType = NwdafEventEnum.values()[j];
                if(eType.equals(NwdafEventEnum.NF_LOAD)){
                    nfloadinfos = DummyDataGenerator.changeNfLoadTimeDependentProperties(nfloadinfos);
                    for(int k=0;k<nfloadinfos.size();k++) {
                        try {
                            metricsService.create(nfloadinfos.get(k));
                            synchronized(started_saving_data_lock){
                                started_saving_data = true;
                            }
                        }
                        catch(Exception e) {
                            logger.error("Failed to save dummy nfloadlevelinfo to timescaledb",e);
                            synchronized (dummyDataProducerLock) {
                                no_dummyDataProducerEventListeners--;
                            }
                            synchronized(started_saving_data_lock){
                                started_saving_data = false;
                            }
                            return;
                        }
                    }
                }
                else if(eType.equals(NwdafEventEnum.UE_MOBILITY)){
                    ueMobilities = DummyDataGenerator.changeUeMobilitiesTimeDependentProperties(ueMobilities);
                    for(int k=0;k<ueMobilities.size();k++) {
                        try {
                            metricsService.create(ueMobilities.get(k));
                            synchronized(started_saving_data_lock){
                                started_saving_data = true;
                            }
                        }
                        catch(Exception e) {
                            logger.error("Failed to save dummy ueMobilities to timescaledb",e);
                            synchronized (dummyDataProducerLock) {
                                no_dummyDataProducerEventListeners--;
                            }
                            synchronized(started_saving_data_lock){
                                started_saving_data = false;
                            }
                            return;
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
					synchronized (dummyDataProducerLock) {
			    		no_dummyDataProducerEventListeners--;
			    	}
					synchronized(started_saving_data_lock){
						started_saving_data = false;
					}
					return;
				}
    		}
        }
    }

    public static Object getDummydataproducerlock() {
        return dummyDataProducerLock;
    }
    public static Integer getNo_dummyDataProducerEventListeners() {
        return no_dummyDataProducerEventListeners;
    }
    public static Object getStartedSavingDataLock() {
        return started_saving_data_lock;
    }
    public static Boolean getStarted_saving_data() {
        return started_saving_data;
    }
}
