package io.nwdaf.eventsubscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
import io.nwdaf.eventsubscription.utilities.Constants;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static io.nwdaf.eventsubscription.IntegrationTests.buildTestSubscriptions;
import static io.nwdaf.eventsubscription.notify.NotificationUtil.*;
import static io.nwdaf.eventsubscription.utilities.Constants.ExampleAOIsMap;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, JvmMetricsAutoConfiguration.class,
        LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@EntityScan({"io.nwdaf.eventsubscription.repository"})
public class NwdafSubApplication {

    public static final UUID NWDAF_INSTANCE_ID = UUID.randomUUID();
    public static NetworkAreaInfo ServingAreaOfInterest = Constants.ServingAreaOfInterest;

    private static final Logger log = LoggerFactory.getLogger(NwdafSubApplication.class);
    private final NotifyPublisher notifyPublisher;
    @Getter
    private final ApplicationContext applicationContext;
    final MetricsService metricsService;
    final SubscriptionsService subscriptionsService;
    final ObjectMapper objectMapper;
    final KafkaProducer kafkaProducer;
    private final NwdafSubProperties nwdafSubProperties;

    public NwdafSubApplication(NotifyPublisher notifyPublisher,
                               ApplicationContext applicationContext,
                               MetricsService metricsService,
                               SubscriptionsService subscriptionsService,
                               ObjectMapper objectMapper,
                               KafkaProducer kafkaProducer,
                               NwdafSubProperties nwdafSubProperties) {

        this.notifyPublisher = notifyPublisher;
        this.applicationContext = applicationContext;
        this.metricsService = metricsService;
        this.subscriptionsService = subscriptionsService;
        this.objectMapper = objectMapper;
        this.kafkaProducer = kafkaProducer;
        this.nwdafSubProperties = nwdafSubProperties;

        if (nwdafSubProperties.integration().servingAreaOfInterestId() != null) {
            ServingAreaOfInterest = ExampleAOIsMap.get(nwdafSubProperties.integration().servingAreaOfInterestId());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(NwdafSubApplication.class, args);
    }

    @Scheduled(fixedDelay = 5000)
    public void wakeUpSupportedEvents() {
        for (NwdafEventEnum e : Constants.supportedEvents) {
            wakeUpDataProducer(kafkaProducer, e, null);
        }
    }

    @Bean
    @ConditionalOnProperty(name = "nnwdaf-eventsubscription.integration.startup", havingValue = "false")
    public CommandLineRunner start() {
        return args -> {
            System.out.println("noClients: " + nwdafSubProperties.integration().noClients());
            saveSubscriptions();

            for (NwdafEventEnum event : Constants.supportedEvents) {
                System.out.println("oldest concurrent timestamp for " + event + ": " + metricsService.findOldestConcurrentMetricsTimeStamp(event));
                System.out.println("oldest historic timestamp for " + event + ": " + metricsService.findOldestHistoricMetricsTimeStamp(event));
                WakeUpMessage wakeUpMessage = wakeUpDataProducer(kafkaProducer, event, null);
                waitForDataProducer(event, wakeUpMessage);
            }
            notifyPublisher.publishNotification("wakeupMethod: kafka, normal startup", 0L);
        };
    }

    public static Logger getLogger() {
        return NwdafSubApplication.log;
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
}
