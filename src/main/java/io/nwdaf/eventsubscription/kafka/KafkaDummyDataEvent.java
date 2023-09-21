package io.nwdaf.eventsubscription.kafka;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class KafkaDummyDataEvent extends ApplicationEvent {
    private String message;

    public KafkaDummyDataEvent(Object source, String msg){
        super(source);
        this.message = msg;
    }
}
