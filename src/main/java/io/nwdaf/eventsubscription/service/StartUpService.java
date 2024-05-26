package io.nwdaf.eventsubscription.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.customModel.*;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.utilities.Constants;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static io.nwdaf.eventsubscription.IntegrationTests.buildTestSubscriptions;
import static io.nwdaf.eventsubscription.NwdafSubApplication.*;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.assignedPartitions;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.consumerInstances;
import static io.nwdaf.eventsubscription.kafka.KafkaNotifierLoadBalanceConsumer.*;
import static io.nwdaf.eventsubscription.kafka.KafkaNotifierLoadBalanceConsumer.assignedSubscriptions;
import static io.nwdaf.eventsubscription.notify.NotificationUtil.*;
import static io.nwdaf.eventsubscription.notify.NotificationUtil.waitForDataProducer;
import static io.nwdaf.eventsubscription.utilities.Constants.*;
import static io.nwdaf.eventsubscription.utilities.Constants.MIN_PERIOD_SECONDS;

@Service
public class StartUpService {

    public static final ConcurrentHashMap<UUID, NetworkAreaInfo> registeredAOIs = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<NetworkAreaInfo, UUID> registeredUUIDsToAOIs = new ConcurrentHashMap<>();
    public static final AtomicBoolean isStartupFinished = new AtomicBoolean(false);

    private final NotifyPublisher notifyPublisher;
    private final MetricsService metricsService;
    private final SubscriptionsService subscriptionsService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;
    private final NwdafSubProperties nwdafSubProperties;
    private final AdminClient adminClient;
    private final List<String> topics = new ArrayList<>();

    public StartUpService(@Autowired(required = false) NotifyPublisher notifyPublisher,
                          MetricsService metricsService,
                          SubscriptionsService subscriptionsService,
                          NotificationService notificationService,
                          ObjectMapper objectMapper,
                          KafkaProducer kafkaProducer,
                          NwdafSubProperties nwdafSubProperties,
                          AdminClient adminClient) {
        this.notifyPublisher = notifyPublisher;
        this.metricsService = metricsService;
        this.subscriptionsService = subscriptionsService;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
        this.kafkaProducer = kafkaProducer;
        this.nwdafSubProperties = nwdafSubProperties;
        this.adminClient = adminClient;
        topics.addAll(Constants.supportedEvents.stream().map(NwdafEvent.NwdafEventEnum::toString).toList());
    }

    @Bean
    @ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.startup", havingValue = "false")
    public CommandLineRunner start() {
        return args -> {
            isStartupFinished.set(false);
            System.out.println("noClients: " + nwdafSubProperties.integration().noClients());
            if (nwdafSubProperties.consume()) {
                if (nwdafSubProperties.integration().resetmetricsdb() && !metricsService.truncate()) {
                    getLogger().error("Truncate metrics tables failed!");
                }
                for (NwdafEvent.NwdafEventEnum event : Constants.supportedEvents) {
                    System.out.println("oldest concurrent timestamp for " + event + ": " + metricsService.findOldestConcurrentMetricsTimeStamp(event));
                    System.out.println("oldest historic timestamp for " + event + ": " + metricsService.findOldestHistoricMetricsTimeStamp(event));
                    WakeUpMessage wakeUpMessage = wakeUpDataProducer(kafkaProducer, event, null);
                    if (!isDataBeingSaved(metricsService, MIN_PERIOD_SECONDS, event, 0L, MIN_PERIOD_SECONDS)) {
                        waitForDataProducer(wakeUpMessage);
                    }
                }
                selfRegisterConsumer();
            }
            if (nwdafSubProperties.notifier()) {
                if (nwdafSubProperties.integration().resetsubdb() && !subscriptionsService.truncate()) {
                    getLogger().error("Truncate subscription table failed!");
                }
                if (nwdafSubProperties.integration().resetnotifdb() && !notificationService.truncate()) {
                    getLogger().error("Truncate notification table failed!");
                }
                if (nwdafSubProperties.initSubscriptions()) {
                    saveSubscriptions();
                }
                selfRegisterNotifier();
            }

            List<NetworkAreaInfo> storedAreas = metricsService.findAllAoI();
            registerNewAOIs(storedAreas);
            ServingAreaOfInterest = storedAreas.stream()
                    .filter(aoi -> aoi.getId().equals(ServingAreaOfInterest.getId()))
                    .findFirst()
                    .orElse(ServingAreaOfInterest);

            if (nwdafSubProperties.notifier() && nwdafSubProperties.leader()) {
                List<NetworkAreaInfo> newAreas = new ArrayList<>(ExampleAOIsMap.values().stream()
                        .filter(aoi -> !storedAreas.contains(aoi) &&
                                !aoi.getId().equals(ServingAreaOfInterest.getId()))
                        .toList());
                if (storedAreas.stream().noneMatch(aoi -> aoi.getId().equals(ServingAreaOfInterest.getId()))) {
                    newAreas.add(ServingAreaOfInterest);
                }
                if (!newAreas.isEmpty()) {
                    registerNewAOIs(newAreas);
                    metricsService.createAllAoI(newAreas, null);
                }
            }

            isStartupFinished.set(true);
        };
    }

    public static void registerNewAOIs(List<NetworkAreaInfo> storedAreas) {
        storedAreas.forEach(aoi -> {
            registeredAOIs.put(aoi.getId(), aoi);
            registeredUUIDsToAOIs.put(aoi, aoi.getId());
        });
    }

    public static void refreshRegisteredAOIs(MetricsService metricsService) {
        registeredAOIs.clear();
        registeredUUIDsToAOIs.clear();
        List<NetworkAreaInfo> storedAreas = metricsService.findAllAoI();
        registerNewAOIs(storedAreas);
        ServingAreaOfInterest = storedAreas.stream()
                .filter(aoi -> aoi.getId().equals(ServingAreaOfInterest.getId()))
                .findFirst()
                .orElse(ServingAreaOfInterest);
    }

    @Scheduled(fixedDelay = 5000)
    public void wakeUpSupportedEvents() {
        if (!nwdafSubProperties.periodicWakeUp()) {
            return;
        }
        for (NwdafEvent.NwdafEventEnum e : Constants.supportedEvents) {
            wakeUpDataProducer(kafkaProducer, e, null);
        }
        List<DiscoverMessage> discoverMessages = new ArrayList<>();
        handleDiscoverMessageQueue(discoverMessages, false, false);
    }

    @Scheduled(fixedDelay = 100)
    public void assignSubscriptions() {
        if (!nwdafSubProperties.notifier()) {
            return;
        }
        selfRegisterNotifier();
        if (!nwdafSubProperties.leader() || !isRegisteringNotifiers.get() || notifiers.isEmpty()) {
            return;
        }
        for (UUID id : notifiers.keySet()) {
            if (id != NWDAF_INSTANCE_ID &&
                    notifiers.get(id) != null &&
                    notifiers.get(id).getTimestamp().isBefore(OffsetDateTime.now().minusSeconds(oldest_assign_subs_msg_age_seconds))) {
                notifiers.remove(id);
                assignedSubscriptions.remove(id);
            }
        }

        List<Long> availableSubscriptions = Collections.emptyList();
        try {
            availableSubscriptions = subscriptionsService.findAllIdsByActive(NotificationFlag.NotificationFlagEnum.DEACTIVATE);
        } catch (Exception e) {
            getLogger().error("Error getting available subscription Ids", e);
        }
        if (availableSubscriptions.isEmpty()) {
            return;
        }

        Set<UUID> notifiersIds = notifiers.keySet();
        int i = 0;
        int subsPerNotifier = availableSubscriptions.size() / notifiersIds.size();
        int lastAssignedIndex = 0;
        for (UUID id : notifiersIds) {
            List<Long> subscriptionIds;
            int noSubs = Math.min(subsPerNotifier, MAX_SUBS_PER_PROCCESS);
            subscriptionIds = availableSubscriptions.subList(i * noSubs, (i + 1) * noSubs);
            AssignSubscriptionsMessage msg = AssignSubscriptionsMessage
                    .builder()
                    .notifierId(id)
                    .subscriptionIds(subscriptionIds)
                    .build();
            if (id == NWDAF_INSTANCE_ID) {
                msg.setLeader(true);
            }
            assignedSubscriptions.put(id, msg);
            lastAssignedIndex = (i + 1) * noSubs - 1;
            i++;
        }
        if (lastAssignedIndex < availableSubscriptions.size() - 1) {
            List<Long> leftOverSubscriptionIds = availableSubscriptions.subList(lastAssignedIndex + 1, availableSubscriptions.size());
            List<Long> subscriptionIds = assignedSubscriptions.get(NWDAF_INSTANCE_ID).getSubscriptionIds();
            List<Long> newSubscriptionIds = Stream.concat(subscriptionIds.stream(), leftOverSubscriptionIds.stream()).toList();
//            if (newSubscriptionIds.size() > 200) {
//                getLogger().warn("Found surplus of " + leftOverSubscriptionIds.size() + " subscriptions to notify. Assigning them to leader...");
//            }
            assignedSubscriptions.get(NWDAF_INSTANCE_ID).setSubscriptionIds(newSubscriptionIds);
        }
        try {
            for (UUID id : notifiersIds) {
                if (assignedSubscriptions.get(id) != null) {
                    kafkaProducer.sendMessage(assignedSubscriptions.get(id).toString(), "ASSIGN_SUBS");
                }
            }
        } catch (IOException e) {
            getLogger().error("Error sending assigned subscriptions to kafka", e);
        }
    }

    @Scheduled(fixedDelay = 350, initialDelay = 200)
    public void assignPartitions() {
        if (!nwdafSubProperties.consume()) {
            return;
        }
        selfRegisterConsumer();
        if (!nwdafSubProperties.leader() || consumerInstances.isEmpty()) {
            return;
        }
        for (UUID id : consumerInstances.keySet()) {
            if (id != NWDAF_INSTANCE_ID &&
                    consumerInstances.get(id) != null &&
                    consumerInstances.get(id).getTimestamp().isBefore(OffsetDateTime.now().minusSeconds(1))) {
                consumerInstances.remove(id);
                assignedPartitions.remove(id);
            }
        }
        int i = 0;
        for (UUID id : consumerInstances.keySet()) {
            assignedPartitions.put(id, AssignPartitionsMessage
                    .builder()
                    .consumerId(id)
                    .leader(id.equals(NWDAF_INSTANCE_ID))
                    .partitionIndex(i)
                    .build());
            i++;
        }
        List<String> topicsToRecreate = new ArrayList<>();
        Map<String, Integer> noPartitionsMap = getNoPartitions(topics, adminClient);
        final int noPartitions = i;
        noPartitionsMap.forEach((k, v) -> {
            if (v >= noPartitions) {
                topics.remove(k);
                if (v > noPartitions) {
                    topicsToRecreate.add(k);
                }
            }
        });
        if (!topics.isEmpty()) {
            getLogger().info("increase parts to {} for topics {}", noPartitions, topics);
            increasePartitions(topics, noPartitions, adminClient);
        }
        if (!topicsToRecreate.isEmpty()) {
            getLogger().info("recreate topics {} with {} partitions and {} replicas", topicsToRecreate, noPartitions, nwdafSubProperties.numberOfReplicas());
            recreateTopics(topicsToRecreate, noPartitions, nwdafSubProperties.numberOfReplicas(), adminClient);
        }

        try {
            for (UUID id : consumerInstances.keySet()) {
                if (assignedPartitions.get(id) != null) {
                    kafkaProducer.sendMessage(assignedPartitions.get(id).toString(), KafkaTopic.ASSIGN_PARTITIONS.name());
                }
            }
        } catch (IOException e) {
            getLogger().error("Error sending assigned subscriptions to kafka", e);
        }
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 2000)
    public void startNotifying() {
        if (!nwdafSubProperties.notifier() || !nwdafSubProperties.autoNotify()) {
            return;
        }
        notifyPublisher.publishNotification("wakeupMethod: kafka, normal startup", 0L);
    }

    public static List<Long> getAssignedSubscriptions() {
        AssignSubscriptionsMessage msg = assignedSubscriptions.get(NWDAF_INSTANCE_ID);
        if (msg != null && msg.getTimestamp().isAfter(OffsetDateTime.now().minusSeconds(2))) {
            return msg.getSubscriptionIds();
        }
        if (msg != null) {
            assignedSubscriptions.remove(NWDAF_INSTANCE_ID);
        }
        return Collections.emptyList();
    }

    public static int getAssignedPartition() {
        AssignPartitionsMessage msg = assignedPartitions.get(NWDAF_INSTANCE_ID);
        if (msg != null && msg.getTimestamp().isAfter(OffsetDateTime.now().minusSeconds(1))) {
            return msg.getPartitionIndex() != null ? msg.getPartitionIndex() : -1;
        }
        if (msg != null) {
            assignedPartitions.remove(NWDAF_INSTANCE_ID);
        }
        return -1;
    }

    public void selfRegisterNotifier() {
        if (nwdafSubProperties.leader()) {
            return;
        }
        try {
            RegisterNotifierMessage selfRegisterMessage = RegisterNotifierMessage.builder()
                    .notifierId(NWDAF_INSTANCE_ID)
                    .build();
            kafkaProducer.sendMessage(selfRegisterMessage.toString(), "REGISTER_NOTIFIER");
        } catch (IOException e) {
            getLogger().error("Error sending self register message to kafka", e);
        }
    }

    public void selfRegisterConsumer() {
        if (nwdafSubProperties.leader()) {
            return;
        }
        try {
            RegisterConsumerMessage selfRegisterMessage = RegisterConsumerMessage.builder()
                    .consumerId(NWDAF_INSTANCE_ID)
                    .build();
            kafkaProducer.sendMessage(selfRegisterMessage.toString(), "REGISTER_CONSUMER");
        } catch (IOException e) {
            getLogger().error("Error sending self register message to kafka", e);
        }
    }

    public static void increasePartitions(List<String> topics, int noPartitions, AdminClient adminClient) {
        try {
            Map<String, NewPartitions> newPartitions = new HashMap<>();
            for (String topic : topics) {
                newPartitions.put(topic, NewPartitions.increaseTo(noPartitions));
            }
            adminClient.createPartitions(newPartitions).all().get();
        } catch (ExecutionException e) {
            getLogger().error("ExecutionException Error increasing partitions to " + noPartitions + "for topics " + topics + ": " + e.getMessage());
        } catch (Exception e) {
            getLogger().error("Error increasing partitions to " + noPartitions + "for topics " + topics, e);
        }
    }

    public static Map<String, Integer> getNoPartitions(List<String> topics, AdminClient adminClient) {
        Map<String, Integer> noPartitions = new HashMap<>();
        try {
            Map<String, TopicDescription> topicDescriptionMap = adminClient.describeTopics(topics).allTopicNames().get();
            for (String topic : topics) {
                noPartitions.put(topic, topicDescriptionMap.get(topic).partitions().size());
            }
        } catch (Exception e) {
            getLogger().error("Error getting number of partitions for topics " + topics, e);
        }
        return noPartitions;
    }

    public static void recreateTopics(List<String> topics, int noPartitions, short noReplicas, AdminClient adminClient) {
        try {
            adminClient.deleteTopics(topics).all().get();
            adminClient.createTopics(topics.stream().map(topic -> new NewTopic(topic, noPartitions, noReplicas)).toList()).all().get();
        } catch (ExecutionException e) {
            getLogger().error("ExecutionException Error recreating topics {}: {}", topics, e.getMessage());
        } catch (Exception e) {
            getLogger().error("Error recreating topics {} with {} partitions and {} replicas", topics, noPartitions, noReplicas, e);
        }
    }

    private void saveSubscriptions() throws IOException {
        File nf_load_test = new File("nf_load_test.json");
        File ue_mobility_test = new File("ue_mobility_test.json");
        File ue_communication_test = new File("ue_communication_test.json");

        List<NnwdafEventsSubscription> subs = buildTestSubscriptions(
                nf_load_test,
                ue_mobility_test,
                ue_communication_test,
                nwdafSubProperties.client().dev_url(),
                nwdafSubProperties.integration().noClients(),
                nwdafSubProperties.integration().noSubs(),
                nwdafSubProperties.client().port(),
                objectMapper);
        if (subs == null) return;
        subscriptionsService.create(subs);
        System.out.println("Created " + nwdafSubProperties.integration().noSubs() + " subs for scenario with " +
                nwdafSubProperties.integration().noClients() + " clients.");
    }
}
