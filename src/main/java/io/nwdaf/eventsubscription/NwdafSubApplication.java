package io.nwdaf.eventsubscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionPublisher;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.*;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
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
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication(exclude = { JacksonAutoConfiguration.class, JvmMetricsAutoConfiguration.class,
		LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class })
@EnableAsync
@EnableScheduling
@EntityScan({ "io.nwdaf.eventsubscription.repository" })
public class NwdafSubApplication {

	private static final Logger log = LoggerFactory.getLogger(NwdafSubApplication.class);
	private final NotifyPublisher notifyPublisher;
	private final DataCollectionPublisher dataCollectionPublisher;
	@Getter
	private final ApplicationContext applicationContext;
	private final DummyDataProducerPublisher dummyDataProducerPublisher;
	final KafkaTemplate<String, String> kafkaTemplate;
	final MetricsService metricsService;
	final SubscriptionsService subscriptionsService;
	final ObjectMapper objectMapper;
	final Environment env;
	final KafkaProducer producer;
	final RedisMetricsRepository redisRepository;
	final RedisSubscriptionRepository redisSubscriptionRepository;
	final RedisNotificationRepository redisNotificationRepository;
	final NotificationService notificationService;
	@Value("${nnwdaf-eventsubscription.integration.nosubs}")
	private Integer noSubs;
	@Value("${nnwdaf-eventsubscription.integration.noclients}")
	private Integer noClients;
	@Value("${nnwdaf-eventsubscription.integration.cycleSeconds}")
	private Integer cycleSeconds;

	public NwdafSubApplication(NotifyPublisher notifyPublisher, DataCollectionPublisher dataCollectionPublisher, ApplicationContext applicationContext, RedisSubscriptionRepository redisSubscriptionRepository, NotificationService notificationService, DummyDataProducerPublisher dummyDataProducerPublisher, KafkaTemplate<String, String> kafkaTemplate, MetricsService metricsService, SubscriptionsService subscriptionsService, RedisNotificationRepository redisNotificationRepository, ObjectMapper objectMapper, Environment env, KafkaProducer producer, RedisMetricsRepository redisRepository) {
		this.notifyPublisher = notifyPublisher;
		this.dataCollectionPublisher = dataCollectionPublisher;
		this.applicationContext = applicationContext;
		this.redisSubscriptionRepository = redisSubscriptionRepository;
		this.notificationService = notificationService;
		this.dummyDataProducerPublisher = dummyDataProducerPublisher;
		this.kafkaTemplate = kafkaTemplate;
		this.metricsService = metricsService;
		this.subscriptionsService = subscriptionsService;
		this.redisNotificationRepository = redisNotificationRepository;
		this.objectMapper = objectMapper;
		this.env = env;
		this.producer = producer;
		this.redisRepository = redisRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(NwdafSubApplication.class, args);

	}

	@Bean
	@ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.resetsubdb", havingValue = "true")
	public CommandLineRunner resetSubDb() {
		return args -> {
			if (!subscriptionsService.truncate()) {
				log.error("Truncate subscription table failed!");
			}
		};
	}

	@Bean
	@ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.resetmetricsdb", havingValue = "true")
	public CommandLineRunner resetMetricsDb() {
		return args -> {
			if (!metricsService.truncate()) {
				log.error("Truncate nf_load & ue_mobility tables failed!");
			}
		};
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

	// @Bean
	public CommandLineRunner testMetricsDB() {
		return args -> {
			metricsService.create(new NfLoadLevelInformation().nfInstanceId(UUID.randomUUID()).nfCpuUsage(100)
					.time(Instant.now()).addSupi("supi"));
			System.out.println(metricsService.findAllInLastIntervalByFilterAndOffset(null, 1, 1, "").get(0));
			metricsService.create(new UeMobility().time(Instant.now()).addAreaOfInterestIdsItem(UUID.randomUUID())
					.addLocInfosItem(new LocationInfo().confidence(12)));
			System.out.println(metricsService.findAllUeMobilityInLastIntervalByFilterAndOffset(null, 1, 1, "").get(0));
		};
	}

	@Bean
	@ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.startup", havingValue = "true")
	public CommandLineRunner run() throws JsonProcessingException {

		return args -> {
			Long subId = 0L;
			File test = new File("test.json");
			String uri = env.getProperty("nnwdaf-eventsubscription.client.prod-url");
			Integer default_port = ParserUtil
					.safeParseInteger(env.getProperty("nnwdaf-eventsubscription.client.port"));
			if (uri == null) {
				return;
			}
			for (int n = 0; n < 5; n++) {
				System.out.println("test iteration: " + n);
				if (!subscriptionsService.truncate()) {
					log.error("Truncate subscription table failed!");
					return;
				}
				for (int i = 0; i < noSubs / noClients; i++) {
					for (int j = 0; j < noClients; j++) {
						int current_port = default_port + j;
						String parsedUri = uri.replace(default_port.toString(), Integer.toString(current_port));
						if (j > 0 && !uri.contains("localhost")) {
							parsedUri = parsedUri.replace(":" + current_port,
									ParserUtil.safeParseString(j + 1) + ":" + current_port);
						}
						subscriptionsService
								.create(objectMapper.reader().readValue(test, NnwdafEventsSubscription.class)
										.notificationURI(parsedUri));
					}
				}
				System.out.println("Created " + noSubs + " subs for scenario with " + noClients + " clients.");
				NotificationUtil.wakeUpDataProducer("kafka",
						NwdafEventEnum.UE_MOBILITY,
						null,
						dataCollectionPublisher,
						dummyDataProducerPublisher,
						producer,
						objectMapper);
				notifyPublisher.publishNotification(subId);
				Thread.sleep(cycleSeconds*1000);
				NotifyListener.stop();
				Thread.sleep(2000);
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
			System.out.println("before:" + nfLoadLevelInformationCached.toString());
			redisRepository.save(nfLoadLevelInformationCached);

			NfLoadLevelInformationHash result = redisRepository
					.findById(nfLoadLevelInformationCached.getNfInstanceId()).orElse(null);
			System.out.println("after:" + result);

			NnwdafEventsSubscriptionCached nnwdafEventsSubscriptionCached = new NnwdafEventsSubscriptionCached();
			File test = new File("test.json");
			NnwdafEventsSubscription sub = objectMapper.reader().readValue(test, NnwdafEventsSubscription.class);
			sub.setId(1L);
			nnwdafEventsSubscriptionCached.setSub(sub);
			System.out.println("before:" + nnwdafEventsSubscriptionCached.toString());
			redisSubscriptionRepository.save(nnwdafEventsSubscriptionCached);

			NnwdafEventsSubscriptionCached subRes = redisSubscriptionRepository
					.findById(nnwdafEventsSubscriptionCached.getId()).orElse(null);
            System.out.println("after:" + subRes);

			NnwdafEventsSubscriptionNotificationCached notificationCached = new NnwdafEventsSubscriptionNotificationCached();
			File notifTest = new File("notifTest.json");
			NnwdafEventsSubscriptionNotification notification = objectMapper.reader().readValue(notifTest,
					NnwdafEventsSubscriptionNotification.class);
			notificationCached.setNotification(notification);
			System.out.println("before:" + notificationCached.toString());
			redisNotificationRepository.save(notificationCached);

			NnwdafEventsSubscriptionNotificationCached notifRes = redisNotificationRepository
					.findById(notificationCached.getId()).orElse(null);
			System.out.println("after:" + notifRes);
		};
	}

	// @Bean
	public CommandLineRunner redisQuerryTest() {
		return args -> {
			NfLoadLevelInformationHash nfLoadLevelInformationCached = new NfLoadLevelInformationHash();
			nfLoadLevelInformationCached.setData(
					new NfLoadLevelInformation().nfInstanceId(UUID.randomUUID()).nfCpuUsage(100).time(Instant.now()));
			System.out.println("before:" + nfLoadLevelInformationCached.toString());
			redisRepository.save(nfLoadLevelInformationCached);

			List<NfLoadLevelInformationHash> res = redisRepository.findBy(null, null);
			System.out.println("after:" + res);
		};
	}

	public static Logger getLogger() {
		return NwdafSubApplication.log;
	}

}
