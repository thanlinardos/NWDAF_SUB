package io.nwdaf.eventsubscription.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.repository.eventnotification.NotificationRepository;
import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    @Async
    public void sendToClient(RestTemplate restTemplate, NnwdafEventsSubscription sub, HttpEntity<NnwdafEventsSubscriptionNotification> client_request) {
        try {
            ResponseEntity<NnwdafEventsSubscriptionNotification> client_response = restTemplate.postForEntity(sub.getNotificationURI() + "/notify",
                    client_request, NnwdafEventsSubscriptionNotification.class);
            if (!client_response.getStatusCode().is2xxSuccessful()) {
                io.nwdaf.eventsubscription.notify.NotifyListener.logger.warn("Client missed a notification for subscription with id: "
                        + sub.getId());
            }
        } catch (RestClientException e) {
            io.nwdaf.eventsubscription.notify.NotifyListener.logger.error("Error connecting to client " + sub.getNotificationURI(),e);
        }
    }
}
