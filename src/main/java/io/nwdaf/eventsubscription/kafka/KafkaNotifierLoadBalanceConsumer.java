package io.nwdaf.eventsubscription.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.customModel.AssignSubscriptionsMessage;
import io.nwdaf.eventsubscription.customModel.KafkaTopic;
import io.nwdaf.eventsubscription.customModel.NefScenario;
import io.nwdaf.eventsubscription.customModel.RegisterNotifierMessage;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.PlmnId;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import lombok.Getter;
import lombok.Setter;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.nwdaf.eventsubscription.NwdafSubApplication.NWDAF_INSTANCE_ID;
import static io.nwdaf.eventsubscription.NwdafSubApplication.ServingAreaOfInterest;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.assignTopicPartitions;
import static io.nwdaf.eventsubscription.service.StartUpService.*;
import static io.nwdaf.eventsubscription.utilities.Constants.*;
import static io.nwdaf.eventsubscription.utilities.ConvertUtil.*;
import static io.nwdaf.eventsubscription.utilities.OtherUtil.updateContainedAOIIds;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.notifier", havingValue = "true")
@Component
public class KafkaNotifierLoadBalanceConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaNotifierLoadBalanceConsumer.class);

    public static ConcurrentHashMap<UUID, RegisterNotifierMessage> notifiers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, AssignSubscriptionsMessage> assignedSubscriptions = new ConcurrentHashMap<>();
    public static final AtomicBoolean isRegisteringNotifiers = new AtomicBoolean(true);
    public static final AtomicBoolean isAssigningSubscriptions = new AtomicBoolean(true);

    private final Consumer<String, String> kafkaConsumerRegisterNotifier;
    private final Consumer<String, String> kafkaConsumerAssignSubs;
    private final KafkaProducer kafkaProducer;
    private final SubscriptionsService subscriptionsService;
    private final ObjectMapper objectMapper;
    private final MetricsService metricsService;

    private static boolean isNefScenarioSet = false;

    @Getter
    @Setter
    private static NefScenario nefScenario;

    @Getter
    private static PlmnId nefScenarioPlmnId;

    @Getter
    private static NetworkAreaInfo nefScenarioServingAoI;

    @Getter
    public static List<NetworkAreaInfo> nefScenarioCellAoIs = new ArrayList<>();

    @Value("${nnwdaf-eventsubscription.leader}")
    private boolean leader;

    @Value("${nnwdaf-eventsubscription.nef_group_id}")
    private String nefGroupId;

    public KafkaNotifierLoadBalanceConsumer(@Qualifier("consumerRegisterNotifier")
                                            Consumer<String, String> kafkaConsumerRegisterNotifier,
                                            @Qualifier("consumerAssignSubs")
                                            Consumer<String, String> kafkaConsumerAssignSubs,
                                            KafkaProducer kafkaProducer,
                                            SubscriptionsService subscriptionsService,
                                            ObjectMapper objectMapper, MetricsService metricsService) {
        this.kafkaConsumerRegisterNotifier = kafkaConsumerRegisterNotifier;
        this.kafkaConsumerAssignSubs = kafkaConsumerAssignSubs;
        this.kafkaProducer = kafkaProducer;
        this.subscriptionsService = subscriptionsService;
        this.objectMapper = objectMapper;
        this.metricsService = metricsService;
    }

    @Scheduled(fixedDelay = 230)
    public void registerNotifierListener() {
        if (notifiers.isEmpty() && leader) {
            RegisterNotifierMessage registerNotifierMessage = RegisterNotifierMessage
                    .builder()
                    .notifierId(NWDAF_INSTANCE_ID)
                    .leader(true)
                    .build();
            notifiers.put(NWDAF_INSTANCE_ID, registerNotifierMessage);
        }
        if (!isRegisteringNotifiers.get()) {
            return;
        }
        String topic = KafkaTopic.REGISTER_NOTIFIER.name();
        List<PartitionInfo> partitions = kafkaConsumerRegisterNotifier.partitionsFor(topic);

        long earliestTimestamp = Long.MAX_VALUE;
        List<TopicPartition> topicPartitions = new ArrayList<>();
        earliestTimestamp = assignTopicPartitions(kafkaConsumerRegisterNotifier,
                partitions,
                topic,
                topicPartitions,
                earliestTimestamp);

        if (earliestTimestamp == Long.MAX_VALUE) {
            return;
        }

        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusNanos(100_000_000).toString()).toEpochMilli();

        for (TopicPartition partition : topicPartitions) {
            OffsetAndTimestamp offsetAndTimestamp = kafkaConsumerRegisterNotifier
                    .offsetsForTimes(Collections.singletonMap(partition, startTimestamp))
                    .get(partition);
            if (offsetAndTimestamp != null) {
                kafkaConsumerRegisterNotifier.seek(partition, offsetAndTimestamp.offset());
            }
        }

        ConsumerRecords<String, String> records = kafkaConsumerRegisterNotifier.poll(Duration.ofMillis(250));
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                try {
                    RegisterNotifierMessage msg = RegisterNotifierMessage.fromString(record.value());
                    if (msg.getNotifierId() == NWDAF_INSTANCE_ID ||
                            msg.getTimestamp().isBefore(OffsetDateTime.now().minusSeconds(1))) {
                        return;
                    }
                    if (msg.getLeader()) {
                        RegisterNotifierMessage selfRegisterMessage = RegisterNotifierMessage.builder()
                                .notifierId(NWDAF_INSTANCE_ID)
                                .build();
                        kafkaProducer.sendMessage(selfRegisterMessage.toString(), "REGISTER_NOTIFIER");
                    }
                    notifiers.put(msg.getNotifierId(), msg);
                    isRegisteringNotifiers.set(true);
                } catch (Exception e) {
                    logger.error("Error while parsing message from {} message queue: {}", topic, e.getMessage());
                }
            }
        });
    }

    @Scheduled(fixedDelay = 230)
    public void assignSubscriptionsListener() {
        if (!isAssigningSubscriptions.get()) {
            return;
        }
        String topic = KafkaTopic.ASSIGN_SUBS.name();
        List<PartitionInfo> partitions = kafkaConsumerAssignSubs.partitionsFor(topic);

        long earliestTimestamp = Long.MAX_VALUE;
        List<TopicPartition> topicPartitions = new ArrayList<>();
        earliestTimestamp = assignTopicPartitions(kafkaConsumerAssignSubs, partitions, topic, topicPartitions, earliestTimestamp);

        if (earliestTimestamp == Long.MAX_VALUE) {
            return;
        }

        long endTimestamp = Instant.parse(OffsetDateTime.now().toString()).toEpochMilli();
        long startTimestamp = Instant.parse(OffsetDateTime.now().minusSeconds(1).toString()).toEpochMilli();

        for (TopicPartition partition : topicPartitions) {
            OffsetAndTimestamp offsetAndTimestamp = kafkaConsumerAssignSubs
                    .offsetsForTimes(Collections.singletonMap(partition, startTimestamp)).get(partition);
            if (offsetAndTimestamp != null) {
                kafkaConsumerAssignSubs.seek(partition, offsetAndTimestamp.offset());
            }
        }

        ConsumerRecords<String, String> records = kafkaConsumerAssignSubs.poll(Duration.ofMillis(250));
        records.forEach(record -> {
            if (record.timestamp() <= endTimestamp && record.timestamp() >= startTimestamp) {
                try {
                    AssignSubscriptionsMessage msg = AssignSubscriptionsMessage.fromString(record.value());
                    assignedSubscriptions.put(msg.getNotifierId(), msg);
                    isAssigningSubscriptions.set(true);
                } catch (Exception e) {
                    logger.error("Error while parsing message from {} message queue: {}", topic, e.getMessage());
                }
            }
        });
    }

    @KafkaListener(topics = {"NEF_SCENARIO"},
            containerFactory = "kafkaListenerContainerFactoryNefScenario",
            autoStartup = "true")
    public String nefScenarioListener(ConsumerRecord<String, String> record) throws InterruptedException {
        String in = record.value();
        if (!isStartupFinished.get()) {
            Thread.sleep(20);
            if (!isStartupFinished.get()) {
                return in;
            }
        }
        try {
            nefScenario = objectMapper.readValue(in, NefScenario.class);
            if (leader && setupAoIs(true, true, metricsService, nefGroupId)) {
                refreshRegisteredAOIs(metricsService);
                recalculateContainedAoIsInSubs(subscriptionsService);
            } else if (setupAoIs(false, false, metricsService, nefGroupId)) {
                refreshRegisteredAOIs(metricsService);
            }
            isNefScenarioSet = true;
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing message from NEF_SCENARIO message queue: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error while updating AoIs: {}", e.getMessage());
        }
        return in;
    }

    /**
     * Setup AoIs based on the NEF scenario and depending on input can store them to the metrics db.
     *
     * @param saveToRegisteredAoIs If true, the stored nef AoIs will be deleted and replaced with the new AoIs.
     * @param saveToServingAoI     If true, the serving AoI will be updated with the new concatenated nef AoIs.
     * @param metricsService       The metrics service.
     * @param nefGroupId           The NEF group id.
     * @return True if new AoIs were found and setup, false otherwise.
     * @throws Exception If an error occurs while setting up the AoIs and calling the metrics service operations.
     */
    public static boolean setupAoIs(Boolean saveToRegisteredAoIs,
                                    Boolean saveToServingAoI,
                                    MetricsService metricsService,
                                    String nefGroupId) throws Exception {
        if (nefScenario == null) {
            logger.error("NefScenario is null");
            return false;
        }
        nefScenarioPlmnId = ConvertUtil.getNefScenarioPlmnId(nefScenario);

        List<NetworkAreaInfo> newNefAoIs = new ArrayList<>();
        nefScenario.getCells().forEach(cell -> newNefAoIs
                .add(mapNefCellToAOI(cell, nefScenario.getGNBs(), nefScenarioPlmnId)));

        List<NetworkAreaInfo> currentNefAoIs = new ArrayList<>();
        if (!nefScenarioCellAoIs.isEmpty() && isNefScenarioSet) {
            currentNefAoIs.addAll(nefScenarioCellAoIs);
        } else {
            currentNefAoIs.addAll(metricsService.findAllNefAoI(nefGroupId));
        }

        boolean hasDeprecatedAoIs = currentNefAoIs.stream().anyMatch(aoi -> newNefAoIs.stream()
                .noneMatch(newAoI -> newAoI.equals(aoi)));

        boolean hasNewAoIs = newNefAoIs.stream().anyMatch(aoi -> currentNefAoIs.stream()
                .noneMatch(oldAoI -> oldAoI.equals(aoi)));
        if (!hasNewAoIs && !hasDeprecatedAoIs) {
            return false;
        }

        if (saveToRegisteredAoIs) {
            currentNefAoIs.forEach(aoi -> toOptional(aoi).ifPresent(areaInfo -> {
                metricsService.deleteAoI(areaInfo.getId());
            }));
        }

        nefScenarioCellAoIs = newNefAoIs;
        if (currentNefAoIs.isEmpty()) {
            nefScenarioCellAoIs.forEach(aoi -> aoi.setId(UUID.randomUUID()));
        } else {
            nefScenarioCellAoIs.forEach(aoi -> currentNefAoIs.stream()
                    .filter(oldAoI -> oldAoI.equals(aoi))
                    .findFirst()
                    .ifPresentOrElse(
                            oldAoI -> aoi.setId(oldAoI.getId()),
                            () -> aoi.setId(UUID.randomUUID())
                    ));
        }

        UUID nefServAoIId = toOptional(nefScenarioServingAoI).map(NetworkAreaInfo::getId).orElse(UUID.randomUUID());
        nefScenarioServingAoI = concatenateAoIs(nefScenarioCellAoIs, nefServAoIId);

        if (saveToServingAoI) {
            ServingAreaOfInterest = concatenateAoIs(List.of(InitialServingAreaOfInterest, nefScenarioServingAoI),
                    InitialServingAreaOfInterest.getId());
            metricsService.updateAoI(ServingAreaOfInterest, null);
        }
        if (saveToRegisteredAoIs) {
            metricsService.createAllAoI(nefScenarioCellAoIs, nefGroupId);
            metricsService.createAoI(nefScenarioServingAoI, nefGroupId);
        }
        return true;
    }

    public static void recalculateContainedAoIsInSubs(SubscriptionsService subscriptionsService) throws JsonProcessingException {
        List<NnwdafEventsSubscription> subs = subscriptionsService.findAll();
        subs.forEach(KafkaNotifierLoadBalanceConsumer::updateSubscriptionContainedAoIs);
        subs.forEach(sub -> subscriptionsService.update(sub.getId(), sub));
    }

    public static void updateSubscriptionContainedAoIs(NnwdafEventsSubscription sub) {
        toOptional(sub.getEventSubscriptions()).ifPresent(eventSubs -> {
            eventSubs.stream()
                    .filter(Objects::nonNull)
                    .map(EventSubscription::getNetworkArea)
                    .filter(Objects::nonNull)
                    .forEach(area -> updateContainedAOIIds(area, registeredAOIs));
            eventSubs.stream()
                    .filter(Objects::nonNull)
                    .map(EventSubscription::getVisitedAreas)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .forEach(area -> updateContainedAOIIds(area, registeredAOIs));
        });
    }

    public static void fillKnownAoIs(NnwdafEventsSubscription sub) {
        toOptional(sub.getEventSubscriptions()).ifPresent(eventSubs -> {
            eventSubs.stream()
                    .filter(Objects::nonNull)
                    .map(EventSubscription::getNetworkArea)
                    .filter(Objects::nonNull)
                    .filter(area -> area.getId() != null)
                    .forEach(area -> fillAoIContents(area, registeredAOIs.get(area.getId())));
            eventSubs.stream()
                    .filter(Objects::nonNull)
                    .map(EventSubscription::getVisitedAreas)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .filter(area -> area.getId() != null)
                    .forEach(area -> fillAoIContents(area, registeredAOIs.get(area.getId())));
        });
    }
}
