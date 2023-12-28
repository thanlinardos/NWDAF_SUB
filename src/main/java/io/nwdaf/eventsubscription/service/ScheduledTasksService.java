package io.nwdaf.eventsubscription.service;

import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ScheduledTasksService {
    public static final ConcurrentLinkedQueue<NnwdafNotificationTable> notificationMessageQueue = new ConcurrentLinkedQueue<>();
    private final NotificationService notificationService;

    public ScheduledTasksService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @Scheduled(fixedRate = 1)
    public void saveNotifications() {
        if (notificationMessageQueue.size() < 100) {
            return;
        }
        List<NnwdafNotificationTable> messages = new ArrayList<>();
        while (!notificationMessageQueue.isEmpty()) {
            NnwdafNotificationTable message = notificationMessageQueue.poll();
            if(message != null) {
                messages.add(message);
            }
        }
        if(messages.isEmpty()) {
            return;
        }
        notificationService.safeSaveAll(messages);
    }
}
