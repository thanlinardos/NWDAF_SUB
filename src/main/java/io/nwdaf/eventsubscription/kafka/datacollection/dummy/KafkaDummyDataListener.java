package io.nwdaf.eventsubscription.kafka.datacollection.dummy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.DummyDataGenerator;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.utilities.Constants;

@Component
public class KafkaDummyDataListener {
    private static Integer no_kafkaDummyDataListeners = 0;
	private static final Object kafkaDummyDataLock = new Object();
	private static Boolean startedSendingData = false;
	private static final Object startedSendingDataLock = new Object();
	private static Logger logger = LoggerFactory.getLogger(KafkaDummyDataListener.class);
    private List<NfLoadLevelInformation> nfloadinfos;
    private List<UeMobility> ueMobilities;

	@Autowired
	Environment env;
    
    @Autowired
    KafkaProducer producer;
	
    @Autowired
    ObjectMapper objectMapper;

    @Async
    @EventListener
    public void onApplicationEvent(final KafkaDummyDataEvent event){
        start();
        if(no_kafkaDummyDataListeners>0){
            nfloadinfos=DummyDataGenerator.generateDummyNfloadLevelInfo(10);
            ueMobilities = DummyDataGenerator.generateDummyUeMobilities(10);
        }
        long start;
        while(no_kafkaDummyDataListeners>0){
            start = System.nanoTime();
            for(int j=0;j<NwdafEventEnum.values().length;j++){
                NwdafEventEnum eType = NwdafEventEnum.values()[j];
                if(eType.equals(NwdafEventEnum.NF_LOAD)){
                    nfloadinfos = DummyDataGenerator.changeNfLoadTimeDependentProperties(nfloadinfos);
                    for(int k=0;k<nfloadinfos.size();k++) {
                        try {
                            producer.sendMessage(objectMapper.writeValueAsString(nfloadinfos.get(k)), eType.toString());
                            synchronized(startedSendingDataLock){
                                startedSendingData = true;
                            }
                        }
                        catch(Exception e) {
                            logger.error("Failed to send dummy nfloadlevelinfo to broker",e);
                            this.stop();
                            continue;
                        }
                    }
                }
                else if(eType.equals(NwdafEventEnum.UE_MOBILITY)){
                    ueMobilities = DummyDataGenerator.changeUeMobilitiesTimeDependentProperties(ueMobilities);
                    for(int k=0;k<ueMobilities.size();k++) {
                        try {
                            producer.sendMessage(objectMapper.writeValueAsString(ueMobilities.get(k)), eType.toString());
                            synchronized(startedSendingDataLock){
                                startedSendingData = true;
                            }
                        }
                        catch(Exception e) {
                            logger.error("Failed to send dummy ueMobilities to broker",e);
                            this.stop();
                            continue;
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
					this.stop();
                    continue;
				}
    		}
        }
        logger.info("Dummy Data Production stopped!");
        return;
    }

    public static Object getKafkaDummyDataLock() {
        return kafkaDummyDataLock;
    }
    public static Integer getNo_kafkaDummyDataListeners() {
        return no_kafkaDummyDataListeners;
    }
    public static Object getStartedSendingDataLock() {
        return startedSendingDataLock;
    }
    public static Boolean getStartedSendingData() {
        return startedSendingData;
    }

    private void stop(){
        synchronized (kafkaDummyDataLock) {
			no_kafkaDummyDataListeners--;
		}
		synchronized(startedSendingDataLock){
			startedSendingData = false;
		}
    }
    public static void start(){
        synchronized (kafkaDummyDataLock) {
			if(no_kafkaDummyDataListeners<1) {
				no_kafkaDummyDataListeners++;
                logger.info("producing dummy data to send to kafka...");
                
			}
		}
    }
}
