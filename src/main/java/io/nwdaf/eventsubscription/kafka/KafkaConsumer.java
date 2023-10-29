package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;

@Component
public class KafkaConsumer {
    
	@Autowired
	MetricsCacheService metricsCacheService;

	@Autowired
	MetricsService metricsService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	@Qualifier("consumerDiscover")
    private Consumer<String, String> kafkaConsumerDiscover;

	@Autowired
	@Qualifier("consumerEvent")
    private Consumer<String, String> kafkaConsumerEvent;
	
	public static Boolean startedReceivingData = false;
	public static final Object startedReceivingDataLock = new Object();
	public static Boolean startedSavingData = false;
	public static final Object startedSavingDataLock = new Object();
	public static Boolean isListening = true;
	public static final Object isListeningLock = new Object();
	public static Boolean startedDiscoveringCollectors = false;
	public static final Object startedDiscoveringCollectorsLock = new Object();

	public static Boolean isDiscovering = true;
	public static final Object isDiscoveringLock = new Object();

	public static BlockingQueue<String> discoverMessageQueue = new LinkedBlockingQueue<>();

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
				// metricsCacheService.create(nfLoadLevelInformation);
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
			NwdafSubApplication.getLogger().error("",e);
			stopListening();
			return "";
		}
		startedSaving();
		return in;
	}

	@Scheduled(fixedDelay = 1000)
	public void discoverListener(){
		if(!isDiscovering){
			return;
		}
		List<PartitionInfo> partitions = kafkaConsumerDiscover.partitionsFor("DISCOVER");

        // Get the beginning offset for each partition and convert it to a timestamp
        long earliestTimestamp = Long.MAX_VALUE;
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (PartitionInfo partition : partitions) {
            TopicPartition topicPartition = new TopicPartition("DISCOVER", partition.partition());
            topicPartitions.add(topicPartition);
            kafkaConsumerDiscover.assign(Collections.singletonList(topicPartition));
            kafkaConsumerDiscover.seekToBeginning(Collections.singletonList(topicPartition));

            long beginningOffset = kafkaConsumerDiscover.position(topicPartition);
			OffsetAndTimestamp offsetAndTimestamp = kafkaConsumerDiscover.offsetsForTimes(Collections.singletonMap(topicPartition, beginningOffset)).get(topicPartition);
            if(offsetAndTimestamp!=null){
				long partitionTimestamp = offsetAndTimestamp.timestamp();
				if (partitionTimestamp < earliestTimestamp) {
					earliestTimestamp = partitionTimestamp;
				}
			}
        }
		if(earliestTimestamp == Long.MAX_VALUE){
			return;
		}
        // Convert the earliest timestamp to a human-readable format
        // String formattedTimestamp = Instant.ofEpochMilli(earliestTimestamp).toString();
        // System.out.println("Earliest Timestamp in the DISCOVER topic: " + formattedTimestamp);

        // Set the desired timestamps for the beginning and end of the range
        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusSeconds(1).toString()).toEpochMilli();

        // Seek to the beginning timestamp
        for (TopicPartition partition : topicPartitions) {
			OffsetAndTimestamp offsetAndTimestamp = kafkaConsumerDiscover.offsetsForTimes(Collections.singletonMap(partition, startTimestamp)).get(partition);
            if(offsetAndTimestamp!=null){
				kafkaConsumerDiscover.seek(partition, offsetAndTimestamp.offset());
			}
        }

	    // consume messages inside the desired range
        ConsumerRecords<String, String> records = kafkaConsumerDiscover.poll(Duration.ofMillis(1000));

        // Process the received messages here
        records.forEach(record -> {
            // Check if the message timestamp is within the desired range
            if(record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                System.out.println("Received message: " + record.value());
                if(!discoverMessageQueue.offer(record.value())) {
                    System.out.println("InterruptedException while writing to DISCOVER message queue.");
					stopDiscovering();
                }
				else{
					startedDiscovering();
				}
            }
        });
	}
	public static void startedSaving(){
		synchronized(startedSavingDataLock){
			startedSavingData = true;
		}
	}
    public static void stoppedSaving(){
		synchronized(startedSavingDataLock){
			startedSavingData = false;
		}
	}
	public static void startListening(){
		synchronized(isListeningLock){
			isListening = true;
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
	public static void startedDiscovering(){
		synchronized(startedDiscoveringCollectorsLock){
			startedDiscoveringCollectors = true;
		}
	}
	public static void stopDiscovering(){
		synchronized(startedDiscoveringCollectorsLock){
			startedDiscoveringCollectors = false;
		}
	}
	public static void startedReceiving(){
		synchronized(startedReceivingDataLock){
			startedReceivingData = true;
		}
	}
	public static void stopReceiving(){
		synchronized(startedReceivingDataLock){
			startedReceivingData = false;
		}
	}
}
