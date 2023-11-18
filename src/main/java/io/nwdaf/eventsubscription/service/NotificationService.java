package io.nwdaf.eventsubscription.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.repository.eventnotification.NotificationRepository;
import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final ObjectMapper objectMapper;

    public NotificationService(NotificationRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Async
    public void create(NnwdafEventsSubscriptionNotification body) {
        NnwdafNotificationTable bodyTable = new NnwdafNotificationTable(
                objectMapper.convertValue(body,new TypeReference<>() {}),
                body.getTimeStamp());
        repository.save(bodyTable);
    }

    public boolean truncate() {
        try {
            repository.truncate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
