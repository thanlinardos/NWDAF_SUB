package io.nwdaf.eventsubscription.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.UUID;

@ConfigurationProperties(prefix = "nnwdaf-eventsubscription")
@RefreshScope
public record NwdafSubProperties(OpenAPIProperties openapi,
                                 ClientProperties client,
                                 Boolean init,
                                 Integration integration,
                                 Log log,
                                 Boolean secureWithTrustStore,
                                 Boolean notifier, Boolean consume,
                                 Boolean periodicWakeUp,
                                 Boolean initSubscriptions,
                                 String consumerUrl,
                                 Integer numberOfPartitions,
                                 Short numberOfReplicas,
                                 Boolean leader,
                                 Boolean autoNotify) {
    public record OpenAPIProperties(String dev_url) {

    }

    public record ClientProperties(String dev_url, Integer port) {

    }

    public record Integration(UUID servingAreaOfInterestId,
                              Integer noClients,
                              Integer noSubs,
                              Boolean startup,
                              Integer cycleSeconds,
                              Integer max_no_cycles,
                              Boolean resetsubdb,
                              Boolean resetmetricsdb,
                              Boolean resetnotifdb) {

    }

    public record Log(Boolean kb, Boolean simple, Boolean sections, Boolean consumer) {

    }

}

