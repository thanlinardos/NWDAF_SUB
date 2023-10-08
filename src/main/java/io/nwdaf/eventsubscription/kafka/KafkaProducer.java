package io.nwdaf.eventsubscription.kafka;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
@Getter @Setter
public class KafkaProducer {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public String sendMessage(String msg, String topicName) throws IOException{
		  return kafkaTemplate.send(topicName, msg).toString();
    }
}
