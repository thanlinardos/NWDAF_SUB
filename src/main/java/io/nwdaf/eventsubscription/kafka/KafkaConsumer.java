package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;

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

	private static Boolean startedReceivingData = false;
	private static final Object startedReceivingDataLock = new Object();

	private static Boolean startedSavingData = false;
	private static final Object startedSavingDataLock = new Object();

	private static Boolean isListening = true;
	private static final Object isListeningLock = new Object();

    @KafkaListener(topics = "topic1")
	public String listens(final String in){
		if(isListening){
			startedReceivingData = true;
			// System.out.println(in);
			NfLoadLevelInformation nfLoadLevelInformation = null;
			UeMobility ueMobility = null;
			try{
				nfLoadLevelInformation = objectMapper.reader().readValue(in, NfLoadLevelInformation.class);
			} catch(IOException e){
				try{
					ueMobility = objectMapper.reader().readValue(in, UeMobility.class);
				} catch(IOException e1){
					NwdafSubApplication.getLogger().info("data not matching any models");
					return "";
				}
			}
			if(nfLoadLevelInformation!=null){
				try{
					metricsService.create(nfLoadLevelInformation);
				} catch(Exception e){
					NwdafSubApplication.getLogger().info("failed to save nfload info to timescaleDB");
					stopListening();
					return "";
				}
			}
			else if(ueMobility!=null){
				try{
					metricsService.create(ueMobility);
				} catch(Exception e){
					NwdafSubApplication.getLogger().info("failed to save ueMobility info to timescaleDB");
					stopListening();
					return "";
				}
			}
			startedSaving();
		}
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
