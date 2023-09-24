package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class KafkaConsumer {
    
	@Autowired
	MetricsService metricsService;

	@Autowired
	ObjectMapper objectMapper;

	public static Boolean startedReceivingData = false;
	public static final Object startedReceivingDataLock = new Object();

	public static Boolean startedSavingData = false;
	public static final Object startedSavingDataLock = new Object();

	public static Boolean isListening = true;
	public static final Object isListeningLock = new Object();

	public static SynchronousQueue<String> discoverMessageQueue = new SynchronousQueue<>();

    @KafkaListener(topics = {"NF_LOAD","UE_MOBILITY","DISCOVER"})
	public String dataListener(ConsumerRecord<String,String> record){
		String topic = record.topic();
		String in = record.value();
		if(!isListening){
			return "";
		}
		startedReceivingData = true;
		// System.out.println(in);
		NfLoadLevelInformation nfLoadLevelInformation = null;
		UeMobility ueMobility = null;
		try{
		switch(topic){
			case "NF_LOAD":
				nfLoadLevelInformation = objectMapper.reader().readValue(in, NfLoadLevelInformation.class);
				metricsService.create(nfLoadLevelInformation);
				break;
			case "UE_MOBILITY":
				ueMobility = objectMapper.reader().readValue(in, UeMobility.class);
				metricsService.create(ueMobility);
				break;
			case "DISCOVER":
				discoverMessageQueue.put(in);
				break;
			default:
				break;
		}
		} catch(IOException e){
			NwdafSubApplication.getLogger().info("data not matching "+topic+" model: "+in);
			return "";
		} catch(Exception e){
			NwdafSubApplication.getLogger().info("failed to save "+topic+" info to timescaleDB");
			stopListening();
			return "";
		}
		startedSaving();
		return in;
	}

	public static Object getStartedsavingdatalock() {
		return startedSavingDataLock;
	}

	public static Boolean getStartedSavingData() {
		return startedSavingData;
	}

	public static Boolean getIsListening() {
		return isListening;
	}

	public static Object getIslisteninglock() {
		return isListeningLock;
	}

	public static void startedSaving(){
		synchronized(startedSavingDataLock){
			startedSavingData = true;
		}
	}
	
	public static void stopListening(){
		synchronized(isListeningLock){
			isListening = false;
		}
		synchronized(startedSavingDataLock){
			startedSavingData = false;
		}
		synchronized(startedReceivingDataLock){
			startedReceivingData = false;
		}
	}
	public static void startListening(){
		synchronized(isListeningLock){
			isListening = true;
		}
	}

	public static Object getStartedReceivingDataLock() {
		return startedReceivingDataLock;
	}
	public static Boolean getStartedReceivingData() {
		return startedReceivingData;
	}
}
