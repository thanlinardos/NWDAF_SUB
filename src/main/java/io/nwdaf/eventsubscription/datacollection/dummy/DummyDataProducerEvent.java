package io.nwdaf.eventsubscription.datacollection.dummy;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class DummyDataProducerEvent extends ApplicationEvent {
    private final String message;

    public DummyDataProducerEvent(Object source, String msg) {
        super(source);
        this.message = msg;
    }
}
