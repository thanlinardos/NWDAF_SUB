package io.nwdaf.eventsubscription.notify;

import java.io.IOException;
import java.lang.Exception;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import io.nwdaf.eventsubscription.config.WebClientConfig;
import io.nwdaf.eventsubscription.model.*;
import io.nwdaf.eventsubscription.requestbuilders.RestTemplateFactory;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.*;

import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.client.WebClient;

import static io.nwdaf.eventsubscription.utilities.CheckUtil.safeCheckEqualsEvent;
import static io.nwdaf.eventsubscription.utilities.CheckUtil.safeCheckEventNotificationWithinMilli;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.parsePresentNfLoadLevelInformations;

@Component
public class NotifyListener {

    public static final Logger logger = NwdafSubApplication.getLogger();
    public static final Integer max_no_notifEventListeners = 1;
    @Getter
    @Setter
    private static AtomicInteger no_notifEventListeners = new AtomicInteger();
    private final SubscriptionsService subscriptionService;
    private final NotificationService notificationService;
    private final MetricsService metricsService;
    RestTemplate restTemplate;
    WebClient webClient;
    private final MetricsCacheService metricsCacheService;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.###");
    @Getter
    private static int no_served_subs;
    @Getter
    private static Map<Long, Integer> repPeriods;
    @Getter
    private static Map<Long, Integer> subIndexes;
    @Getter
    private static Map<Long, OffsetDateTime> lastNotifTimes;
    @Getter
    private static Map<Long, OffsetDateTime> oldNotifTimes;
    @Getter
    private static List<Pair<EventSubscription, NnwdafEventsSubscriptionNotification>> mapEventToNotification;
    @Getter
    private static double tsdb_req_delay;
    @Getter
    private static double client_delay;
    @Getter
    private static double notif_save_delay;
    @Getter
    private static long no_sent_notifs;
    @Getter
    private static double no_sent_kilobytes;
    @Getter
    private static double total_sent_kilobytes;
    @Getter
    private static long counter;
    @Getter
    private static long total_sent_notifs;
    @Getter
    private static long no_found_notifs;
    @Getter
    private static long avg_io_delay;
    @Getter
    private static long avg_program_delay;
    @Getter
    private static double total;
    @Getter
    private static double sub_delay;

    @Value("${trust.store}")
    private Resource trustStore;
    @Value("${trust.store.password}")
    private String trustStorePassword;
    @Value("${nnwdaf-eventsubscription.secureWithTrustStore}")
    private Boolean secureWithTrustStore;

    @Value("${nnwdaf-eventsubscription.log.kb}")
    private Boolean logKilobyteCount;
    @Value("${nnwdaf-eventsubscription.log.simple}")
    private Boolean logSimple;
    @Value("${nnwdaf-eventsubscription.log.sections}")
    private Boolean logSections;
    @Value("${nnwdaf-eventsubscription.integration.cycleSeconds}")
    private Integer cycleSeconds;
    @Value("${nnwdaf-eventsubscription.integration.nosubs}")
    private Integer noSubs;

    public NotifyListener(SubscriptionsService subscriptionService, NotificationService notificationService, MetricsService metricsService, MetricsCacheService metricsCacheService) {
        this.subscriptionService = subscriptionService;
        this.notificationService = notificationService;
        this.metricsService = metricsService;
        this.metricsCacheService = metricsCacheService;
    }

    static Long encodeKey(Long i, Integer j) {
        return (i << 7) | j;
    }

    static Long decodeKeyI(Long combinedKey) {
        return combinedKey >> 7;
    }

    static Integer decodeKeyJ(Long combinedKey) {
        return Long.valueOf(combinedKey & 0x7F).intValue();
    }


    @Async
    @EventListener
    public void sendNotifications(NotificationEvent notificationEvent) {
        System.out.println("trying to enter with current number of notify listeners: " + no_notifEventListeners.get());
        if (no_notifEventListeners.get() < max_no_notifEventListeners) {
            no_notifEventListeners.getAndIncrement();
        } else {
            return;
        }

        List<NnwdafEventsSubscription> subs = new ArrayList<>();
        try {
            subs = subscriptionService.findAll();
        } catch (Exception e) {
            logger.error("Error with find subs in subscriptionService", e);
            stop();
        }
        int listenerId = no_notifEventListeners.get() - 1;
        logger.info("Started NotifyListener(" + listenerId + ") with message: " + notificationEvent.getMessage() +
                " and caller id: " + notificationEvent.getCallerId());

        // map with key each served event (pair of sub id,event index)
        // and value being the last time a notification was sent for this event to the
        // corresponding client
        lastNotifTimes = new HashMap<>();
        // repetition period for each event
        repPeriods = new HashMap<>();
        oldNotifTimes = new HashMap<>();
        subIndexes = new HashMap<>();
        mapEventToNotification = new ArrayList<>();

        NotificationBuilder notificationBuilder = new NotificationBuilder();
        mapRepopulate(subs);

        System.out.println("no_subs=" + subs.size());
        System.out.println("no_Ssubs=" + no_served_subs);
        long start;

        initGlobalLogs();

        setupWebClient();
//        setupRestTemplate();

        while (no_served_subs > 0 && no_notifEventListeners.get() > 0) {
            long st;
            initLogs();
            long loop_section;
            long st2 = System.nanoTime();
            start = System.nanoTime();

            // loop through the event subscriptions and the dates of last producing a
            // notification for each of them
            double section_a = 0, section_b = 0, section_c = 0;
            long kb_time = 0;

            for (Map.Entry<Long, OffsetDateTime> entry : lastNotifTimes.entrySet()) {

                // match the id to the subscription
                long lst = System.nanoTime();

                boolean found = false;
                long key = entry.getKey();
                long id = decodeKeyI(key);
                int eventIndex = decodeKeyJ(key);
                NnwdafEventsSubscription sub = subs.get(subIndexes.get(id));    // TODO: BOTTLENECK #1 -> offload to redis query

                // match the event index to the event subscription
                EventSubscription event = sub.getEventSubscriptions().get(eventIndex);
                Integer repPeriod = repPeriods.get(key);

                if (repPeriod == null) {
                    sub.addFailEventReportsItem(new FailureEventInfo()
                            .event(event.getEvent())
                            .failureCode(new NwdafFailureCode()
                                    .failureCode(NwdafFailureCode.NwdafFailureCodeEnum.OTHER)));
                    continue;
                }

                // build the notification
                NnwdafEventsSubscriptionNotification notification = notificationBuilder.build(id).time(Instant.now());
                section_a += (double) (System.nanoTime() - lst) / 1_000L;

                int i = -1;
                for (int n = 0; n < mapEventToNotification.size(); n++) {
                    if (event.equals(mapEventToNotification.get(n).getFirst())) {
                        i = n;
                        break;
                    }
                }

                NnwdafEventsSubscriptionNotification foundNotification = null;
                if (i != -1) {
                    foundNotification = mapEventToNotification.get(i).getSecond();      // TODO: Bottleneck #2 -> found notification map too slow
                }

                try {
                    if (foundNotification != null && safeCheckEventNotificationWithinMilli(foundNotification,
                            repPeriod * 1_250L, Instant.now().toEpochMilli())) {

                        notification = foundNotification;
                        notification.notificationReference(foundNotification.getId())
                                .id(UUID.randomUUID())
                                .subscriptionId(sub.getId().toString());
                        no_found_notifs++;
                        found = true;
                    } else {
                        st = System.nanoTime();
                        notification = NotificationUtil.getNotification(sub, eventIndex, notification, metricsService,
                                metricsCacheService);
                        tsdb_req_delay += (double) (System.nanoTime() - st) / 1_000L;
                    }
                    if (notification != null && sub.getFailEventReports() != null && !sub.getFailEventReports().isEmpty()) {

                        sub.setFailEventReports(sub.getFailEventReports()
                                .stream()
                                .filter(t -> !safeCheckEqualsEvent(t.getEvent(), event.getEvent()))
                                .toList());
                    }
                } catch (Exception e) {
                    logger.error("[Service Error]Failed to collect data for event: " + event.getEvent().getEvent(), e);
                    stop();
                    continue;
                }

                if (notification == null) {
                    continue;
                }

                if (i != -1) {
                    mapEventToNotification.set(i, Pair.of(mapEventToNotification.get(i).getFirst(), notification));
                } else {
                    mapEventToNotification.add(Pair.of(event, notification));
                }

                saveNotification(found, notification);

                // check if period has passed -> notify client (or if threshold has been reached)
                SectionBLogs sectionBLogs = handleSendNotification(repPeriod, notification, event, sub, key, section_b, section_c, kb_time, entry);
                section_b = sectionBLogs.section_b();
                section_c = sectionBLogs.section_c();
                kb_time = sectionBLogs.kb_time();
            }

            if (logKilobyteCount) {
                System.out.println("kb_time= " + kb_time / 1_000L + "ms");
            }

            loop_section = (System.nanoTime() - st2);
            printPerfA(loop_section, section_a, section_b, section_c, no_served_subs);
            long st_sub = System.nanoTime();

            try {
                subs = subscriptionService.findAll();
            } catch (Exception e) {
                logger.error("Error with find subs in subscriptionService", e);
                stop();
                continue;
            }
            sub_delay = (System.nanoTime() - st_sub) / 1_000_000.0;
            if (logSections) {
                System.out.print(" || sub query time: " + decimalFormat.format(sub_delay) + "ms");
            }

            st = System.nanoTime();

            mapCleanUp();   //TODO: Bottleneck #4 -> map clean up & repopulate

            mapRepopulate(subs);

            if (logSections) {
                System.out.println(" || maps=" + decimalFormat.format((System.nanoTime() - st) / 1_000_000L) + "ms");
            }

            total = (double) (System.nanoTime() - start) / 1_000_000L;
            long io_delay = (long) (tsdb_req_delay + client_delay + notif_save_delay) / 1_000L;
            avg_io_delay += (long) (io_delay + sub_delay);
            avg_program_delay += (long) (total - io_delay - sub_delay);
            total_sent_kilobytes += no_sent_kilobytes;
            counter++;
            printPerfB(tsdb_req_delay, client_delay, notif_save_delay, no_sent_notifs, no_sent_kilobytes, total);

            // wait till one quarter of a second (min period) passes (10^9/4 nanoseconds)
            long wait_time = (long) Constants.MIN_PERIOD_SECONDS * 250L;
            if ((long) total < wait_time) {

                try {
                    Thread.sleep(wait_time - (long) total);
                } catch (InterruptedException e) {
                    logger.error("Failed to wait for thread...", e);
                    stop();
                }
            }
        }

        System.out.println("no of found notifications = " + no_found_notifs);
        System.out.println("==NotifyListener(" + listenerId + ") finished===");

        if (no_notifEventListeners.get() > 0) {
            no_notifEventListeners.getAndDecrement();
        } else {
            System.out.println("==(NotifyListener manually stopped)");
        }
        System.out.println("Number of listeners after stop: " + no_notifEventListeners.get());
        if (counter == 0) {
            return;
        }
        logger.info(
                "avg io delay= " + avg_io_delay / counter + "ms || avg program delay= " + avg_program_delay / counter
                        + "ms || avg total delay= " + (avg_io_delay + avg_program_delay) / counter + "ms");
        if (logKilobyteCount) {
            logger.info("total_sent_megabytes= " + total_sent_kilobytes / 1024 + "MB");
            double avg_throughput = total_sent_kilobytes / (128 * 100_000);
            logger.info("avg throughput= " + avg_throughput + "mbps");
        }

        logger.info(total_sent_notifs + "/" + cycleSeconds * noSubs + " notifications sent");
    }

    private static void initGlobalLogs() {
        total_sent_kilobytes = 0;
        counter = 0;
        total_sent_notifs = 0;
        no_found_notifs = 0;
        avg_io_delay = 0;
        avg_program_delay = 0;
    }

    private static void initLogs() {
        no_sent_notifs = 0;
        no_sent_kilobytes = 0;
        tsdb_req_delay = 0;
        client_delay = 0;
        notif_save_delay = 0;
    }

    private static void mapRepopulate(List<NnwdafEventsSubscription> subs) {
        no_served_subs = 0;
        for (int i = 0; i < subs.size(); i++) {

            Long id = subs.get(i).getId();
            for (int j = 0; j < subs.get(i).getEventSubscriptions().size(); j++) {
                long key = encodeKey(id, j);
                Integer period = NotificationUtil.needsServing(subs.get(i), j);

                if (period != null) {

                    no_served_subs++;
                    repPeriods.put(key, period);

                    if (oldNotifTimes.get(key) == null) {
                        lastNotifTimes.put(key, OffsetDateTime.now());
                    } else {
                        lastNotifTimes.put(key, oldNotifTimes.get(key));
                    }
                    subIndexes.put(subs.get(i).getId(), i);
                }
            }
        }
    }

    private static void mapCleanUp() {
        oldNotifTimes.clear();
        oldNotifTimes.putAll(lastNotifTimes);
        mapEventToNotification.clear();
        lastNotifTimes.clear();
        repPeriods.clear();
        subIndexes.clear();
    }

    private record SectionBLogs(double section_b, double section_c, long kb_time) {
    }

    private void saveNotification(boolean found, NnwdafEventsSubscriptionNotification notification) {
        long st = System.nanoTime();
        if (found) {
            notificationService.asyncCreate(notification.getNotificationReference(), notification.getTimeStamp(), notification.getId());
        } else {
            notificationService.asyncCreate(notification);
        }
        notif_save_delay += (double) (System.nanoTime() - st) / 1_000L;
    }

    private SectionBLogs handleSendNotification(Integer repPeriod, NnwdafEventsSubscriptionNotification
            notification, EventSubscription event, NnwdafEventsSubscription sub, long key, double section_b, double section_c,
                                                long kb_time, Map.Entry<Long, OffsetDateTime> entry) {
        long st_if = System.nanoTime();

        OffsetDateTime now = OffsetDateTime.now();
        if ((repPeriod > 0 && now.isAfter(entry.getValue().plusSeconds(repPeriod))) || (repPeriod == 0 && thresholdReached(event, notification))) {

            long st = System.nanoTime();

            HttpEntity<NnwdafEventsSubscriptionNotification> client_request = new HttpEntity<>(notification);
//          notificationService.sendToClient(restTemplate, sub, client_request);
            notificationService.sendToClientWebClient(webClient, sub, client_request);

            client_delay += (double) (System.nanoTime() - st) / 1_000L;

            st = System.nanoTime();

            lastNotifTimes.put(key, now);       //TODO: Bottleneck #3
            no_sent_notifs++;

            if (logKilobyteCount) {
                long kb_start = System.nanoTime();
                no_sent_kilobytes += (double) notification.toString().getBytes().length / 1_024L;
                kb_time += (System.nanoTime() - kb_start) / 1_000L;
            }
            total_sent_notifs++;

            section_c += (double) (System.nanoTime() - st) / 1_000L;
        }

        section_b += (double) (System.nanoTime() - st_if) / 1_000L;
        return new SectionBLogs(section_b, section_c, kb_time);
    }

    private boolean thresholdReached(EventSubscription event, NnwdafEventsSubscriptionNotification notification) {
        NwdafEventEnum eType = event.getEvent().getEvent();

        switch (eType) {
            case NF_LOAD:
                List<ThresholdLevel> thresholdLevels = event.getNfLoadLvlThds();
                if (thresholdLevels == null || thresholdLevels.isEmpty()) {
                    return false;
                }

                int size = Math.min(notification.getEventNotifications().getFirst().getNfLoadLevelInfos().size(),
                        thresholdLevels.size());
                List<NfLoadLevelInformation> presentNfLevelInformation = parsePresentNfLoadLevelInformations(notification.getEventNotifications().getFirst().getNfLoadLevelInfos());
                int index;

                for (int i = 0; i < size; i++) {
                    NfLoadLevelInformation nfLoadLevelInformation = presentNfLevelInformation.get(i);
                    if (thresholdLevels.get(i) == null) {
                        continue;
                    }

                    boolean[] isThresholdBooleans = mapIsThresholdBooleans(thresholdLevels, i, nfLoadLevelInformation);
                    int booleanIndex = -1;
                    for (int j = 0; j < isThresholdBooleans.length; j++) {
                        if (isThresholdBooleans[j]) {
                            booleanIndex = j;
                            break;
                        }
                    }

                    if (booleanIndex != -1) {
                        index = notification.getEventNotifications().getFirst().getNfLoadLevelInfos()
                                .indexOf(nfLoadLevelInformation);
                        int[] propertyValues = mapPropertyValues(notification, index);

                        notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                                .setThresholdProperty(Constants.nfLoadThresholdProps[booleanIndex]);
                        notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                                .setThresholdValue(propertyValues[booleanIndex]);
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private static boolean[] mapIsThresholdBooleans(List<ThresholdLevel> thresholdLevels,
                                                    int i, NfLoadLevelInformation nfLoadLevelInformation) {
        return new boolean[]{(thresholdLevels.get(i).getNfCpuUsage() != null
                && nfLoadLevelInformation.getNfCpuUsage() >= thresholdLevels.get(i).getNfCpuUsage()),
                (thresholdLevels.get(i).getNfMemoryUsage() != null && nfLoadLevelInformation
                        .getNfMemoryUsage() >= thresholdLevels.get(i).getNfMemoryUsage()),
                (thresholdLevels.get(i).getNfStorageUsage() != null && nfLoadLevelInformation
                        .getNfStorageUsage() >= thresholdLevels.get(i).getNfStorageUsage()),
                (thresholdLevels.get(i).getNfLoadLevel() != null && nfLoadLevelInformation
                        .getNfLoadLevelAverage() >= thresholdLevels.get(i).getNfLoadLevel())};
    }

    private static int[] mapPropertyValues(NnwdafEventsSubscriptionNotification notification, int index) {
        return new int[]{
                notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                        .getNfCpuUsage(),
                notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                        .getNfMemoryUsage(),
                notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                        .getNfStorageUsage(),
                notification.getEventNotifications().getFirst().getNfLoadLevelInfos().get(index)
                        .getNfLoadLevelAverage()};
    }

    public static void stop() {
        if (no_notifEventListeners.get() > 0) {
            no_notifEventListeners.getAndDecrement();
        }
        System.out.println("after stop func: " + no_notifEventListeners.get());
    }

    private void printPerfA(double loop_section, double section_a, double section_b, double section_c,
                            int no_served_subs) {
        if (logSections) {
            System.out.print("loop_section=" + decimalFormat.format(loop_section / 1_000_000L) + "ms");
            System.out.print(" || section_a=" + decimalFormat.format(section_a / 1_000L) + "ms");
            System.out.print(" || section_b=" + decimalFormat.format(section_b / 1_000L) + "ms");
            System.out.print(" || section_c=" + decimalFormat.format(section_c / 1_000L) + "ms");
        }
        if (logSimple) {
            System.out.print(" || served_subs=" + no_served_subs);
        }
    }

    private void printPerfB(double tsdb_req_delay, double client_delay, double notif_save_delay,
                            long no_sent_notifs,
                            double no_sent_kilobytes, double total) {
        if (logSections) {
            System.out.print(" || client_delay:" + decimalFormat.format(client_delay / 1_000L) + "ms");
            System.out.print(" || notif_save_delay:" + decimalFormat.format(notif_save_delay / 1_000L) + "ms");
        }
        if (logSimple) {
            System.out.print(" || tsdb_req_delay: " + decimalFormat.format(tsdb_req_delay / 1_000L) + "ms");
            System.out.print(" || no_sent_notifs:" + no_sent_notifs);
            if (logKilobyteCount) {
                System.out.print(" || no_sent_kilobytes:" + no_sent_kilobytes + "KB");
            }
            System.out.print(" || total delay: " + decimalFormat.format(total) + "ms\n");
        }
    }

    private void setupWebClient() {
        WebClientConfig.setTrustStore(trustStore);
        WebClientConfig.setTrustStorePassword(trustStorePassword);
        webClient = WebClient
                .builder()
                .exchangeStrategies(WebClientConfig.createExchangeStrategies(5 * 1024 * 1024))  //5MB
                .clientConnector(Objects.requireNonNull(WebClientConfig.createWebClientFactory(secureWithTrustStore)))
                .build();
    }

    private void setupRestTemplate() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        restTemplate = new RestTemplate(Objects.requireNonNull(RestTemplateFactory.createRestTemplateFactory(trustStore, trustStorePassword)));
    }
}