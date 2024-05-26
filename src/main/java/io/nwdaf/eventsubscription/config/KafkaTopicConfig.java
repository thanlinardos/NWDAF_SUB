package io.nwdaf.eventsubscription.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import io.nwdaf.eventsubscription.customModel.KafkaTopic;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private final NwdafSubProperties nwdafSubProperties;

    public KafkaTopicConfig(NwdafSubProperties nwdafSubProperties) {
        this.nwdafSubProperties = nwdafSubProperties;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    @Bean
    public AdminClient adminClient() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return AdminClient.create(config);
    }
    @Bean
    public NewTopic DISCOVER() {
         return new NewTopic(KafkaTopic.DISCOVER.name(), 1, (short) 1);
    }
    @Bean
    public NewTopic WAKE_UP() {
         return new NewTopic(KafkaTopic.WAKE_UP.name(), 1, (short) 1);
    }
    @Bean
    public NewTopic NF_LOAD() {
         return new NewTopic(KafkaTopic.NF_LOAD.name(), nwdafSubProperties.numberOfPartitions(), nwdafSubProperties.numberOfReplicas());
    }
    @Bean
    public NewTopic UE_MOBILITY() {
         return new NewTopic(KafkaTopic.UE_MOBILITY.name(), nwdafSubProperties.numberOfPartitions(), nwdafSubProperties.numberOfReplicas());
    }
    @Bean
    public NewTopic UE_COMM() {
        return new NewTopic(KafkaTopic.UE_COMM.name(), nwdafSubProperties.numberOfPartitions(), nwdafSubProperties.numberOfReplicas());
    }

    @Bean
    public NewTopic REGISTER_NOTIFIER() {
        return new NewTopic(KafkaTopic.REGISTER_NOTIFIER.name(), 1, (short) 1);
    }

    @Bean
    public NewTopic ASSIGN_SUBS() {
        return new NewTopic(KafkaTopic.ASSIGN_SUBS.name(), 1, (short) 1);
    }

    @Bean
    public NewTopic REGISTER_CONSUMER() {
        return new NewTopic(KafkaTopic.REGISTER_CONSUMER.name(), 1, (short) 1);
    }

    @Bean
    public NewTopic ASSIGN_PARTITIONS() {
        return new NewTopic(KafkaTopic.ASSIGN_PARTITIONS.name(), 1, (short) 1);
    }

    @Bean
    public NewTopic NEF_SCENARIO() {
        return new NewTopic(KafkaTopic.NEF_SCENARIO.name(), 1, (short) 1);
    }
}