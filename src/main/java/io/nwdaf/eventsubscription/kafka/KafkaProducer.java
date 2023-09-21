package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
@Getter @Setter
public class KafkaProducer {

    private String topicName;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public String sendMessage(String msg, Optional<String> topicNameOptional) throws IOException{
		  return kafkaTemplate.send(topicNameOptional.orElse(this.topicName), msg).toString();
    }
}
