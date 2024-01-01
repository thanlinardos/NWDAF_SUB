package io.nwdaf.eventsubscription.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import io.nwdaf.eventsubscription.controller.http.HttpServletRequestAdapter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionPublisher;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.notify.responsetypes.GetGlobalNotifMethodAndRepPeriodResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetNotifMethodAndRepPeriodsResponse;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static io.nwdaf.eventsubscription.controller.http.ValidateSubscriptionRequest.validateRequest;
import static io.nwdaf.eventsubscription.notify.NotificationUtil.*;

@RestController
public class SubscriptionsController implements SubscriptionsApi {

    private final Environment env;
    private final NotifyPublisher notifyPublisher;
    private final DataCollectionPublisher dataCollectionPublisher;
    private final DummyDataProducerPublisher dummyDataProducerPublisher;
    private final SubscriptionsService subscriptionService;
    private final MetricsService metricsService;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final MetricsCacheService metricsCacheService;
    private final Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);

    public SubscriptionsController(Environment env, NotifyPublisher notifyPublisher, DataCollectionPublisher dataCollectionPublisher, DummyDataProducerPublisher dummyDataProducerPublisher, SubscriptionsService subscriptionService, MetricsService metricsService, KafkaProducer kafkaProducer, ObjectMapper objectMapper, MetricsCacheService metricsCacheService) {
        this.env = env;
        this.notifyPublisher = notifyPublisher;
        this.dataCollectionPublisher = dataCollectionPublisher;
        this.dummyDataProducerPublisher = dummyDataProducerPublisher;
        this.subscriptionService = subscriptionService;
        this.metricsService = metricsService;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
        this.metricsCacheService = metricsCacheService;
    }


    @SneakyThrows
    @Override
    public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(@Valid NnwdafEventsSubscription body) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequestAdapter request = attributes != null ? new HttpServletRequestAdapter(attributes.getRequest()) : null;
        String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url") + "/nwdaf-eventsubscription/v1/subscriptions";
        System.out.println("Controller logic...");
        HttpHeaders responseHeaders = new HttpHeaders();
        int count_of_notif = 0;
        List<Integer> periods_to_serve = new ArrayList<>();

        validateRequest(body, attributes, request);

        logger.info(String.valueOf(Thread.currentThread()));

        List<Integer> negotiatedFeaturesList = negotiateSupportedFeatures(body);
        logger.info("negotiatedFeaturesList:" + negotiatedFeaturesList.toString());

        GetGlobalNotifMethodAndRepPeriodResponse globalResponse = getGlobalNotifMethodAndRepPeriod(body.getEvtReq());
        GetNotifMethodAndRepPeriodsResponse getResponse = getNotifMethodAndRepPeriods(
                body.getEventSubscriptions(),
                globalResponse.getNotificationMethod(),
                globalResponse.getRepetionPeriod());
        for (int i = 0; i < getResponse.getInvalid_events().size(); i++) {
            body.getEventSubscriptions().remove((int) getResponse.getInvalid_events().get(i));
        }
        List<Boolean> canServeSubscription = checkCanServeSubscriptions(getResponse.getNo_valid_events(), body,
                getResponse.getEventIndexToNotifMethodMap(), getResponse.getEventIndexToRepPeriodMap(),
                dataCollectionPublisher, dummyDataProducerPublisher, kafkaProducer, objectMapper, metricsService, metricsCacheService,
                globalResponse.getImmRep(), 0L);

        // check the amount of subscriptions that need to be notified
        for (int i = 0; i < canServeSubscription.size(); i++) {
            if (canServeSubscription.get(i)) {
                count_of_notif++;
                periods_to_serve.add(getResponse.getEventIndexToRepPeriodMap().get(i));
            }
        }
        // if no subscriptions need notifying mute the subscription
        if (count_of_notif == 0) {
            if (body.getEvtReq() == null) {
                body.setEvtReq(new ReportingInformation());
            }
            body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE));
        }
        // save sub to db and get back the created id
        NnwdafEventsSubscriptionTable result;
        try {
            result = subscriptionService.create(body);
        } catch (Exception e) {
            logger.error("Couldn't save sub to db", e);
            throw new Exception("Couldn't save sub to db", e);
        }
        if (result == null) {
            logger.error("Couldn't save sub to db (service returned null)");
            throw new Exception("Couldn't save sub to db (service returned null)");
        }

        body.setId(result.getId());
        //notify about new saved subscription
        if (count_of_notif > 0 && !globalResponse.getMuted()) {
            notifyPublisher.publishNotification("controller requested notification for client with URI: " + body.getNotificationURI(), body.getId());
        }

        System.out.println("id=" + body.getId());
        System.out.println(body);
        //set the location header to the subscription uri
        responseHeaders.set("Location", subsUri + "/" + body.getId());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(body);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
                                                                                  @Valid NnwdafEventsSubscription body) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequestAdapter request = attributes != null ? new HttpServletRequestAdapter(attributes.getRequest()) : null;
        HttpHeaders responseHeaders = new HttpHeaders();

        if (subscriptionId == null) {
            throw new MissingPathVariableException("subscriptionId",
                    new MethodParameter(this.getClass().getDeclaredMethod("updateNWDAFEventsSubscription", String.class, NnwdafEventsSubscription.class), 0));
        }
        if (ParserUtil.safeParseLong(subscriptionId) == null) {
            throw new ConversionNotSupportedException(new PropertyChangeEvent(SubscriptionsController.class, "subscriptionId", subscriptionId, subscriptionId), Long.class, null);
        }
        String subsUri = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url") + "/nwdaf-eventsubscription/v1/subscriptions" + "/" + subscriptionId;
        int count_of_notif = 0;
        List<Integer> periods_to_serve = new ArrayList<>();

        validateRequest(body, attributes, request);

        body.setId(ParserUtil.safeParseLong(subscriptionId));
        List<Integer> negotiatedFeaturesList = negotiateSupportedFeatures(body);
        logger.info("negotiatedFeaturesList:" + negotiatedFeaturesList.toString());

        GetGlobalNotifMethodAndRepPeriodResponse globalResponse = getGlobalNotifMethodAndRepPeriod(body.getEvtReq());
        GetNotifMethodAndRepPeriodsResponse getResponse = getNotifMethodAndRepPeriods(
                body.getEventSubscriptions(),
                globalResponse.getNotificationMethod(),
                globalResponse.getRepetionPeriod());
        for (int i = 0; i < getResponse.getInvalid_events().size(); i++) {
            body.getEventSubscriptions().remove((int) getResponse.getInvalid_events().get(i));
        }
        List<Boolean> canServeSubscription = checkCanServeSubscriptions(getResponse.getNo_valid_events(), body,
                getResponse.getEventIndexToNotifMethodMap(), getResponse.getEventIndexToRepPeriodMap(),
                dataCollectionPublisher, dummyDataProducerPublisher, kafkaProducer, objectMapper, metricsService, metricsCacheService,
                globalResponse.getImmRep(), 0L);
        // check the amount of subscriptions that need to be notified
        for (int i = 0; i < canServeSubscription.size(); i++) {
            if (canServeSubscription.get(i)) {
                count_of_notif++;
                periods_to_serve.add(getResponse.getEventIndexToRepPeriodMap().get(i));
            }
        }
        //if no subscriptions need notifying mute the subscription
        if (count_of_notif == 0) {
            if (body.getEvtReq() == null) {
                body.setEvtReq(new ReportingInformation());
            }
            body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE));
        }

        subscriptionService.update(ParserUtil.safeParseLong(subscriptionId), body);
        //notify about updated subscription
        if (count_of_notif > 0 && !globalResponse.getMuted()) {
            notifyPublisher.publishNotification("controller requested notification for client with URI: " + body.getNotificationURI(), body.getId());
        }
        responseHeaders.set("Location", subsUri);
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(body);
    }

    @Override
    public ResponseEntity<Void> deleteNWDAFEventsSubscription(String subscriptionId) {
        try {
            subscriptionService.delete(ParserUtil.safeParseLong(subscriptionId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
