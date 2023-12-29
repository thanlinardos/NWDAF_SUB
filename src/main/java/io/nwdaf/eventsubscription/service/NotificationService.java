package io.nwdaf.eventsubscription.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.notify.NotifyListener;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.repository.eventnotification.NotificationRepository;
import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.nwdaf.eventsubscription.notify.NotifyListener.logger;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final ObjectMapper objectMapper;
    private final NotifyPublisher notifyPublisher;
    private Future<?> scheduledTask;

    public NotificationService(NotificationRepository repository, ObjectMapper objectMapper, NotifyPublisher notifyPublisher) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.notifyPublisher = notifyPublisher;
    }

    public UUID create(NnwdafEventsSubscriptionNotification body) {
        NnwdafNotificationTable bodyTable = new NnwdafNotificationTable(
                objectMapper.<Map<String, Object>>convertValue(body, new TypeReference<>() {
                }),
                body.getTimeStamp(),
                body.getId());
        return safeSaveSingle(bodyTable);
    }

    public UUID create(UUID notificationReference, OffsetDateTime timeStamp, UUID id) {
        NnwdafNotificationTable bodyTable = new NnwdafNotificationTable(notificationReference, timeStamp, id);
        return safeSaveSingle(bodyTable);
    }

    private UUID safeSaveSingle(NnwdafNotificationTable bodyTable) {
        try {
            NnwdafNotificationTable result = repository.save(bodyTable);
            return result.getId();
        } catch (JpaSystemException e) {
            logger.warn("JpaSystemException for notification service. Going to sleep mode for 1 minute.");
            NotifyListener.stop();
            scheduleStartNotifyingTask(60_000);
        }
        return null;
    }

    public List<UUID> createAll(List<NnwdafEventsSubscriptionNotification> bodies) {
        List<NnwdafNotificationTable> bodyTables = new ArrayList<>();
        for (NnwdafEventsSubscriptionNotification body : bodies) {
            bodyTables.add(new NnwdafNotificationTable(
                    objectMapper.<Map<String, Object>>convertValue(body, new TypeReference<>() {
                    }),
                    body.getTimeStamp(),
                    body.getId()));
        }
        return safeSaveAll(bodyTables);
    }

    public List<UUID> createAll(List<UUID> notificationReferences, List<OffsetDateTime> timeStamps, List<UUID> ids) {
        List<NnwdafNotificationTable> bodyTables = new ArrayList<>();
        int counter = 0;
        for (UUID notificationReference : notificationReferences) {
            bodyTables.add(new NnwdafNotificationTable(
                    notificationReference, timeStamps.get(counter), ids.get(counter)));
            counter++;
        }
        return safeSaveAll(bodyTables);
    }

    public List<UUID> safeSaveAll(List<NnwdafNotificationTable> bodyTables) {
        try {
            List<NnwdafNotificationTable> results = repository.saveAll(bodyTables);
            return results.stream().map(NnwdafNotificationTable::getId).toList();
        } catch (JpaSystemException e) {
            logger.warn("JpaSystemException for notification service. Going to sleep mode for 1 minute.");
            NotifyListener.stop();
            scheduleStartNotifyingTask(60_000);
        } catch (InvalidDataAccessApiUsageException e) {
            logger.warn("InvalidDataAccessApiUsageException for notification service.", e);
        }
        return null;
    }

    public void asyncCreate(NnwdafEventsSubscriptionNotification body) {
        NnwdafNotificationTable bodyTable = new NnwdafNotificationTable(
                objectMapper.<Map<String, Object>>convertValue(body, new TypeReference<>() {
                }),
                body.getTimeStamp(),
                body.getId());
        ScheduledTasksService.notificationMessageQueue.offer(bodyTable);
    }

    public void asyncCreate(UUID notificationReference, OffsetDateTime timeStamp, UUID id) {
        NnwdafNotificationTable bodyTable = new NnwdafNotificationTable(notificationReference, timeStamp, id);
        ScheduledTasksService.notificationMessageQueue.offer(bodyTable);
    }

    private void safeVoidSaveSingle(NnwdafNotificationTable bodyTable) {
        try {
            repository.save(bodyTable);
        } catch (JpaSystemException e) {
            logger.warn("JpaSystemException for notification service. Going to sleep mode for 1 minute.");
            NotifyListener.stop();
            scheduleStartNotifyingTask(60_000);
        }
    }

    @Async
    public void performAsyncStartNotifyingTask() {
        notifyPublisher.publishNotification("Retrying notification with task", 0L);
    }

    public void scheduleStartNotifyingTask(int delayMilliseconds) {
        if (scheduledTask == null || scheduledTask.isDone()) {
            try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
                scheduledTask = executorService.schedule(this::performAsyncStartNotifyingTask, delayMilliseconds, TimeUnit.MILLISECONDS);
                executorService.shutdown();
            }
        }
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
                logger.warn("Client missed a notification for subscription with id: "
                        + sub.getId());
            }
        } catch (RestClientException e) {
            logger.error("Error connecting to client " + sub.getNotificationURI(), e);
        }
    }

    public void sendToClientWebClient(WebClient webClient, NnwdafEventsSubscription sub, HttpEntity<NnwdafEventsSubscriptionNotification> client_request) {
        Flux<NnwdafEventsSubscriptionNotification> notificationFlux = webClient
                .post()
                .uri(sub.getNotificationURI() + "/notify")
                .bodyValue(Objects.requireNonNull(client_request.getBody()))
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        response -> {
                            logger.warn("Client missed a notification with id: " + client_request.getBody().getId() + " for subscription with id: "
                                    + sub.getId());
                            return Mono.empty();
                        }
                )
                .bodyToFlux(NnwdafEventsSubscriptionNotification.class);
        notificationFlux.subscribe(response -> {
        }, error -> {
            if (error instanceof WebClientResponseException responseException) {
                HttpStatusCode statusCode = responseException.getStatusCode();
                logger.error("Error notifying client " + sub.getNotificationURI() + " with status code " + statusCode, error);
            } else {
                logger.error("Unexpected error notifying client " + sub.getNotificationURI(), error);
            }
        });
    }
}
