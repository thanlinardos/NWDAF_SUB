package io.nwdaf.eventsubscription;

import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;

import static io.nwdaf.eventsubscription.utilities.Constants.*;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, JvmMetricsAutoConfiguration.class,
        LogbackMetricsAutoConfiguration.class, MetricsAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@EntityScan({"io.nwdaf.eventsubscription.repository"})
public class NwdafSubApplication {

    public static final UUID NWDAF_INSTANCE_ID = UUID.randomUUID();
    public static NetworkAreaInfo ServingAreaOfInterest = InitialServingAreaOfInterest;

    private static final Logger log = LoggerFactory.getLogger(NwdafSubApplication.class);
    @Getter
    private final ApplicationContext applicationContext;

    public NwdafSubApplication(ApplicationContext applicationContext,
                               NwdafSubProperties nwdafSubProperties) {

        this.applicationContext = applicationContext;
        if (nwdafSubProperties.integration().servingAreaOfInterestId() != null) {
            ServingAreaOfInterest.setId(nwdafSubProperties.integration().servingAreaOfInterestId());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(NwdafSubApplication.class, args);
    }

    public static Logger getLogger() {
        return NwdafSubApplication.log;
    }
}
