package io.nwdaf.eventsubscription.datacollection.prometheus;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class DataCollectionEvent extends ApplicationEvent{
    private String message;

    public DataCollectionEvent(Object source, String msg){
        super(source);
        this.message = msg;
    }
}