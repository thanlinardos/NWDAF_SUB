package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
@Getter
@Setter
public class KafkaProducer {

    final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendMessage(String msg, String topicName) throws IOException {
        return kafkaTemplate.send(topicName, msg).toString();
    }
}
