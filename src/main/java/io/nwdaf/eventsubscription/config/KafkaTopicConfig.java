package io.nwdaf.eventsubscription.config;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    @Bean
    public NewTopic DISCOVER() {
         return new NewTopic("DISCOVER", 1, (short) 1);
    }
    @Bean
    public NewTopic WAKE_UP() {
         return new NewTopic("WAKE_UP", 1, (short) 1);
    }
    @Bean
    public NewTopic NF_LOAD() {
         return new NewTopic("NF_LOAD", 3, (short) 2);
    }
    @Bean
    public NewTopic UE_MOBILITY() {
         return new NewTopic("UE_MOBILITY", 3, (short) 2);
    }
    @Bean
    public NewTopic UE_COMM() {
        return new NewTopic("UE_COMM", 3, (short) 2);
    }
}