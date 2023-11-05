package io.nwdaf.eventsubscription.notify;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.config.RestTemplateFactoryConfig;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.NotificationService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

import org.springframework.core.io.Resource;

@Component
public class NotifyListener {

	public static Integer max_subs_per_process = 200;
	public static Integer max_no_notifEventListeners = 1;
	private static Integer no_notifEventListeners = 0;
	private static final Object notifLock = new Object();
	@Autowired
	SubscriptionsService subscriptionService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MetricsService metricsService;

	@Autowired
	ObjectMapper objectMapper;

	RestTemplate restTemplate;

	@Autowired
	MetricsCacheService metricsCacheService;

	@Value("${trust.store}")
	private Resource trustStore;
	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Async
	@EventListener
	void sendNotifications(Long subId) {
		synchronized (notifLock) {
			if (no_notifEventListeners < 1) {
				no_notifEventListeners++;
			} else {
				return;
			}
		}
		Logger logger = NwdafSubApplication.getLogger();
		List<NnwdafEventsSubscription> subs = new ArrayList<>();
		try {
			subs = subscriptionService.findAll();
		} catch (Exception e) {
			logger.error("Error with find subs in subscriptionService", e);
			stop();
		}
		// map with key each served event (pair of sub id,event index)
		// and value being the last time a notification was sent for this event to the
		// corresponding client
		Map<Pair<Long, Integer>, OffsetDateTime> lastNotifTimes = new HashMap<>();
		// repetition period for each event
		Map<Pair<Long, Integer>, Integer> repPeriods = new HashMap<>();
		Map<Pair<Long, Integer>, OffsetDateTime> oldNotifTimes = new HashMap<>();
		Map<Long, Integer> subIndexes = new HashMap<>();
		List<Pair<EventSubscription, NnwdafEventsSubscriptionNotification>> mapEventToNotification = new ArrayList<>();
		// builder for converting data to notification objects
		NotificationBuilder notifBuilder = new NotificationBuilder();
		int no_served_subs = 0;
		for (int i = 0; i < subs.size(); i++) {
			for (int j = 0; j < subs.get(i).getEventSubscriptions().size(); j++) {
				Integer period = NotificationUtil.needsServing(subs.get(i), j);
				if (period != null) {
					no_served_subs++;
					repPeriods.put(Pair.of(subs.get(i).getId(), j), period);
					lastNotifTimes.put(Pair.of(subs.get(i).getId(), j), OffsetDateTime.now());
					subIndexes.put(subs.get(i).getId(), i);
				}
			}
		}
		System.out.println("no_subs=" + subs.size());
		System.out.println("no_Ssubs=" + no_served_subs);
		long start;
		double tsdb_req_delay, client_delay, notif_save_delay;
		int no_sent_notifs, no_sent_kilobytes;
		double avg_client_delay_per_notif = 0;
		int counter = 0, total_sent_notifs = 0;
		RestTemplateFactoryConfig.setTrustStore(trustStore);
		RestTemplateFactoryConfig.setTrustStorePassword(trustStorePassword);
		restTemplate = new RestTemplate(RestTemplateFactoryConfig.createRestTemplateFactory());
		long no_found_notifs = 0;
		long avg_io_delay = 0, avg_program_delay = 0;
		while (no_served_subs > 0 && no_notifEventListeners > 0) {
			long st;
			no_sent_notifs = 0;
			no_sent_kilobytes = 0;
			start = System.nanoTime();
			tsdb_req_delay = 0;
			client_delay = 0;
			notif_save_delay = 0;
			long loop_section = 0;
			long st2 = System.nanoTime();
			// loop through the event subscriptions and the dates of last producing a
			// notification for each of them
			double section_a = 0, section_b = 0, section_c = 0;
			for (Map.Entry<Pair<Long, Integer>, OffsetDateTime> entry : lastNotifTimes.entrySet()) {
				// match the id to the subscription
				long lst = System.nanoTime();
				Long id = entry.getKey().getFirst();
				int eventIndex = entry.getKey().getSecond();
				NnwdafEventsSubscription sub = subs.get(subIndexes.get(id));
				// match the event index to the event subscription
				EventSubscription event = sub.getEventSubscriptions().get(eventIndex);
				Integer repPeriod = NotificationUtil.needsServing(sub, eventIndex);
				// build the notification
				NnwdafEventsSubscriptionNotification notification = notifBuilder.build(id);
				section_a += (System.nanoTime() - lst) / 1000l;
				st = System.nanoTime();
				int i = -1;
				for (int n = 0; n < mapEventToNotification.size(); n++) {
					if (event.equals(mapEventToNotification.get(n).getFirst())) {
						i = n;
						break;
					}
				}
				NnwdafEventsSubscriptionNotification foundNotification = null;
				if (i != -1) {
					foundNotification = mapEventToNotification.get(i).getSecond();
				}
				try {
					if (foundNotification != null
							&& CheckUtil.safeCheckEventNotificationWithinMilli(foundNotification, repPeriod * 1250l)) {
						notification = foundNotification;
						no_found_notifs++;
					} else {
						notification = NotificationUtil.getNotification(sub, eventIndex, notification, metricsService,
								metricsCacheService);
					}
				} catch (JsonMappingException e) {
					logger.error("Error building the notification for sub: " + id
							+ ". Data is no longer available for this event", e);
					// add failureEventInfo
					sub.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent())
							.failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
					continue;
				} catch (JsonProcessingException e) {
					logger.error("Error building the notification for sub: " + id
							+ ". Data is no longer available for this event", e);
					// add failureEventInfo
					sub.addFailEventReportsItem(new FailureEventInfo().event(event.getEvent())
							.failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
					continue;
				} catch (Exception e) {
					logger.error("Failed to collect data for event(timescaledb error)", e);
					stop();
					continue;
				}
				if (notification == null) {
					continue;
				}
				if (mapEventToNotification.size() > 2 * max_subs_per_process) {
					mapEventToNotification.clear();
				}
				if (i != -1) {
					mapEventToNotification.set(i, Pair.of(mapEventToNotification.get(i).getFirst(), notification));
				} else {
					mapEventToNotification.add(Pair.of(event, notification));
				}
				tsdb_req_delay += (System.nanoTime() - st) / 1000l;
				// save the sent notification to a second database
				st = System.nanoTime();
				notificationService.create(notification);
				notif_save_delay += (System.nanoTime() - st) / 1000l;
				// check if period has passed -> notify client (or if threshold has been
				// reached)
				long st_if = System.nanoTime();
				if ((repPeriod != 0
						&& OffsetDateTime.now().compareTo(entry.getValue().plusSeconds((long) repPeriod)) > 0) ||
						(repPeriod == 0 && thresholdReached(event, notification))) {
					st = System.nanoTime();
					HttpEntity<NnwdafEventsSubscriptionNotification> client_request = new HttpEntity<>(notification);
					try {
						CompletableFuture
								.supplyAsync(() -> restTemplate.postForEntity(sub.getNotificationURI() + "/notify",
										client_request, NnwdafEventsSubscriptionNotification.class))
								.thenAccept(client_response -> {
									if (client_response == null || !client_response.getStatusCode().is2xxSuccessful()) {
										logger.error("Client missed a notification for subscription with id: "
												+ sub.getId());
									}
								});
					} catch (RestClientException e) {
						logger.error("Error connecting to client " + sub.getNotificationURI());
						logger.info(e.toString());
					}
					client_delay += (System.nanoTime() - st) / 1000l;
					// if notifying the client was successful update the map with the current time
					st = System.nanoTime();
					lastNotifTimes.put(Pair.of(entry.getKey().getFirst(), eventIndex), OffsetDateTime.now());
					no_sent_notifs++;
					no_sent_kilobytes += notification.toString().getBytes().length / 1000;
					total_sent_notifs++;
					section_c += (System.nanoTime() - st) / 1000l;
				}
				section_b += (System.nanoTime() - st_if) / 1000l;
			}
			// System.out.print(",subs="+subs.size());
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
			// System.out.print(" || sub query time: "+(System.nanoTime()-st_sub) /
			// 1000000l+"ms");
			st = System.nanoTime();
			// make a copy of the map
			oldNotifTimes.clear();
			for (Map.Entry<Pair<Long, Integer>, OffsetDateTime> entry : lastNotifTimes.entrySet()) {
				oldNotifTimes.put(Pair.of(entry.getKey().getFirst(), entry.getKey().getSecond()), entry.getValue());
			}
			lastNotifTimes.clear();
			repPeriods.clear();
			subIndexes.clear();
			no_served_subs = 0;
			for (int i = 0; i < subs.size(); i++) {
				Long id = subs.get(i).getId();
				for (int j = 0; j < subs.get(i).getEventSubscriptions().size(); j++) {
					Integer period = NotificationUtil.needsServing(subs.get(i), j);
					if (period != null) {
						no_served_subs++;
						repPeriods.put(Pair.of(id, j), period);
						if (oldNotifTimes.get(Pair.of(id, j)) == null) {
							lastNotifTimes.put(Pair.of(id, j), OffsetDateTime.now());
						} else {
							lastNotifTimes.put(Pair.of(id, j), oldNotifTimes.get(Pair.of(id, j)));
						}
						subIndexes.put(subs.get(i).getId(), i);
					}
				}
			}
			double total = (System.nanoTime() - start) / 1_000_000l;
			long totalLong = (long) total;
			avg_io_delay += (long) (tsdb_req_delay + client_delay + notif_save_delay) / 1_000l;
			avg_program_delay += (long) (total - avg_io_delay);
			printPerfB(tsdb_req_delay, client_delay, notif_save_delay, no_sent_notifs, no_sent_kilobytes, total);
			if (no_sent_notifs != 0) {
				avg_client_delay_per_notif += client_delay / no_sent_notifs;
				counter++;
			}
			// wait till one quarter of a second (min period) passes (10^9/4 nanoseconds)
			long wait_time = (long) Constants.MIN_PERIOD_SECONDS * 250l;
			if (totalLong < wait_time) {
				try {
					Thread.sleep(wait_time - totalLong);
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();
					continue;
				}
			}
		}
		System.out.println("no of found notifications = " + no_found_notifs);
		System.out.println("==NotifListener(" + no_notifEventListeners + ") finished===");
		synchronized (notifLock) {
			if (no_notifEventListeners > 0) {
				no_notifEventListeners--;
			} else {
				System.out.println("==(NotifListener manually stopped)");
			}
		}
		// logger.info("avg_client_delay_per_notif="+avg_client_delay_per_notif /
		// counter+"ms");
		logger.info("avg io delay= " + avg_io_delay / 1000l + "ms || avg program delay= " + avg_program_delay / 1000l
				+ "ms || avg total delay= " + (avg_io_delay + avg_program_delay) / 1000l + "ms");
		logger.info(total_sent_notifs + "/40000 notifications sent");
	}

	private boolean thresholdReached(EventSubscription event, NnwdafEventsSubscriptionNotification notification) {
		NwdafEventEnum eType = event.getEvent().getEvent();

		switch (eType) {
			case NF_LOAD:
				List<ThresholdLevel> thresholdLevels = event.getNfLoadLvlThds();
				if (thresholdLevels == null || thresholdLevels.isEmpty()) {
					return false;
				}
				int size = Math.min(notification.getEventNotifications().get(0).getNfLoadLevelInfos().size(),
						thresholdLevels.size());
				List<NfLoadLevelInformation> presentNfLevelInformations = ParserUtil
						.parsePresentNfLoadLevelInformations(
								notification.getEventNotifications().get(0).getNfLoadLevelInfos());
				int index;
				String[] propertyNames = { "nfCpuUsage", "nfMemoryUsage", "nfStorageUsage", "nfLoadLevelAverage" };
				for (int i = 0; i < size; i++) {
					NfLoadLevelInformation nfLoadLevelInformation = presentNfLevelInformations.get(i);
					if (thresholdLevels.get(i) == null) {
						continue;
					}
					boolean[] isThresholdBooleans = { (thresholdLevels.get(i).getNfCpuUsage() != null
							&& nfLoadLevelInformation.getNfCpuUsage() >= thresholdLevels.get(i).getNfCpuUsage()),
							(thresholdLevels.get(i).getNfMemoryUsage() != null && nfLoadLevelInformation
									.getNfMemoryUsage() >= thresholdLevels.get(i).getNfMemoryUsage()),
							(thresholdLevels.get(i).getNfStorageUsage() != null && nfLoadLevelInformation
									.getNfStorageUsage() >= thresholdLevels.get(i).getNfStorageUsage()),
							(thresholdLevels.get(i).getNfLoadLevel() != null && nfLoadLevelInformation
									.getNfLoadLevelAverage() >= thresholdLevels.get(i).getNfLoadLevel()) };
					int booleanIndex = -1;
					for (int j = 0; j < isThresholdBooleans.length; j++) {
						if (isThresholdBooleans[j]) {
							booleanIndex = j;
							break;
						}
					}
					if (booleanIndex != -1) {
						index = notification.getEventNotifications().get(0).getNfLoadLevelInfos()
								.indexOf(nfLoadLevelInformation);
						int[] propertyValues = {
								notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
										.getNfCpuUsage(),
								notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
										.getNfMemoryUsage(),
								notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
										.getNfStorageUsage(),
								notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
										.getNfLoadLevelAverage() };
						notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
								.setThresholdProperty(propertyNames[booleanIndex]);
						notification.getEventNotifications().get(0).getNfLoadLevelInfos().get(index)
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

	public static Integer getNo_notifEventListeners() {
		return no_notifEventListeners;
	}

	public static void setNo_notifEventListeners(Integer no) {
		no_notifEventListeners = no;
	}

	public static Object getNotifLock() {
		return notifLock;
	}

	public static void stop() {
		synchronized (notifLock) {
			no_notifEventListeners--;
		}
	}

	private void printPerfA(long loop_section, double section_a, double section_b, double section_c,
			int no_served_subs) {
		// System.out.print("loop_section="+loop_section / 1000000l+"ms");
		// System.out.print(" || section_a="+section_a / 1000l+"ms");
		// System.out.print(" || section_b="+section_b / 1000l+"ms");
		// System.out.print(" || section_c="+section_c / 1000l+"ms");
		System.out.print(" || served_subs=" + no_served_subs);
	}

	private void printPerfB(double tsdb_req_delay, double client_delay, double notif_save_delay, int no_sent_notifs,
			int no_sent_kilobytes, double total) {
		// System.out.print(" || maps="+(System.nanoTime()-st) / 1000l+"μs");
		System.out.print(" || tsdb_req_delay: " + (long) tsdb_req_delay / 1000l + "ms");
		// System.out.print(" || client_delay:"+(long)client_delay / 1000l+"ms");
		// System.out.print(" || notif_save_delay:"+(long)notif_save_delay /
		// 1000l+"ms");
		System.out.print(" || no_sent_notifs:" + no_sent_notifs);
		System.out.print(" || no_sent_kilobytes:" + no_sent_kilobytes + "KB");
		System.out.print(" || total delay: " + total + "ms\n");
	}
}