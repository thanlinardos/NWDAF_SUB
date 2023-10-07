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
import lombok.Getter;
import lombok.Setter;

@Component
public class KafkaConsumer {
    
	@Autowired
	MetricsService metricsService;

	@Autowired
	ObjectMapper objectMapper;
	
	@Getter
	private static Boolean startedReceivingData = false;
	@Getter
	private static final Object startedReceivingDataLock = new Object();

	@Getter
	private static Boolean startedSavingData = false;
	@Getter
	private static final Object startedSavingDataLock = new Object();

	@Getter @Setter
	private static Boolean isListening = true;
	@Getter
	private static final Object isListeningLock = new Object();

	@Getter
	private static Boolean startedDiscoveringCollectors = false;
	@Getter
	private static final Object startedDiscoveringCollectorsLock = new Object();

	@Getter @Setter
	private static Boolean isDiscovering = true;
	@Getter
	private static final Object isDiscoveringLock = new Object();

	public static SynchronousQueue<String> discoverMessageQueue = new SynchronousQueue<>();

    @KafkaListener(topics = {"NF_LOAD","UE_MOBILITY"}, groupId = "event", containerFactory = "kafkaListenerContainerFactoryEvent")
	public String dataListener(ConsumerRecord<String,String> record){
		String topic = record.topic();
		String in = record.value();
		if(!isListening){
			return "";
		}
		startedReceiving();
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

	@KafkaListener(topics = {"DISCOVER"}, groupId = "nwdaf_sub_discover", containerFactory = "kafkaListenerContainerFactoryDiscover")
	public String discoverListener(ConsumerRecord<String,String> record){
		String topic = record.topic();
		String in = record.value();
		if(!isDiscovering){
			return "";
		}
		try{
			switch(topic){
				case "DISCOVER":
					discoverMessageQueue.put(in);
					startedDiscovering();
					break;
				default:
					break;
			}
		}catch(InterruptedException e){
			NwdafSubApplication.getLogger().error("failed to add discover msg to queue",e);
			stopListening();
			return "";
		}
		startedSaving();
		return in;
	}
	public static void startedSaving(){
		synchronized(startedSavingDataLock){
			startedSavingData = true;
		}
	}
	public static void startedReceiving(){
		synchronized(startedReceivingDataLock){
			startedReceivingData = true;
		}
	}
	public static void startedDiscovering(){
		synchronized(startedDiscoveringCollectorsLock){
			startedDiscoveringCollectors = true;
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
}
