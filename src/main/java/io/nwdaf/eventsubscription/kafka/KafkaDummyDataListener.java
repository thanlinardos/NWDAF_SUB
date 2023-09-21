package io.nwdaf.eventsubscription.kafka;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.datacollection.DummyDataGenerator;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.utilities.Constants;

@Component
public class KafkaDummyDataListener {
    private static Integer no_kafkaDummyDataListeners = 0;
	private static final Object kafkaDummyDataLock = new Object();
	private static Boolean started_sending_data = false;
	private static final Object started_sending_data_lock = new Object();
	
	@Autowired
	Environment env;
    
    @Autowired
    KafkaProducer producer;

    @Value(value = "${nnwdaf-eventsubscription.kafka.topic}")
    String topicName;
	
    @Autowired
    ObjectMapper objectMapper;

    @Async
    @EventListener
    public void onApplicationEvent(final KafkaDummyDataEvent event){
        synchronized (kafkaDummyDataLock) {
			if(no_kafkaDummyDataListeners<1) {
				no_kafkaDummyDataListeners++;
			}
			else {
				return;
			}
		}

        Logger logger = NwdafSubApplication.getLogger();
    	logger.info("producing dummy data to send to kafka...");
        long start;
        List<NfLoadLevelInformation> nfloadinfos=DummyDataGenerator.generateDummyNfloadLevelInfo(10);
        List<UeMobility> ueMobilities = DummyDataGenerator.generateDummyUeMobilities(10);
        while(no_kafkaDummyDataListeners>0){
            start = System.nanoTime();
            for(int j=0;j<NwdafEventEnum.values().length;j++){
                NwdafEventEnum eType = NwdafEventEnum.values()[j];
                if(eType.equals(NwdafEventEnum.NF_LOAD)){
                    nfloadinfos = DummyDataGenerator.changeNfLoadTimeDependentProperties(nfloadinfos);
                    for(int k=0;k<nfloadinfos.size();k++) {
                        try {
                            producer.sendMessage(objectMapper.writeValueAsString(nfloadinfos.get(k)), Optional.of(topicName));
                            synchronized(started_sending_data_lock){
                                started_sending_data = true;
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
                            producer.sendMessage(objectMapper.writeValueAsString(ueMobilities.get(k)), Optional.of(topicName));
                            synchronized(started_sending_data_lock){
                                started_sending_data = true;
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

    public static Object getKafkadummydatalock() {
        return kafkaDummyDataLock;
    }
    public static Integer getNo_kafkaDummyDataListeners() {
        return no_kafkaDummyDataListeners;
    }
    public static Object getStartedSendingDataLock() {
        return started_sending_data_lock;
    }
    public static Boolean getStarted_sending_data() {
        return started_sending_data;
    }

    private void stop(){
        synchronized (kafkaDummyDataLock) {
			no_kafkaDummyDataListeners--;
		}
		synchronized(started_sending_data_lock){
			started_sending_data = false;
		}
    }
}
