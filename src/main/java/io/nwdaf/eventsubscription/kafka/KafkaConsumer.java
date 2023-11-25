package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import io.nwdaf.eventsubscription.customModel.DiscoverMessage;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.UeCommunication;
import io.nwdaf.eventsubscription.utilities.Constants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;

import static io.nwdaf.eventsubscription.utilities.Constants.supportedEvents;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    final MetricsCacheService metricsCacheService;

    final MetricsService metricsService;

    final ObjectMapper objectMapper;

    private final Consumer<String, String> kafkaConsumerDiscover;

    private final Consumer<String, String> kafkaConsumerEvent;
    public static AtomicBoolean isListening = new AtomicBoolean(true);
    public static AtomicBoolean isDiscovering = new AtomicBoolean(true);
    public static final BlockingQueue<String> discoverMessageQueue = new LinkedBlockingQueue<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, DiscoverMessage> latestDiscoverMessageEventMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, WakeUpMessage> latestWakeUpMessageEventMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Long> eventConsumerCounters = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Long> eventConsumerLastSaves = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerStartedSaving = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerStartedReceiving = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerIsSyncing = new ConcurrentHashMap<>();

    public static OffsetDateTime startTime;
    public static OffsetDateTime endTime;

    @Value("${nnwdaf-eventsubscription.log.consumer}")
    private Boolean logConsumer;

    public KafkaConsumer(MetricsCacheService metricsCacheService,
                         MetricsService metricsService,
                         ObjectMapper objectMapper,
                         @Qualifier("consumerDiscover") Consumer<String, String> kafkaConsumerDiscover,
                         @Qualifier("consumerEvent") Consumer<String, String> kafkaConsumerEvent) {

        this.metricsCacheService = metricsCacheService;
        this.metricsService = metricsService;
        this.objectMapper = objectMapper;
        this.kafkaConsumerDiscover = kafkaConsumerDiscover;
        this.kafkaConsumerEvent = kafkaConsumerEvent;

        for (NwdafEvent.NwdafEventEnum e : supportedEvents) {
            eventConsumerCounters.put(e, 0L);
            eventConsumerLastSaves.put(e, 0L);
            eventConsumerStartedSaving.put(e, false);
            eventConsumerStartedReceiving.put(e, false);
            eventConsumerIsSyncing.put(e, false);
        }
    }

    // Consumer for metrics since start offset of topic
    @KafkaListener(topics = {"NF_LOAD", "UE_MOBILITY", "UE_COMM"}, groupId = "event", containerFactory = "kafkaListenerContainerFactoryEvent",
            autoStartup = "true")
    public String dataListener(ConsumerRecord<String, String> record) {
        NwdafEvent.NwdafEventEnum eventTopic = NwdafEvent.NwdafEventEnum.valueOf(record.topic());
        String in = record.value();
        long timeDiff = System.currentTimeMillis() - record.timestamp() - Constants.MIN_PERIOD_SECONDS * 1_000L;
        if (!isListening.get() || timeDiff <= 0) {
            if (timeDiff < -Constants.MIN_PERIOD_SECONDS * 1_000L && eventConsumerIsSyncing.get(eventTopic)) {
                logger.warn("Stopped syncing for event: " + eventTopic + " (" + timeDiff / 1_000L + " seconds behind)");
                stopSyncing(eventTopic);
            }
            return "";
        }
        if (!eventConsumerIsSyncing.get(eventTopic) && timeDiff > Constants.MIN_PERIOD_SECONDS * 1_000L) {
            logger.warn("Started syncing for event: " + eventTopic + " (" + timeDiff / 1_000L + " seconds behind)");
        }
        startedSyncing(eventTopic);
        try {
            consumeMetric(eventTopic, in, record.timestamp(), false);
        } catch (IOException e) {
            logger.warn("data not matching " + eventTopic + " model: " + in);
            return "";
        } catch (Exception e) {
            logger.info("failed to save " + eventTopic + " info to timescaleDB");
            logger.error("", e);
            stopListening(eventTopic);
            return "";
        }
        startedSaving(eventTopic);
        return in;
    }

    @Scheduled(fixedDelay = 1)
    public void discoverListener() {
        if (!isDiscovering.get()) {
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
            if (offsetAndTimestamp != null) {
                long partitionTimestamp = offsetAndTimestamp.timestamp();
                if (partitionTimestamp < earliestTimestamp) {
                    earliestTimestamp = partitionTimestamp;
                }
            }
        }
        if (earliestTimestamp == Long.MAX_VALUE) {
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
            if (offsetAndTimestamp != null) {
                kafkaConsumerDiscover.seek(partition, offsetAndTimestamp.offset());
            }
        }

        // consume messages inside the desired range
        ConsumerRecords<String, String> records = kafkaConsumerDiscover.poll(Duration.ofMillis(1));

        // Process the received messages here
        records.forEach(record -> {
            // Check if the message timestamp is within the desired range
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
//                System.out.println("Received message: " + record.value());
                if (!discoverMessageQueue.offer(record.value())) {
                    System.out.println("InterruptedException while writing to DISCOVER message queue.");
                    stopDiscovering();
                } else {
                    startedDiscovering();
                }
            }
        });
    }

    // Consumer for metrics only within period
    @KafkaListener(topics = {"NF_LOAD", "UE_MOBILITY", "UE_COMM"}, groupId = "latestEvent", containerFactory = "kafkaListenerContainerFactoryEvent",
            autoStartup = "true",
            properties = {
                    "max.poll.records=1", // Consume only one record per poll
                    "auto.offset.reset=latest", // Start consuming from the latest offset
                    "enable.auto.commit=false",
                    "commitOffsetsOnFirstJoin=false"
            })
    public String concurrentDataListener(ConsumerRecord<String, String> record) {
        NwdafEvent.NwdafEventEnum eventTopic = NwdafEvent.NwdafEventEnum.valueOf(record.topic());
        String in = record.value();
        if (!isListening.get() || record.timestamp() < eventConsumerLastSaves.get(eventTopic) || System.currentTimeMillis() - record.timestamp() > Constants.MIN_PERIOD_SECONDS * 1_000L) {
            return "";
        }
        startedReceiving(eventTopic);

        try {
            consumeMetric(eventTopic, in, record.timestamp(), true);
        } catch (IOException e) {
            logger.info("data not matching " + eventTopic + " model: " + in);
            return "";
        } catch (Exception e) {
            logger.info("failed to save " + eventTopic + " info to timescaleDB");
            logger.error("", e);
            stopListening(eventTopic);
            return "";
        }
        startedSaving(eventTopic);
        return in;
    }

    private void consumeMetric(NwdafEvent.NwdafEventEnum eventTopic, String in, Long recordTimeStamp, Boolean isLatest) throws IOException, Exception {
        switch (eventTopic) {
            case NF_LOAD:
                NfLoadLevelInformation nfLoadLevelInformation = objectMapper.reader().readValue(in, NfLoadLevelInformation.class);
                // metricsCacheService.create(nfLoadLevelInformation);
                metricsService.createNfload(nfLoadLevelInformation);
                break;
            case UE_MOBILITY:
                UeMobility ueMobility = objectMapper.reader().readValue(in, UeMobility.class);
                metricsService.createUeMob(ueMobility);
                break;
            case UE_COMM:
                UeCommunication ueCommunication = objectMapper.reader().readValue(in, UeCommunication.class);
                metricsService.createUeComm(ueCommunication);
                break;
            default:
                break;
        }
        setupAndLogCounters(eventTopic, recordTimeStamp, isLatest, 1);
    }

    private void consumeMetrics(NwdafEvent.NwdafEventEnum eventTopic, List<String> in, Long recordTimeStamp, Boolean isLatest) throws IOException, Exception {
        switch (eventTopic) {
            case NF_LOAD:
                List<NfLoadLevelInformation> nfLoadLevelInformations = in.stream().map(s -> {
                    try {
                        return objectMapper.reader().readValue(s, NfLoadLevelInformation.class);
                    } catch (IOException e) {
                        logger.warn("data not matching " + eventTopic + " model: " + in);
                    }
                    return null;
                }).toList();
                // metricsCacheService.create(nfLoadLevelInformation);
                metricsService.createAllNfload(nfLoadLevelInformations);
                break;
            case UE_MOBILITY:
                List<UeMobility> ueMobilities = in.stream().map(s -> {
                    try {
                        return objectMapper.reader().readValue(s, UeMobility.class);
                    } catch (IOException e) {
                        logger.warn("data not matching " + eventTopic + " model: " + in);
                    }
                    return null;
                }).toList();
                metricsService.createAllUeMobs(ueMobilities);
                break;
            case UE_COMM:
                List<UeCommunication> ueCommunications = in.stream().map(s -> {
                    try {
                        return objectMapper.reader().readValue(s, UeCommunication.class);
                    } catch (IOException e) {
                        logger.warn("data not matching " + eventTopic + " model: " + in);
                    }
                    return null;
                }).toList();
                metricsService.createAllUeCommms(ueCommunications);
                break;
            default:
                break;
        }
        setupAndLogCounters(eventTopic, recordTimeStamp, isLatest, in.size());
    }

    private void setupAndLogCounters(NwdafEvent.NwdafEventEnum eventTopic, Long recordTimeStamp, Boolean isLatest, Integer length) {
        if (isLatest) {
            eventConsumerLastSaves.put(eventTopic, recordTimeStamp);
            eventConsumerCounters.compute(eventTopic, (k, v) -> (v == null || v < 0) ? 0 : v + length);
            if (logConsumer) {
                if (eventConsumerCounters.get(eventTopic) % 100 == 0) {
                    logger.info("Saved " + eventTopic + "(x100) to database.");
                }
            }
        }
    }

    public static void startedSaving(NwdafEvent.NwdafEventEnum eventTopic) {
        eventConsumerStartedSaving.compute(eventTopic, (k, v) -> true);
    }

    public static void stoppedSaving(NwdafEvent.NwdafEventEnum eventTopic) {
        eventConsumerStartedSaving.compute(eventTopic, (k, v) -> false);
    }

    public static void startListening() {
        isListening.set(true);
    }

    public static void stopListening(NwdafEvent.NwdafEventEnum eventTopic) {
        isListening.set(false);
        stoppedSaving(eventTopic);
        stopReceiving(eventTopic);
    }

    public static void startedDiscovering() {
        isDiscovering.set(true);
    }

    public static void stopDiscovering() {
        isDiscovering.set(false);
    }

    public static void startedReceiving(NwdafEvent.NwdafEventEnum eventTopic) {
        if (!eventConsumerStartedReceiving.get(eventTopic)) {
            eventConsumerStartedReceiving.compute(eventTopic, (k, v) -> true);
            startTime = OffsetDateTime.now();
            eventConsumerCounters.compute(eventTopic, (k, v) -> 0L);
        }
    }

    public static void stopReceiving(NwdafEvent.NwdafEventEnum eventTopic) {
        if (eventConsumerStartedReceiving.get(eventTopic)) {
            eventConsumerStartedReceiving.compute(eventTopic, (k, v) -> false);
            endTime = OffsetDateTime.now();
        }
        stoppedSaving(eventTopic);
    }

    public static void startedSyncing(NwdafEvent.NwdafEventEnum eventTopic) {
        eventConsumerIsSyncing.compute(eventTopic, (k, v) -> true);
    }

    public static void stopSyncing(NwdafEvent.NwdafEventEnum eventTopic) {
        eventConsumerStartedReceiving.compute(eventTopic, (k, v) -> false);
    }
}
