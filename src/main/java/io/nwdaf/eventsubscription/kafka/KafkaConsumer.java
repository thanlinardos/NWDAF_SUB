package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.nwdaf.eventsubscription.customModel.*;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.UeCommunication;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import org.springframework.transaction.TransactionSystemException;

import static io.nwdaf.eventsubscription.NwdafSubApplication.NWDAF_INSTANCE_ID;
import static io.nwdaf.eventsubscription.kafka.KafkaNotifierLoadBalanceConsumer.setupAoIs;
import static io.nwdaf.eventsubscription.service.StartUpService.getAssignedPartition;
import static io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum.NF_LOAD;
import static io.nwdaf.eventsubscription.service.StartUpService.refreshRegisteredAOIs;
import static io.nwdaf.eventsubscription.utilities.Constants.*;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.consume", havingValue = "true")
@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    final MetricsCacheService metricsCacheService;
    final MetricsService metricsService;
    final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    private final Consumer<String, String> kafkaConsumerDiscover;
    private final ThreadLocal<Consumer<String, String>> kafkaConsumerEventThreadLocal;
    private final Consumer<String, String> kafkaConsumerRegisterConsumer;
    private final Consumer<String, String> kafkaConsumerAssignPartitions;
    private final SubscriptionsService subscriptionsService;

    public static final AtomicBoolean isListening = new AtomicBoolean(true);
    public static final AtomicBoolean isDiscovering = new AtomicBoolean(true);
    public static final BlockingQueue<String> discoverMessageQueue = new LinkedBlockingQueue<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, DiscoverMessage> latestDiscoverMessageEventMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, WakeUpMessage> latestWakeUpMessageEventMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Long> eventConsumerCounters = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Long> eventConsumerLastSaves = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerStartedSaving = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerStartedReceiving = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, Boolean> eventConsumerIsSyncing = new ConcurrentHashMap<>();
    public static final Set<UUID> eventCollectorIdSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static ConcurrentHashMap<UUID, RegisterConsumerMessage> consumerInstances = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, AssignPartitionsMessage> assignedPartitions = new ConcurrentHashMap<>();

    public static OffsetDateTime startTime;
    public static OffsetDateTime endTime;

    @Value("${nnwdaf-eventsubscription.log.consumer}")
    private Boolean logConsumer;
    @Value("${nnwdaf-eventsubscription.leader}")
    private boolean leader;
    private Future<?> scheduledTask;
    @Value("${nnwdaf-eventsubscription.notifier}")
    private boolean notifier;
    @Value("${nnwdaf-eventsubscription.nef_group_id}")
    private String nefGroupId;

    public KafkaConsumer(MetricsCacheService metricsCacheService,
                         MetricsService metricsService,
                         ObjectMapper objectMapper,
                         @Qualifier("consumerDiscover") Consumer<String, String> kafkaConsumerDiscover,
                         @Qualifier("consumerFactoryEvent") ConsumerFactory<String, String> consumerFactory,
                         @Qualifier("consumerRegisterConsumer") Consumer<String, String> kafkaConsumerRegisterConsumer,
                         @Qualifier("consumerAssignPartitions") Consumer<String, String> kafkaConsumerAssignPartitions,
                         KafkaProducer kafkaProducer, SubscriptionsService subscriptionsService) {

        this.metricsCacheService = metricsCacheService;
        this.metricsService = metricsService;
        this.objectMapper = objectMapper;
        this.kafkaConsumerDiscover = kafkaConsumerDiscover;
        this.kafkaConsumerEventThreadLocal = ThreadLocal.withInitial(consumerFactory::createConsumer);
        this.kafkaConsumerRegisterConsumer = kafkaConsumerRegisterConsumer;
        this.kafkaConsumerAssignPartitions = kafkaConsumerAssignPartitions;
        this.kafkaProducer = kafkaProducer;
        this.subscriptionsService = subscriptionsService;

        for (NwdafEvent.NwdafEventEnum e : supportedEvents) {
            eventConsumerCounters.put(e, 0L);
            eventConsumerLastSaves.put(e, 0L);
            eventConsumerStartedSaving.put(e, false);
            eventConsumerStartedReceiving.put(e, false);
            eventConsumerIsSyncing.put(e, false);
        }
    }

    // Consumer for metrics since start offset of topic
    @KafkaListener(topics = {"NF_LOAD", "UE_MOBILITY", "UE_COMM"}, groupId = "syncEvent", containerFactory = "kafkaListenerContainerFactoryEvent",
            autoStartup = "true")
    public String dataListener(ConsumerRecord<String, String> record) {
        NwdafEvent.NwdafEventEnum eventTopic = NwdafEvent.NwdafEventEnum.valueOf(record.topic());
        String in = record.value();
        long timeDiff = System.currentTimeMillis() - record.timestamp() - MIN_PERIOD_MILLIS;
        if (getAssignedPartition() == -1) {
            return "";
        }
        if (!isListening.get() || timeDiff <= 0) {
            if (timeDiff < -MIN_PERIOD_MILLIS && eventConsumerIsSyncing.get(eventTopic)) {
                logger.warn("Stopped syncing for event: {} ({} seconds behind)", eventTopic, timeDiff / 1_000L);
                stopSyncing(eventTopic);
            }
            return "";
        }
        if (!eventConsumerIsSyncing.get(eventTopic) && timeDiff > MIN_PERIOD_MILLIS) {
            logger.warn("Started syncing for event: {} ({} seconds behind)", eventTopic, timeDiff / 1_000L);
        }
        startedSyncing(eventTopic);
        try {
            consumeMetric(eventTopic, in, record.timestamp(), false);
        } catch (JpaSystemException | TransactionSystemException | SQLException e) {
            logger.warn("JpaSystemException for event:{}. Sync-Consumer going to sleep mode for 1 minute.", eventTopic);
            stopListening();
            scheduleStartListeningTask(60_000);
            return "";
        } catch (IOException e) {
            logger.warn("data not matching {} model: {}", eventTopic, in);
            return "";
        } catch (Exception e) {
            logger.info("failed to save {} info to timescaleDB", eventTopic);
            logger.error("", e);
            stopListening();
            return "";
        }
        startedSaving(eventTopic);
        return in;
    }

    @Scheduled(fixedDelay = 230)
    public void discoverListener() {
        if (!isDiscovering.get()) {
            return;
        }
        String topic = KafkaTopic.DISCOVER.name();
        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusNanos(oldest_discovery_age_nanos).toString()).toEpochMilli();

        if (setUpConsumer(topic, startTimestamp, kafkaConsumerDiscover)) return;

        ConsumerRecords<String, String> records = kafkaConsumerDiscover.poll(Duration.ofMillis(250));
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                if (!discoverMessageQueue.offer(record.value())) {
                    System.out.println("InterruptedException while writing to DISCOVER message queue.");
                    stopDiscovering();
                } else {
                    startedDiscovering();
                }
            }
        });
    }

    @Scheduled(fixedRate = 230)
    public void scheduledConcurrentNfLoadListener() {
        if (getAssignedPartition() == -1) {
            return;
        }
        concurrentDataListener(NF_LOAD, kafkaConsumerEventThreadLocal.get());
    }

    @Scheduled(fixedRate = 230)
    public void scheduledConcurrentUeMobilityListener() {
        if (getAssignedPartition() == -1) {
            return;
        }
        concurrentDataListener(NwdafEvent.NwdafEventEnum.UE_MOBILITY, kafkaConsumerEventThreadLocal.get());
    }

    @Scheduled(fixedRate = 230)
    public void scheduledConcurrentUeCommunicationListener() {
        if (getAssignedPartition() == -1) {
            return;
        }
        concurrentDataListener(NwdafEvent.NwdafEventEnum.UE_COMM, kafkaConsumerEventThreadLocal.get());
    }

    @Scheduled(fixedDelay = 230)
    public void registerConsumerListener() {
        if (consumerInstances.isEmpty() && leader) {
            consumerInstances.put(NWDAF_INSTANCE_ID, RegisterConsumerMessage.builder().consumerId(NWDAF_INSTANCE_ID).leader(true).build());
        }
        String topic = KafkaTopic.REGISTER_CONSUMER.name();
        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusNanos(oldest_register_consumer_age_nanos).toString()).toEpochMilli();

        if (setUpConsumer(topic, startTimestamp, kafkaConsumerRegisterConsumer)) return;

        ConsumerRecords<String, String> records = kafkaConsumerRegisterConsumer.poll(Duration.ofMillis(250));
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                try {
                    RegisterConsumerMessage msg = RegisterConsumerMessage.fromString(record.value());
                    if (msg.getConsumerId() == NWDAF_INSTANCE_ID || msg.getTimestamp().isBefore(OffsetDateTime.now().minusSeconds(oldest_register_consumer_msg_age_seconds))) {
                        return;
                    }
                    if (msg.getLeader()) {
                        RegisterConsumerMessage selfRegisterMessage = RegisterConsumerMessage.builder()
                                .consumerId(NWDAF_INSTANCE_ID)
                                .build();
                        kafkaProducer.sendMessage(selfRegisterMessage.toString(), KafkaTopic.REGISTER_NOTIFIER.name());
                    }
                    consumerInstances.put(msg.getConsumerId(), msg);
                } catch (Exception e) {
                    logger.error("Error while parsing message from {} message queue: {}", topic, e.getMessage());
                }
            }
        });
    }

    @Scheduled(fixedDelay = 230)
    public void assignPartitionsListener() {
        String topic = KafkaTopic.ASSIGN_PARTITIONS.name();
        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusSeconds(oldest_assign_partitions_msg_age_seconds).toString()).toEpochMilli();

        if (setUpConsumer(topic, startTimestamp, kafkaConsumerAssignPartitions)) return;

        ConsumerRecords<String, String> records = kafkaConsumerAssignPartitions.poll(Duration.ofMillis(250));
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                try {
                    AssignPartitionsMessage msg = AssignPartitionsMessage.fromString(record.value());
                    assignedPartitions.put(msg.getConsumerId(), msg);
                } catch (Exception e) {
                    logger.error("Error while parsing message from {} message queue: {}", topic, e.getMessage());
                }
            }
        });
    }

    @KafkaListener(topics = {"NEF_SCENARIO"},
            containerFactory = "kafkaListenerContainerFactoryNefScenario2",
            autoStartup = "true")
    public String nefScenarioListener(ConsumerRecord<String, String> record) {
        if (notifier) {
            return "";
        }
        String in = record.value();
        try {
            KafkaNotifierLoadBalanceConsumer.setNefScenario(objectMapper.readValue(in, NefScenario.class));
            if (setupAoIs(false, false, metricsService, nefGroupId)) {
                refreshRegisteredAOIs(metricsService);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing message from NEF_SCENARIO message queue: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error while updating AoIs: {}", e.getMessage());
        }
        return in;
    }

    public void concurrentDataListener(NwdafEvent.NwdafEventEnum eventEnum, Consumer<String, String> kafkaConsumer) {
        if (!isListening.get()) {
            return;
        }
        startedReceiving(eventEnum);
        List<PartitionInfo> partitions = kafkaConsumer.partitionsFor(eventEnum.toString());
        int assignedPartition = getAssignedPartition();
        if (partitions.isEmpty() || assignedPartition > partitions.size() - 1 || assignedPartition < 0) {
            return;
        }
        String topic = eventEnum.toString();
        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusNanos(Constants.MIN_PERIOD_NANOS / 4).toString()).toEpochMilli();

        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (PartitionInfo partition : partitions) {
            TopicPartition topicPartition = new TopicPartition(topic, partition.partition());
            topicPartitions.add(topicPartition);
        }

        TopicPartition partition = topicPartitions.get(assignedPartition);
        kafkaConsumer.assign(List.of(partition));

        OffsetAndTimestamp offsetAndTimestamp = kafkaConsumer.offsetsForTimes(Collections.singletonMap(partition, startTimestamp)).get(partition);
        if (offsetAndTimestamp != null) {
            kafkaConsumer.seek(partition, offsetAndTimestamp.offset());
        }

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofNanos(Constants.MIN_PERIOD_NANOS / 4));

        List<String> values = new ArrayList<>();
        AtomicLong earliestRecordTimeStamp = new AtomicLong(Long.MAX_VALUE);
        AtomicLong latestRecordTimeStamp = new AtomicLong(Long.MIN_VALUE);
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp && record.timestamp() >= eventConsumerLastSaves.get(eventEnum)) {
                values.add(record.value());
                if (record.timestamp() < earliestRecordTimeStamp.get()) {
                    earliestRecordTimeStamp.set(record.timestamp());
                }
                if (record.timestamp() > latestRecordTimeStamp.get()) {
                    latestRecordTimeStamp.set(record.timestamp());
                }
            }
        });
        if (values.isEmpty()) {
            return;
        }

        try {
            consumeMetrics(eventEnum, values, latestRecordTimeStamp.get(), true);
//            System.out.println("Saved " + eventEnum + " x " + values.size() + " to database from "
//                    + Instant.ofEpochMilli(earliestRecordTimeStamp.get()) + " to " + Instant.ofEpochMilli(latestRecordTimeStamp.get()));
            startedSaving(eventEnum);
        } catch (IOException e) {
            logger.info("data not matching {} model: {}", eventEnum, values);
        } catch (JpaSystemException | TransactionSystemException | SQLException e) {
            logger.warn("JpaSystemException for event:{}. Consumer going to sleep mode for 1 minute.", eventEnum);
            stopListening();
            scheduleStartListeningTask(60_000);
        } catch (Exception e) {
            logger.info("failed to save {} infos to timescaleDB", eventEnum);
            logger.error("", e);
            stopListening();
        }
    }

    private boolean setUpConsumer(String topic, long startTimestamp, Consumer<String, String> consumer) {
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);

        long earliestTimestamp = Long.MAX_VALUE;
        List<TopicPartition> topicPartitions = new ArrayList<>();
        earliestTimestamp = assignTopicPartitions(consumer, partitions, topic, topicPartitions, earliestTimestamp);

        if (earliestTimestamp == Long.MAX_VALUE) {
            return true;
        }

        for (TopicPartition partition : topicPartitions) {
            OffsetAndTimestamp offsetAndTimestamp = consumer.offsetsForTimes(Collections.singletonMap(partition, startTimestamp)).get(partition);
            if (offsetAndTimestamp != null) {
                consumer.seek(partition, offsetAndTimestamp.offset());
            }
        }
        return false;
    }


    public static long assignTopicPartitions(Consumer<String, String> kafkaConsumer, List<PartitionInfo> partitions, String topic, List<TopicPartition> topicPartitions, long earliestTimestamp) {
        int i = 1;
        for (PartitionInfo partition : partitions) {
            TopicPartition topicPartition = new TopicPartition(topic, partition.partition());
            topicPartitions.add(topicPartition);
            kafkaConsumer.assign(Collections.singletonList(topicPartition));
            kafkaConsumer.seekToBeginning(Collections.singletonList(topicPartition));

            long beginningOffset = kafkaConsumer.position(topicPartition);
            OffsetAndTimestamp offsetAndTimestamp = kafkaConsumer.offsetsForTimes(Collections.singletonMap(topicPartition, beginningOffset)).get(topicPartition);
            if (offsetAndTimestamp != null) {
//                logger.info("Partition(" + i + "): " + partition.partition() + " Offset: " + beginningOffset + " Timestamp: " + Instant.ofEpochMilli(offsetAndTimestamp.timestamp()));
                long partitionTimestamp = offsetAndTimestamp.timestamp();
                if (partitionTimestamp < earliestTimestamp) {
                    earliestTimestamp = partitionTimestamp;
                }
            }
            i++;
        }
        return earliestTimestamp;
    }

    private void consumeMetric(NwdafEvent.NwdafEventEnum eventTopic, String in, Long recordTimeStamp, Boolean isLatest) throws Exception {
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

    private void consumeMetrics(NwdafEvent.NwdafEventEnum eventTopic, List<String> in, Long recordTimeStamp, Boolean isLatest) throws Exception {
        switch (eventTopic) {
            case NF_LOAD:
                List<NfLoadLevelInformation> nfLoadLevelInformations = in.stream().map(s -> {
                    try {
                        return objectMapper.reader().readValue(s, NfLoadLevelInformation.class);
                    } catch (IOException e) {
                        logger.warn("data not matching {} model: {}", eventTopic, in);
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
                        logger.warn("data not matching {} model: {}", eventTopic, in);
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
                        logger.warn("data not matching {} model: {}", eventTopic, in);
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
                    logger.info("Saved {}(x100) to database.", eventTopic);
                }
            }
        }
    }

    @Async
    public void performAsyncStartListeningTask() {
        startListening();
    }

    public void scheduleStartListeningTask(int delayMilliseconds) {
        if (scheduledTask == null || scheduledTask.isDone()) {
            try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
                scheduledTask = executorService.schedule(this::performAsyncStartListeningTask, delayMilliseconds, TimeUnit.MILLISECONDS);
                executorService.shutdown();
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

    public static void stopListening() {
        isListening.set(false);
        for (NwdafEvent.NwdafEventEnum e : supportedEvents) {
            stoppedSaving(e);
            stopReceiving(e);
        }
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
