package io.nwdaf.eventsubscription.notify;

import java.io.IOException;
import java.lang.Exception;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;

import io.nwdaf.eventsubscription.controller.http.NwdafFailureException;
import io.nwdaf.eventsubscription.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.customModel.DiscoverMessage;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.kafka.KafkaConsumer;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.notify.responsetypes.AggregateChecksForAOIResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetGlobalNotifMethodAndRepPeriodResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetNotifMethodAndRepPeriodsResponse;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import io.nwdaf.eventsubscription.utilities.OtherUtil;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static io.nwdaf.eventsubscription.NwdafSubApplication.*;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.latestWakeUpMessageEventMap;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.latestDiscoverMessageEventMap;
import static io.nwdaf.eventsubscription.kafka.KafkaConsumer.discoverMessageQueue;
import static io.nwdaf.eventsubscription.utilities.CheckUtil.safeCheckListNotEmpty;
import static io.nwdaf.eventsubscription.utilities.CheckUtil.safeCheckObjectsEquals;
import static io.nwdaf.eventsubscription.utilities.Constants.*;
import static io.nwdaf.eventsubscription.utilities.OtherUtil.*;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.*;


public class NotificationUtil {
    private static final Logger logger = LoggerFactory.getLogger(NotificationUtil.class);

    // check if subscription needs to be served and return the repetition period (0
    // for threshold)
    public static Integer needsServing(NnwdafEventsSubscription body, Integer eventIndex, Boolean immediateReporting) {
        NotificationMethodEnum notificationMethod = null;
        int repetitionPeriod = -1;
        EventSubscription eventSub = body.getEventSubscriptions().get(eventIndex);
        if (eventSub != null) {
            if (eventSub.getNotificationMethod() != null
                    && eventSub.getNotificationMethod().getNotifMethod() != null) {
                notificationMethod = eventSub.getNotificationMethod()
                        .getNotifMethod();
                if (notificationMethod.equals(NotificationMethodEnum.PERIODIC)) {
                    repetitionPeriod = eventSub.getRepetitionPeriod();
                }
            }
        }

        if (body.getEvtReq() != null) {
            if (body.getEvtReq().getNotifFlag() != null && safeCheckObjectsEquals(
                    body.getEvtReq().getNotifFlag().getNotifFlag(), NotificationFlagEnum.DEACTIVATE) && !immediateReporting) {
                return null;
            }
            if (!safeCheckObjectsEquals(body.getEvtReq().getNotifMethod().getNotifMethod(), null)) {
                notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
                if (body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
                    repetitionPeriod = body.getEvtReq().getRepPeriod();
                }
            }
        }
        if (safeCheckObjectsEquals(notificationMethod, NotificationMethodEnum.THRESHOLD)) {
            return 0;
        }
        return repetitionPeriod;
    }

    // check if client wants past/future data by setting no_secs parameter
    public static long[] findRequestedDataOffset(EventSubscription eventSub) {
        long no_secs = Integer.MIN_VALUE;
        int isFuture = 0;
        long end = 0L;
        if (eventSub.getExtraReportReq() == null) {
            return new long[]{Integer.MIN_VALUE, 0, 0};
        }
        if (eventSub.getExtraReportReq().getEndTs() != null && eventSub.getExtraReportReq().getStartTs() != null) {
            if (eventSub.getExtraReportReq().getEndTs().toEpochSecond() < eventSub.getExtraReportReq().getStartTs()
                    .toEpochSecond()) {
                return new long[]{Integer.MIN_VALUE, 0, 0};
            }
            // future data
            if (eventSub.getExtraReportReq().getEndTs().toEpochSecond() > OffsetDateTime.now().toEpochSecond()) {
                // both stat predictions not allowed
                if (eventSub.getExtraReportReq().getStartTs().toEpochSecond() < OffsetDateTime.now().toEpochSecond()) {
                    return new long[]{Integer.MIN_VALUE, 2, 0};
                }
                isFuture = 1;
            }
            no_secs = OffsetDateTime.now().toEpochSecond()
                    - eventSub.getExtraReportReq().getStartTs().toEpochSecond();
            end = OffsetDateTime.now().toEpochSecond() - eventSub.getExtraReportReq().getEndTs().toEpochSecond();
        } else {
            if (eventSub.getExtraReportReq().getStartTs() != null) {
                // if no endTs is given , get the duration by subtracting from current time
                no_secs = OffsetDateTime.now().toEpochSecond() - eventSub.getExtraReportReq().getStartTs().toEpochSecond();
            }
            // if startTs & endTs aren't given , check offsetPeriod for a duration value:
            else {
                if (eventSub.getExtraReportReq().getOffsetPeriod() != null) {
                    if (eventSub.getExtraReportReq().getOffsetPeriod() < 0) {
                        no_secs = (-1) * eventSub.getExtraReportReq().getOffsetPeriod();
                    }
                    // future data
                    else if (eventSub.getExtraReportReq().getOffsetPeriod() > 0) {
                        isFuture = 1;
                        no_secs = eventSub.getExtraReportReq().getOffsetPeriod();
                    }
                }
            }
        }
        return new long[]{no_secs, isFuture, end};
    }

    // get the notification for the event subscription by querying the given service
    public static NnwdafEventsSubscriptionNotification getNotification(NnwdafEventsSubscription sub, Integer index,
                                                                       NnwdafEventsSubscriptionNotification notification, MetricsService metricsService,
                                                                       MetricsCacheService metricsCacheService) {
        OffsetDateTime now = OffsetDateTime.now();
        EventSubscription eventSub = sub.getEventSubscriptions().get(index);
        NwdafEventEnum eType = eventSub.getEvent().getEvent();
        NotificationBuilder notifBuilder = new NotificationBuilder();
        ObjectWriter ow = new ObjectMapper().writer();
        String params = null;
        String columns = "";
        long[] findOffset = findRequestedDataOffset(eventSub);
        Long no_secs = findOffset[0] != Integer.MIN_VALUE ? findOffset[0] : Constants.MIN_PERIOD_SECONDS;
        long isFuture = findOffset[1];
        Integer repPeriod = needsServing(sub, index, true);
        // future data not implemented | subscription doesn't need serving:
        if (isFuture == 1 || repPeriod == null) {
            return null;
        }
        NetworkAreaInfo networkArea = eventSub.getNetworkArea();
        TargetUeInformation tgtUe = eventSub.getTgtUe();
        boolean isAnyUe = tgtUe == null || tgtUe.isAnyUe() == null || tgtUe.isAnyUe();
        switch (eType) {
            case NF_LOAD:
                List<NfLoadLevelInformation> nfloadlevels;
                List<String> filterTypes = new ArrayList<>();
                // choose the querry filter: nfinstanceids take priority over nfsetids and supis
                // over all
                // Supis filter (checks if the supis list on each nfinstance contains any of the
                // supis in the sub request)
                if (!isAnyUe && safeCheckListNotEmpty(tgtUe.getSupis())) {
                    params = parseQuerryFilterContains(tgtUe.getSupis(), "supis");
                    filterTypes.add("supis");
                } else if (safeCheckListNotEmpty(eventSub.getNfInstanceIds())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(eventSub.getNfInstanceIds(), "nfInstanceId"));
                    filterTypes.add("nfInstanceId");
                } else if (safeCheckListNotEmpty(eventSub.getNfSetIds())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(eventSub.getNfSetIds(), "nfSetId"));
                    filterTypes.add("nfSetId");
                } else if (networkArea != null
                        && safeCheckListNotEmpty(networkArea.getContainedAreaIds())
                        && (safeCheckListNotEmpty(networkArea.getEcgis()) ||
                        safeCheckListNotEmpty(networkArea.getNcgis()) ||
                        safeCheckListNotEmpty(networkArea.getGRanNodeIds()) ||
                        safeCheckListNotEmpty(networkArea.getTais()))) {
                    // aggregate container areas for the aoi inside the sub object as valid filters
                    params = parseQuerryFilter(parseListToFilterList(
                            networkArea.getContainedAreaIds(), "areaOfInterestId"));
                    filterTypes.add("areaOfInterestId");
                }
                // Network Slice Instances filter
                else if (safeCheckListNotEmpty(eventSub.getSnssaia())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(
                                    ParserUtil.parseObjectListToFilterList(
                                            ConvertUtil.convertObjectWriterList(eventSub.getSnssaia(), ow)),
                                    "snssai"));
                    filterTypes.add("snssai");
                }
                // NfTypes filter
                else if (safeCheckListNotEmpty(eventSub.getNfTypes())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(
                                    ParserUtil.parseObjectListToFilterList(
                                            ConvertUtil.convertObjectWriterList(eventSub.getNfTypes(), ow)),
                                    "nfType"));
                    filterTypes.add("nfType");
                }
                // if given analyticsubsets list in the sub request, select only the appropriate
                // columns in the request
                columns = setNfloadPostgresColumns("", eventSub.getListOfAnaSubsets());

                try {
                    nfloadlevels = metricsService.findAllInLastIntervalByFilterAndOffset(
                            params,
                            no_secs,
                            findOffset[2],
                            repPeriod,
                            columns);
                    // nfloadlevels =
                    // metricsCacheService.findAllInLastIntervalByFilterAndOffset(eventSub,
                    // filterTypes, params, no_secs, repPeriod, columns);
                } catch (Exception e) {
                    NwdafSubApplication.getLogger()
                            .error("Service error for eventType: " + eType + " and subId: " + sub.getId(), e);
                    return null;
                }
                if (nfloadlevels == null || nfloadlevels.isEmpty()) {
                    return null;
                }
                // if given analyticsubsets list in the sub request, set the non-included info
                // to null
                nfloadlevels = setNfloadNonIncludedInfoNull(nfloadlevels, eventSub.getListOfAnaSubsets());
                notification = notifBuilder.addEvent(
                        notification,
                        NwdafEventEnum.NF_LOAD,
                        now.minusSeconds(no_secs),
                        findOffset[2] > 0 ? now.minusSeconds(findOffset[2]) : null,
                        now, null,
                        null,
                        null,
                        nfloadlevels);
                // logger.info("notified with:
                // "+notification.getEventNotifications().get(0).getTimeStampGen());
                break;
            case UE_MOBILITY:
                List<UeMobility> ueMobilities;
                if (!isAnyUe && safeCheckListNotEmpty(tgtUe.getSupis())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(tgtUe.getSupis(), "supi"));
                } else if (!isAnyUe && safeCheckListNotEmpty(tgtUe.getIntGroupIds())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(tgtUe.getIntGroupIds(), "intGroupId"));
                } else if (eventSub.getVisitedAreas() != null
                        && safeCheckListNotEmpty(eventSub.getVisitedAreas())) {
                    List<String> validVisitedAreas = new ArrayList<>();
                    for (NetworkAreaInfo aoi : eventSub.getVisitedAreas()) {
                        if (aoi != null && safeCheckListNotEmpty(aoi.getContainedAreaIds()) &&
                                (safeCheckListNotEmpty(aoi.getEcgis()) ||
                                        safeCheckListNotEmpty(aoi.getNcgis()) ||
                                        safeCheckListNotEmpty(aoi.getGRanNodeIds()) ||
                                        safeCheckListNotEmpty(aoi.getTais()))) {
                            List<String> areaOfInterestIds = safeParseListString(Collections.singletonList(aoi.getContainedAreaIds()));
                            validVisitedAreas.addAll(areaOfInterestIds);
                        }
                    }
                    validVisitedAreas = validVisitedAreas.stream().distinct().toList();
                    params = parseQuerryFilterContains(parseListToFilterList(validVisitedAreas, "areaOfInterestId"), "areaOfInterestIds");
                }
                try {
                    ueMobilities = metricsService.findAllUeMobilityInLastIntervalByFilterAndOffset(params, no_secs,
                            findOffset[2], repPeriod, columns);
                } catch (Exception e) {
                    NwdafSubApplication.getLogger()
                            .error("Service error for eventType: " + eType + " and subId: " + sub.getId(), e);
                    return null;
                }
                if (ueMobilities == null || ueMobilities.isEmpty()) {
                    return null;
                }
                notification = notifBuilder.addEvent(notification, NwdafEventEnum.UE_MOBILITY, null, null, now, null,
                        null, null, ueMobilities);
                break;
            case UE_COMM:
                List<UeCommunication> ueCommunications;
                if (!isAnyUe && safeCheckListNotEmpty(tgtUe.getSupis())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(tgtUe.getSupis(), "supi"));
                } else if (!isAnyUe && safeCheckListNotEmpty(tgtUe.getIntGroupIds())) {
                    params = parseQuerryFilter(
                            parseListToFilterList(tgtUe.getIntGroupIds(), "intGroupId"));
                } else if (networkArea != null
                        && safeCheckListNotEmpty(networkArea.getContainedAreaIds())
                        && (safeCheckListNotEmpty(networkArea.getEcgis()) ||
                        safeCheckListNotEmpty(networkArea.getNcgis()) ||
                        safeCheckListNotEmpty(networkArea.getGRanNodeIds()) ||
                        safeCheckListNotEmpty(networkArea.getTais()))) {
                    // aggregate container areas for the aoi inside the sub object as valid filters
                    params = parseQuerryFilter(parseListToFilterList(
                            networkArea.getContainedAreaIds(), "areaOfInterestId"));
                }
                try {
                    ueCommunications = metricsService.findAllUeCommunicationInLastIntervalByFilterAndOffset(params, no_secs,
                            findOffset[2], repPeriod, columns);
                } catch (Exception e) {
                    NwdafSubApplication.getLogger()
                            .error("Service error for eventType: " + eType + " and subId: " + sub.getId(), e);
                    return null;
                }
                if (ueCommunications == null || ueCommunications.isEmpty()) {
                    return null;
                }
                notification = notifBuilder.addEvent(notification, NwdafEventEnum.UE_COMM, null, null, now, null,
                        null, null, ueCommunications);
                break;
            default:
                break;
        }
        setPastOffsetCutoff(eventSub, repPeriod);
        // notifCorrId field is used as the eventSubscription index, so that the client
        // can group the notifications it receives
        notification.setNotifCorrId(index.toString());
        return notification;
    }

    public static boolean[] handleDiscoverMessageQueue(List<DiscoverMessage> discoverMessages, boolean hasData, boolean responded) {
        while (!discoverMessageQueue.isEmpty()) {
            String msg = discoverMessageQueue.poll();
            if (msg == null) {
                logger.error("InterruptedException: Couldn't take msg from discover queue");
                break;
            }
            DiscoverMessage discoverMessage = DiscoverMessage.fromString(msg);
            if (discoverMessage.getAvailableOffset() == null) {
                discoverMessage.setAvailableOffset(0L);
            }
            if (discoverMessage.getHasData() == null) {
                discoverMessage.setHasData(false);
            }
            long diff = Instant.now().getNano() - discoverMessage.getTimestamp().getNano();
            boolean tooOldMsg = diff > 500_000_000L && diff > discoverMessage.getAvailableOffset() * 1_000_000_000L;
            if (!tooOldMsg) {
                discoverMessages.add(discoverMessage);
                if (discoverMessage.getRequestedEvent() != null) {
                    latestDiscoverMessageEventMap.put(discoverMessage.getRequestedEvent(), discoverMessage);
                }
                if (discoverMessage.getCollectorInstanceId() != null) {
                    KafkaConsumer.eventCollectorIdSet.add(discoverMessage.getCollectorInstanceId());
                }
            }
            hasData = discoverMessage.getHasData() && (!tooOldMsg);
            responded = true;
        }
        return new boolean[]{hasData, responded};
    }

    public static Long[] waitForDataProducer(WakeUpMessage wakeUpMessage) throws InterruptedException, IOException {

        KafkaConsumer.startListening();
        Thread.sleep(0, 1_000);
        NwdafEventEnum requestedEvent = wakeUpMessage.getRequestedEvent();

        // check if there is data already available

        long maxWait = 4_000L;
        OffsetDateTime latestWakeUpMessageTimestamp = latestWakeUpMessageEventMap.get(requestedEvent) != null ?
                latestWakeUpMessageEventMap.get(requestedEvent).getTimestamp() : null;
        if (wakeUpMessage != null) {
            latestWakeUpMessageEventMap.put(wakeUpMessage.getRequestedEvent(), wakeUpMessage);
        }
        DiscoverMessage lastDiscoverMessage = latestDiscoverMessageEventMap.get(requestedEvent);
        if (lastDiscoverMessage != null && !lastDiscoverMessage.getHasData() && latestWakeUpMessageTimestamp != null &&
                latestWakeUpMessageTimestamp.isAfter(OffsetDateTime.now().minusSeconds(60))) {
            maxWait = 50L;
        }

        // wait for data sending & saving to start
        boolean responded = false;
        List<DiscoverMessage> discoverMessages = new ArrayList<>();
        boolean hasData = false;
        long start = System.nanoTime();
        while (System.nanoTime() <= start + maxWait * 1_000_000L) {
            boolean[] result = handleDiscoverMessageQueue(discoverMessages, hasData, responded);
            hasData = result[0];
            responded = result[1];
            if (hasData && responded) {
                discoverMessages = discoverMessages.stream().distinct().toList();
                break;
            }
            Thread.sleep(0, 1_000);
        }
        System.out.println("kafka_wait_time= " + (System.nanoTime() - start) / 1_000_000L + "ms");
        long isDataAvailable = 0L;
        long expectedWaitTime = 0L;
        for (DiscoverMessage msg : discoverMessages) {
            System.out.println("discover msg: " + msg);
            long availableOffset = msg.getAvailableOffset() != null ? msg.getAvailableOffset() : 0L;
            boolean messageHasData = msg.getHasData() != null && msg.getHasData();
            if (messageHasData) {
                if (msg.getRequestedOffset() == null
                        || msg.getRequestedOffset() <= Constants.MIN_PERIOD_SECONDS) {
                    isDataAvailable = 1;
                    break;
                } else if (availableOffset >= msg.getRequestedOffset()) {
                    isDataAvailable = 1;
                    break;
                } else {
                    expectedWaitTime = msg.getRequestedOffset() - availableOffset;
                }
            }
            if (!messageHasData &&
                    msg.getRequestedOffset() != null &&
                    msg.getExpectedWaitTime() != null) {
                expectedWaitTime = msg.getExpectedWaitTime() + msg.getRequestedOffset();
            }
        }
        if (isDataAvailable == 1) {
            start = System.nanoTime();
            while (KafkaConsumer.isListening.get() && !KafkaConsumer.eventConsumerStartedSaving.get(requestedEvent) && System.nanoTime() <= start + maxWait * 1_000_000L) {
                Thread.sleep(0, 1_000);
            }
        }
        System.out.println("consumer_save_wait_time= " + (System.nanoTime() - start) / 1_000_000L + "ms");
        return new Long[]{isDataAvailable, expectedWaitTime};
    }

    // check if for this event subscription the requested area of interest
// is inside (or equals to) the service area of this NWDAF instance
// the checks are for when the serializer initializes the lists inside aoi
// object with null
    public static AggregateChecksForAOIResponse aggregateChecksForAOI(NetworkAreaInfo requestNetworkArea) {
        NetworkAreaInfo matchingArea = null;
        boolean insideServiceArea = false;
        boolean failed_notif = false;
        NwdafFailureCodeEnum failCode = null;
        if (requestNetworkArea != null && requestNetworkArea.getId() != null) {
            matchingArea = ExampleAOIsMap.get(requestNetworkArea.getId());
            insideServiceArea = Constants.ServingAreaOfInterest.containsArea(matchingArea);
        }
        if (requestNetworkArea != null
                && (CheckUtil.safeCheckNetworkAreaNotEmpty(requestNetworkArea) || requestNetworkArea.getId() != null)) {
            if (!(CheckUtil.safeCheckNetworkAreaNotEmpty(requestNetworkArea)
                    && Constants.ServingAreaOfInterest.containsArea(requestNetworkArea))
                    && (matchingArea == null || !insideServiceArea)) {
                failed_notif = true;
                failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
                System.out.println("not inside serving aoi");
            } else {
                if (ExampleAOIsToUUIDsMap.containsKey(requestNetworkArea)) {
                    // check if its lists equal one of the known AOIs -> set the id
                    requestNetworkArea.id(ExampleAOIsToUUIDsMap.get(requestNetworkArea));
                    System.out.println("same as aoi with id: " + requestNetworkArea.getId());
                } else if (requestNetworkArea.getId() != null
                        && ExampleAOIsMap.containsKey(requestNetworkArea.getId())) {
                    // check if id equals to a known AOI -> set the area lists
                    NetworkAreaInfo matchingAOI = ExampleAOIsMap.get(requestNetworkArea.getId());
                    requestNetworkArea.ecgis(matchingAOI.getEcgis()).ncgis(matchingAOI.getNcgis())
                            .gRanNodeIds(matchingAOI.getGRanNodeIds()).tais(matchingAOI.getTais());
                } else {
                    // if new AOI create id if it doesnt have one and add it to the known AOIs
                    if (requestNetworkArea.getId() == null) {
                        requestNetworkArea.id(UUID.randomUUID());
                    }
                    ExampleAOIsMap.put(requestNetworkArea.getId(), requestNetworkArea);
                    ExampleAOIsToUUIDsMap.put(requestNetworkArea, requestNetworkArea.getId());
                }
                // aggregate known areas that are inside of this area of interest
                for (Map.Entry<UUID, NetworkAreaInfo> entry : ExampleAOIsMap.entrySet()) {
                    UUID key = entry.getKey();
                    NetworkAreaInfo aoi = entry.getValue();
                    if (requestNetworkArea.containsArea(aoi) || key.equals(requestNetworkArea.getId())) {
                        requestNetworkArea.addContainedAreaIdsItem(key);
                    }
                }
                requestNetworkArea
                        .setContainedAreaIds(ParserUtil.removeDuplicates(requestNetworkArea.getContainedAreaIds()));
            }
        }
        return AggregateChecksForAOIResponse.builder()
                .failCode(failCode)
                .failed_notif(failed_notif)
                .insideServiceArea(insideServiceArea)
                .matchingArea(matchingArea)
                .build();
    }

    // negotiate the supported features with the client
    public static List<Integer> negotiateSupportedFeatures(NnwdafEventsSubscription body) {
        List<Integer> negotiatedFeaturesList;
        if (body.getSupportedFeatures() != null && !body.getSupportedFeatures().isEmpty()) {
            if (!Constants.supportedFeatures.equals(body.getSupportedFeatures()) &&
                    CheckUtil.listInside(Constants.supportedFeaturesList,
                            ConvertUtil.convertFeaturesToList(body.getSupportedFeatures()))) {
                negotiatedFeaturesList = ConvertUtil.convertFeaturesToList(body.getSupportedFeatures());
            } else {
                body.setSupportedFeatures(Constants.supportedFeatures);
                negotiatedFeaturesList = Constants.supportedFeaturesList;
            }
        } else {
            body.setSupportedFeatures(Constants.supportedFeatures);
            negotiatedFeaturesList = Constants.supportedFeaturesList;
        }
        return negotiatedFeaturesList;
    }

    // get global notification method and period if they exist
    public static GetGlobalNotifMethodAndRepPeriodResponse getGlobalNotifMethodAndRepPeriod(
            ReportingInformation evtReq) {
        Integer repetionPeriod = null;
        NotificationMethodEnum notificationMethod = null;
        boolean muted = false;
        Boolean immRep = false;
        if (evtReq != null) {
            if (evtReq.getNotifMethod() != null && evtReq.getNotifMethod().getNotifMethod() != null) {
                notificationMethod = evtReq.getNotifMethod().getNotifMethod();
                if (evtReq.getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
                    repetionPeriod = evtReq.getRepPeriod();
                }
                if (evtReq.getNotifFlag() != null) {
                    muted = evtReq.getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE);
                }
            }
            if (evtReq.isImmRep() != null) {
                immRep = evtReq.isImmRep();
            }
        }
        return GetGlobalNotifMethodAndRepPeriodResponse.builder()
                .immRep(immRep)
                .muted(muted)
                .notificationMethod(notificationMethod)
                .repetionPeriod(repetionPeriod)
                .build();
    }

    // get period and notification method for each event
    public static GetNotifMethodAndRepPeriodsResponse getNotifMethodAndRepPeriods(
            List<EventSubscription> eventSubscriptions,
            NotificationMethodEnum notificationMethod,
            Integer repetionPeriod) {
        List<Integer> invalid_events = new ArrayList<>();
        Map<Integer, NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
        Map<Integer, Integer> eventIndexToRepPeriodMap = new HashMap<>();
        int noValidEvents = eventSubscriptions.size();
        for (int i = 0; i < eventSubscriptions.size(); i++) {
            EventSubscription e = eventSubscriptions.get(i);
            if (e == null || e.getEvent() == null || e.getEvent().getEvent() == null) {
                noValidEvents--;
                invalid_events.add(i);
                continue;
            }
            if (notificationMethod == null) {
                if (e.getNotificationMethod() != null) {
                    eventIndexToNotifMethodMap.put(i, e.getNotificationMethod().getNotifMethod());
                } else {
                    eventIndexToNotifMethodMap.put(i, NotificationMethodEnum.THRESHOLD);
                }

            } else {
                eventIndexToNotifMethodMap.put(i, notificationMethod);
            }
            if (eventIndexToNotifMethodMap.get(i) != null) {
                if (eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
                    if (repetionPeriod == null) {
                        eventIndexToRepPeriodMap.put(i, e.getRepetitionPeriod());
                    } else {
                        eventIndexToRepPeriodMap.put(i, repetionPeriod);
                    }

                }
            }
            e = OtherUtil.setShapes(e);
            eventSubscriptions.set(i, e);
        }
        return GetNotifMethodAndRepPeriodsResponse.builder()
                .eventIndexToNotifMethodMap(eventIndexToNotifMethodMap)
                .eventIndexToRepPeriodMap(eventIndexToRepPeriodMap)
                .invalid_events(invalid_events)
                .no_valid_events(noValidEvents)
                .build();
    }

    // check which subscriptions can be served
    public static List<Boolean> checkCanServeSubscriptions(Integer noValidEvents,
                                                           NnwdafEventsSubscription body,
                                                           Map<Integer, NotificationMethodEnum> eventIndexToNotifMethodMap,
                                                           Map<Integer, Integer> eventIndexToRepPeriodMap,
                                                           KafkaProducer kafkaProducer,
                                                           MetricsService metricsService,
                                                           MetricsCacheService metricsCacheService,
                                                           Boolean immediateReporting,
                                                           Long id,
                                                           Boolean isConsumer,
                                                           WebClient webClient,
                                                           String consumerUrl) throws NwdafFailureException {
        List<Boolean> canServeSubscription = new ArrayList<>(Collections.nCopies(noValidEvents, false));
        for (int i = 0; i < noValidEvents; i++) {
            EventSubscription eventSubscription = body.getEventSubscriptions().get(i);
            NwdafEventEnum eType = eventSubscription.getEvent().getEvent();
            boolean periodic = false;
            if (eventIndexToRepPeriodMap.get(i) != null) {
                periodic = eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC);
            }
            boolean failed_notif;
            NwdafFailureCodeEnum failCode;
            // check if eventType is supported
            if (!Constants.supportedEvents.contains(eType)) {
                body.addFailEventReportsItem(new FailureEventInfo()
                        .event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
                continue;
            }

            // check if period is valid (between 1sec and 10min) and if not add
            // failureReport
            if (periodic && (eventIndexToRepPeriodMap.get(i) < Constants.MIN_PERIOD_SECONDS
                    || eventIndexToRepPeriodMap.get(i) > Constants.MAX_PERIOD_SECONDS)) {
                body.addFailEventReportsItem(new FailureEventInfo()
                        .event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(NwdafFailureCodeEnum.OTHER)));
                continue;
            }
            // area of interest checks against the AOI of the serving NWDAF_SUB instance (this)
            AggregateChecksForAOIResponse aggregateChecksForAOIResponse = NotificationUtil
                    .aggregateChecksForAOI(eventSubscription.getNetworkArea());
            failed_notif = aggregateChecksForAOIResponse.getFailed_notif();
            failCode = aggregateChecksForAOIResponse.getFailCode();
            if (failed_notif) {
                body.addFailEventReportsItem(new FailureEventInfo()
                        .event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(failCode)));
                continue;
            }

            long[] findOffset = findRequestedDataOffset(eventSubscription);
            Long no_secs = findOffset[0] != Integer.MIN_VALUE ? findOffset[0] : null;

            if (findOffset[1] == 2) {
                body.addFailEventReportsItem(new FailureEventInfo()
                        .event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(NwdafFailureCodeEnum.BOTH_STAT_PRED_NOT_ALLOWED)));
                throw new NwdafFailureException("Both future and past predictions not allowed", null,
                        NwdafFailureCodeEnum.BOTH_STAT_PRED_NOT_ALLOWED.toString());
            }

            if (findOffset[1] == 1) {
                body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(NwdafFailureCodeEnum.OTHER)));
                throw new UnsupportedOperationException("/nnwdaf-eventsubscription/v1/subscriptions: Future data not implemented.");
            }

            WakeUpMessage wakeUpMessage = wakeUpDataProducer(kafkaProducer, eType, no_secs);

            // check whether data is already being gathered
            long start = no_secs == null ? MIN_PERIOD_SECONDS : no_secs;
            Integer period = eventIndexToRepPeriodMap.get(i);
            List<OffsetDateTime> metricsTimeStamps;
            boolean hasData;
            if (start > 3600 * 24) {
                metricsTimeStamps = metricsService.findAvailableHistoricMetricsTimeStamps(eType, start + 1, findOffset[2]);
                if (findOffset[2] < 5L * 60L) {
                    List<OffsetDateTime> concurrentTimeStamps = metricsService.findAvailableConcurrentMetricsTimeStampsWithOffset(eType, 5L * 60L + 1, findOffset[2], 60);
                    metricsTimeStamps.addAll(concurrentTimeStamps);
                }
                hasData = checkOffsetInsideAvailableData(start, findOffset[2] + 60, period, metricsTimeStamps, true);
            } else {
                metricsTimeStamps = metricsService.findAvailableConcurrentMetricsTimeStamps(eType, start + 1, findOffset[2]);
                hasData = checkOffsetInsideAvailableData(start, findOffset[2] + 1, period, metricsTimeStamps, false);
            }

            if ((!hasData && start > period)) {
                body.addFailEventReportsItem(new FailureEventInfo()
                        .event(eventSubscription.getEvent())
                        .failureCode(new NwdafFailureCode()
                                .failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
                throw new NwdafFailureException("No data available for event: " + eType + " and subId: " + id, null,
                        NwdafFailureCodeEnum.UNAVAILABLE_DATA.toString());
            }
            if (findOffset[2] > 0) {
                muteSubscription(body);
            }

            if (hasData) {
                canServeSubscription.set(i, true);

                if (immediateReporting) {
                    NnwdafEventsSubscriptionNotification notificationBeforeKafka = new NotificationBuilder().build(id).time(Instant.now());

                    try {
                        notificationBeforeKafka = getNotification(body, i, notificationBeforeKafka, metricsService,
                                metricsCacheService);
                    } catch (Exception e) {
                        logger.error("getNotification error for event: " + eType + " and subId: " + id, e);
                    }
                    if (notificationBeforeKafka == null) {
                        logger.error("Notification is NULL for event: " + eType + " and subId: " + id);
                    } else {
                        body.addEventNotificationsItem(notificationBeforeKafka.getEventNotifications().getFirst()
                                .rvWaitTime(0L));
                    }
                }
                logger.info("notifMethod=" + eventIndexToNotifMethodMap.get(i) + ", repPeriod="
                        + eventIndexToRepPeriodMap.get(i));
                continue;
            }

            // check whether data is available to be gathered from kafka
            NnwdafEventsSubscriptionNotification notification = new NotificationBuilder().build(id).time(Instant.now());
            boolean isDataAvailable = false;
            Long expectedWaitTime = null;
            try {
                Long[] wakeUpResult;
                if (isConsumer) {
                    wakeUpResult = waitForDataProducer(wakeUpMessage);
                } else {
                    wakeUpResult = sendWaitForDataProducerRequest(wakeUpMessage, webClient, consumerUrl);
                }
                isDataAvailable = wakeUpResult[0] == 1;
                expectedWaitTime = wakeUpResult[1];
            } catch (IOException e) {
                failed_notif = true;
                logger.error("Couldn't connect to kafka topic WAKE_UP.", e);
            } catch (InterruptedException e) {
                failed_notif = true;
                logger.error("Thread failed to wait for datacollection to start for event: " + eType, e);
            }
            if (!isDataAvailable) {
                failed_notif = true;
                logger.info("Informed by collector(s) that data is not available");
            }

            if (!failed_notif) {
                metricsTimeStamps = metricsService.findAvailableConcurrentMetricsTimeStamps(eType, no_secs, findOffset[2]);
                hasData = no_secs != null && checkOffsetInsideAvailableData(no_secs, findOffset[2] - 1, eventIndexToRepPeriodMap.get(i), metricsTimeStamps, false);
                if (immediateReporting) {
                    try {
                        notification = NotificationUtil.getNotification(body, i, notification, metricsService,
                                metricsCacheService);
                        if (body.getEvtReq() != null && notification != null) {
                            body.addEventNotificationsItem(notification.getEventNotifications().get(i));
                        }
                    } catch (Exception e) {
                        logger.error("getNotification error for event: " + eType + " and subId: " + id, e);
                    }
                    if (notification == null) {
                        logger.error("Notification is NULL for event: " + eType + " and subId: " + id);
                    } else {
                        body.addEventNotificationsItem(notification.getEventNotifications().getFirst()
                                .rvWaitTime(expectedWaitTime));
                    }
                }
                if (hasData) {
                    canServeSubscription.set(i, true);
                    logger.info("notifMethod=" + eventIndexToNotifMethodMap.get(i) + ", repPeriod="
                            + eventIndexToRepPeriodMap.get(i));
                } else {
                    body.addFailEventReportsItem(new FailureEventInfo()
                            .event(eventSubscription.getEvent())
                            .failureCode(new NwdafFailureCode()
                                    .failureCode(NwdafFailureCodeEnum.UNAVAILABLE_DATA)));
                }
            }
        }
        return canServeSubscription;
    }

    public static Long[] sendWaitForDataProducerRequest(WakeUpMessage wakeUpMessage, WebClient webClient, String consumerUrl) {
        try {
            return webClient.post()
                    .uri(consumerUrl + "/consumer/waitForDataProducer")
                    .bodyValue(wakeUpMessage)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new RuntimeException("Client Error! Status code: " + clientResponse.statusCode())))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new RuntimeException("Server Error! Status code: " + clientResponse.statusCode())))
                    .bodyToMono(Long[].class)
                    .block();
        } catch (RuntimeException e) {
            logger.error("Couldn't connect to consumer/waitForDataProducer.", e);
            return new Long[]{0L, null};
        }
    }

    public static WakeUpMessage wakeUpDataProducer(KafkaProducer kafkaProducer, NwdafEventEnum eType, Long no_secs) {
        WakeUpMessage wakeUpMessage = null;
        try {
            wakeUpMessage = WakeUpMessage.builder()
                    .requestedEvent(eType)
                    .nfInstancedId(NWDAF_INSTANCE_ID)
                    .requestedOffset(no_secs).build();
            kafkaProducer.sendMessage(wakeUpMessage.toString(), "WAKE_UP");
        } catch (IOException e) {
            logger.error("Couldn't connect to kafka topic WAKE_UP.", e);
        }
        return wakeUpMessage;
    }
}