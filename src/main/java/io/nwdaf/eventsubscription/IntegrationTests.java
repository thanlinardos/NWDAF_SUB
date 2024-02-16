package io.nwdaf.eventsubscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.config.WebClientConfig;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.kafka.KafkaConsumer;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.UeCommunication;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.notify.NotificationUtil;
import io.nwdaf.eventsubscription.notify.NotifyListener;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.redis.RedisMetricsRepository;
import io.nwdaf.eventsubscription.repository.redis.RedisNotificationRepository;
import io.nwdaf.eventsubscription.repository.redis.RedisSubscriptionRepository;
import io.nwdaf.eventsubscription.repository.redis.entities.NfLoadLevelInformationHash;
import io.nwdaf.eventsubscription.repository.redis.entities.NnwdafEventsSubscriptionCached;
import io.nwdaf.eventsubscription.repository.redis.entities.NnwdafEventsSubscriptionNotificationCached;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.DummyDataGenerator;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.nwdaf.eventsubscription.notify.NotificationUtil.*;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.safeParseInteger;

@Component
public class IntegrationTests {
    private final SubscriptionsService subscriptionsService;
    private final MetricsService metricsService;
    private final NotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(IntegrationTests.class);
    private final RedisMetricsRepository redisRepository;
    private final RedisSubscriptionRepository redisSubscriptionRepository;
    private final RedisNotificationRepository redisNotificationRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final NotifyPublisher notifyPublisher;
    private final NwdafSubProperties nwdafSubProperties;
    private final WebClient webClient;

    public IntegrationTests(SubscriptionsService subscriptionsService,
                            MetricsService metricsService,
                            NotificationService notificationService,
                            RedisMetricsRepository redisRepository,
                            RedisSubscriptionRepository redisSubscriptionRepository,
                            RedisNotificationRepository redisNotificationRepository,
                            KafkaProducer kafkaProducer,
                            ObjectMapper objectMapper,
                            @Autowired(required = false) NotifyPublisher notifyPublisher, NwdafSubProperties nwdafSubProperties) {
        this.subscriptionsService = subscriptionsService;
        this.metricsService = metricsService;
        this.notificationService = notificationService;
        this.redisRepository = redisRepository;
        this.redisSubscriptionRepository = redisSubscriptionRepository;
        this.redisNotificationRepository = redisNotificationRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
        this.notifyPublisher = notifyPublisher;
        this.nwdafSubProperties = nwdafSubProperties;
        this.webClient = WebClient
                .builder()
                .clientConnector(Objects.requireNonNull(WebClientConfig.createWebClientFactory(false)))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.resetnotifdb", havingValue = "true")
    public CommandLineRunner resetNotifDb() {
        return args -> {
            if (!notificationService.truncate()) {
                log.error("Truncate notification table failed!");
            }
        };
    }

    @Bean
    public CommandLineRunner testMetricsDB() {
        return args -> {
            NfLoadLevelInformation nfLoadLevelInformation = DummyDataGenerator.generateDummyNfLoadLevelInfo(1).getFirst();
            metricsService.createNfload(nfLoadLevelInformation);
            assert (metricsService.findAllInLastIntervalByFilterAndOffset(null, 1L, 0L, 1, "").getFirst().getNfInstanceId() == nfLoadLevelInformation.getNfInstanceId());

            UeMobility ueMobility = DummyDataGenerator.generateDummyUeMobilities(1).getFirst();
            metricsService.createUeMob(ueMobility);
            assert (Objects.equals(metricsService.findAllUeMobilityInLastIntervalByFilterAndOffset(null, 1L, 0L, 1, "").getFirst().getSupi(), ueMobility.getSupi()));

            UeCommunication ueCommunication = DummyDataGenerator.generateDummyUeCommunications(1).getFirst();
            metricsService.createUeComm(ueCommunication);
            assert (Objects.equals(metricsService.findAllUeCommunicationInLastIntervalByFilterAndOffset(null, 1L, 0L, 1, "").getFirst().getSupi(), ueCommunication.getSupi()));
            log.info("Metrics DB tests passed.");
        };
    }

    @Bean
    @ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.startup", havingValue = "true")
    public CommandLineRunner integrationTest() {
        NwdafSubProperties.Integration integration = nwdafSubProperties.integration();
        return args -> {
            long subId = 0L;
            int n = 0;
            while (integration.max_no_cycles() == -1 || n < integration.max_no_cycles()) {
                System.out.println("test iteration: " + n);
                if (!subscriptionsService.truncate()) {
                    log.error("Truncate subscription table failed!");
                    return;
                }
                saveSubscriptions();

                for (NwdafEvent.NwdafEventEnum e : Constants.supportedEvents) {
                    WakeUpMessage wakeUpMessage = wakeUpDataProducer(kafkaProducer, e, null);
                    if(nwdafSubProperties.consume()) {
                        waitForDataProducer(wakeUpMessage);
                    } else {
                        sendWaitForDataProducerRequest(wakeUpMessage, webClient, nwdafSubProperties.consumerUrl());
                    }
                }

                Thread.sleep(2000);
                notifyPublisher.publishNotification(
                        "wakeupMethod: kafka, integrationTest with "
                                + integration.noSubs() + " subs for " + integration.cycleSeconds() + " seconds",
                        ++subId);

                Thread.sleep(integration.cycleSeconds() * 1000);
                NotifyListener.stop();
                KafkaConsumer.stopReceiving(NwdafEvent.NwdafEventEnum.NF_LOAD);

                long timeDiff = KafkaConsumer.endTime.minusNanos(KafkaConsumer.startTime.getNano()).toInstant().toEpochMilli();
                double timePerSave = (double) timeDiff / KafkaConsumer.eventConsumerCounters.get(NwdafEvent.NwdafEventEnum.NF_LOAD);
                System.out.println("StartTime: " + KafkaConsumer.startTime + ", EndTime: " + KafkaConsumer.endTime + ", TimePerSave: " + timePerSave + "ms");

                KafkaConsumer.startedReceiving(NwdafEvent.NwdafEventEnum.NF_LOAD);
                Thread.sleep(2000);
                n++;
            }
        };
    }

    // @Bean
    public CommandLineRunner redisTest() {
        return args -> {
            NfLoadLevelInformation nfLoadLevelInformation = new NfLoadLevelInformation().nfInstanceId(UUID.randomUUID())
                    .nfCpuUsage(100).time(Instant.now());
            NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(nfLoadLevelInformation);
            System.out.println(bodyTable.getData());
            NfLoadLevelInformationHash nfLoadLevelInformationCached = new NfLoadLevelInformationHash();
            nfLoadLevelInformationCached.setData(nfLoadLevelInformation);
            System.out.println("before:" + nfLoadLevelInformationCached);
            redisRepository.save(nfLoadLevelInformationCached);

            NfLoadLevelInformationHash result = redisRepository
                    .findById(nfLoadLevelInformationCached.getNfInstanceId()).orElse(null);
            System.out.println("after:" + result);

            NnwdafEventsSubscriptionCached nnwdafEventsSubscriptionCached = new NnwdafEventsSubscriptionCached();
            File test = new File("ue_mobility_test.json");
            NnwdafEventsSubscription sub = objectMapper.reader().readValue(test, NnwdafEventsSubscription.class);
            sub.setId(1L);
            nnwdafEventsSubscriptionCached.setSub(sub);
            System.out.println("before:" + nnwdafEventsSubscriptionCached);
            redisSubscriptionRepository.save(nnwdafEventsSubscriptionCached);

            NnwdafEventsSubscriptionCached subRes = redisSubscriptionRepository
                    .findById(nnwdafEventsSubscriptionCached.getId()).orElse(null);
            System.out.println("after:" + subRes);

            NnwdafEventsSubscriptionNotificationCached notificationCached = new NnwdafEventsSubscriptionNotificationCached();
            File notifTest = new File("notifTest.json");
            NnwdafEventsSubscriptionNotification notification = objectMapper.reader().readValue(notifTest,
                    NnwdafEventsSubscriptionNotification.class);
            notificationCached.setNotification(notification);
            System.out.println("before:" + notificationCached);
            redisNotificationRepository.save(notificationCached);

            NnwdafEventsSubscriptionNotificationCached notifRes = redisNotificationRepository
                    .findById(notificationCached.getId()).orElse(null);
            System.out.println("after:" + notifRes);
        };
    }

    // @Bean
    public CommandLineRunner redisQueryTest() {
        return args -> {
            NfLoadLevelInformationHash nfLoadLevelInformationCached = new NfLoadLevelInformationHash();
            nfLoadLevelInformationCached.setData(
                    new NfLoadLevelInformation().nfInstanceId(UUID.randomUUID()).nfCpuUsage(100).time(Instant.now()));
            System.out.println("before:" + nfLoadLevelInformationCached);
            redisRepository.save(nfLoadLevelInformationCached);

            List<NfLoadLevelInformationHash> res = redisRepository.findBy(null, null);
            System.out.println("after:" + res);
        };
    }

    private void saveSubscriptions() throws IOException {
        File nf_load_test = new File("nf_load_test.json");
        File ue_mobility_test = new File("ue_mobility_test.json");
        File ue_communication_test = new File("ue_communication_test.json");

        List<NnwdafEventsSubscription> subs = buildTestSubscriptions(
                nf_load_test,
                ue_mobility_test,
                ue_communication_test,
                nwdafSubProperties.client().prod_url(),
                nwdafSubProperties.integration().noClients(),
                nwdafSubProperties.integration().noSubs(),
                nwdafSubProperties.client().port(),
                objectMapper);
        if (subs == null) return;
        subscriptionsService.create(subs);
        System.out.println("Created " + nwdafSubProperties.integration().noSubs() + " subs for scenario with " + nwdafSubProperties.integration().noClients() + " clients.");
    }

    public static List<NnwdafEventsSubscription> buildTestSubscriptions(File nf_load_test,
                                                                        File ue_mobility_test,
                                                                        File ue_communication_test,
                                                                        String uri,
                                                                        Integer noClients,
                                                                        Integer noSubs,
                                                                        String defaultPort,
                                                                        ObjectMapper objectMapper) throws IOException {
        if (uri == null || noClients == null || noSubs == null || noClients == 0 || noSubs == 0) {
            System.out.println("Invalid parameters, cannot save subscriptions.");
            return null;
        }
        List<NnwdafEventsSubscription> subs = new ArrayList<>();
        for (int i = 0; i < noSubs / noClients; i++) {
            for (int j = 0; j < noClients; j++) {
                int current_port = safeParseInteger(defaultPort) + j;
                String parsedUri = uri.replace(defaultPort, Integer.toString(current_port));
                if (j > 0 && !uri.contains("localhost")) {
                    parsedUri = parsedUri.replace(":" + current_port,
                            ParserUtil.safeParseString(j + 1) + ":" + current_port);
                }
                subs.add(objectMapper.reader().readValue(nf_load_test, NnwdafEventsSubscription.class)
                        .notificationURI(parsedUri));
                subs.add(objectMapper.reader().readValue(ue_mobility_test, NnwdafEventsSubscription.class)
                        .notificationURI(parsedUri));
                subs.add(objectMapper.reader().readValue(ue_communication_test, NnwdafEventsSubscription.class)
                        .notificationURI(parsedUri));
            }
        }
        return subs;
    }
}
