package io.nwdaf.eventsubscription.service;

import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import io.nwdaf.eventsubscription.utilities.Constants;
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
    private static long notificationLastSaved = Long.MIN_VALUE;
    private static final long MAX_NOTIFICATION_SAVE_INTERVAL = Constants.MIN_PERIOD_SECONDS * 1000 * 2;

    public ScheduledTasksService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @Scheduled(fixedRate = 1)
    public void saveNotifications() {
        if (notificationMessageQueue.size() < 100 && getMillisSinceNotificationLastSave() < MAX_NOTIFICATION_SAVE_INTERVAL) {
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
        notificationLastSaved = System.currentTimeMillis();
        notificationService.safeSaveAll(messages);
    }

    private static long getMillisSinceNotificationLastSave() {
        return System.currentTimeMillis() - notificationLastSaved;
    }
}
