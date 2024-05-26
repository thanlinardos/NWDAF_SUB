package io.nwdaf.eventsubscription.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import io.nwdaf.eventsubscription.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.config.WebClientConfig;
import io.nwdaf.eventsubscription.controller.http.HttpServletRequestAdapter;
import io.nwdaf.eventsubscription.controller.http.NwdafFailureException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import io.nwdaf.eventsubscription.api.SubscriptionsApi;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.notify.responsetypes.GetGlobalNotifMethodAndRepPeriodResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetNotifMethodAndRepPeriodsResponse;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.service.MetricsCacheService;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.SubscriptionsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import static io.nwdaf.eventsubscription.controller.http.ValidateSubscriptionRequest.validateRequest;
import static io.nwdaf.eventsubscription.kafka.KafkaNotifierLoadBalanceConsumer.updateSubscriptionContainedAoIs;
import static io.nwdaf.eventsubscription.notify.NotificationUtil.*;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.safeParseLong;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.notifier", havingValue = "true")
@RestController
@CrossOrigin
public class SubscriptionsController implements SubscriptionsApi {

    public static final String SUBSCRIPTIONS_URI = "/nwdaf-eventsubscription/v1/subscriptions";

    @Value("${nnwdaf-eventsubscription.openapi.dev-url}")
    private String serverHost;

    private final NotifyPublisher notifyPublisher;
    private final SubscriptionsService subscriptionService;
    private final MetricsService metricsService;
    private final KafkaProducer kafkaProducer;
    private final MetricsCacheService metricsCacheService;
    private final Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);
    private final NwdafSubProperties nwdafSubProperties;
    private final WebClient webClient;

    public SubscriptionsController(NotifyPublisher notifyPublisher,
                                   SubscriptionsService subscriptionService,
                                   MetricsService metricsService,
                                   KafkaProducer kafkaProducer,
                                   MetricsCacheService metricsCacheService,
                                   NwdafSubProperties nwdafSubProperties) {
        this.notifyPublisher = notifyPublisher;
        this.subscriptionService = subscriptionService;
        this.metricsService = metricsService;
        this.kafkaProducer = kafkaProducer;
        this.metricsCacheService = metricsCacheService;
        this.nwdafSubProperties = nwdafSubProperties;
        this.webClient = WebClient
                .builder()
                .clientConnector(Objects.requireNonNull(WebClientConfig.createWebClientFactory(false)))
                .build();
    }


    @SneakyThrows
    @Override
    public ResponseEntity<NnwdafEventsSubscription> createNWDAFEventsSubscription(@Valid NnwdafEventsSubscription body) {

        HttpHeaders responseHeaders = new HttpHeaders();
        List<Integer> periodsToServe = new ArrayList<>();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequestAdapter request = null;
        if (attributes != null) request = new HttpServletRequestAdapter(attributes.getRequest());

        validateRequest(body, attributes, request);

        GetGlobalNotifMethodAndRepPeriodResponse globalResponse = handleSubscription(body, periodsToServe);
        // save sub to db and get back the created id
        NnwdafEventsSubscriptionTable result;
        result = subscriptionService.create(body);
        if (result == null) {
            logger.error("Couldn't save sub to db (service returned null)");
            throw new Exception("Couldn't save sub to db (service returned null)");
        }

        body.setId(result.getId());
        // notify about new saved subscription
        if (!periodsToServe.isEmpty() && !globalResponse.getMuted()) {
            notifyPublisher.publishNotification("controller requested notification for client with URI: " +
                    body.getNotificationURI(), body.getId());
        }

        System.out.println("created Sub with id=" + body.getId());
        // set the location header to the subscription uri
        String subscriptionsUrl = serverHost + SUBSCRIPTIONS_URI + "/" + body.getId();
        responseHeaders.set("Location", subscriptionsUrl);
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(body);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<NnwdafEventsSubscription> updateNWDAFEventsSubscription(String subscriptionId,
                                                                                  @Valid NnwdafEventsSubscription body) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequestAdapter request = attributes != null ?
                new HttpServletRequestAdapter(attributes.getRequest()) : null;
        HttpHeaders responseHeaders = new HttpHeaders();

        if (subscriptionId == null) {
            throw new MissingPathVariableException("subscriptionId",
                    new MethodParameter(this.getClass()
                            .getDeclaredMethod("updateNWDAFEventsSubscription",
                                    String.class,
                                    NnwdafEventsSubscription.class), 0));
        }
        if (safeParseLong(subscriptionId) == null) {
            throw new ConversionNotSupportedException(
                    new PropertyChangeEvent(
                            SubscriptionsController.class,
                            "subscriptionId",
                            subscriptionId,
                            subscriptionId), Long.class, null);
        }

        List<Integer> periodsToServe = new ArrayList<>();

        validateRequest(body, attributes, request);

        body.setId(safeParseLong(subscriptionId));
        GetGlobalNotifMethodAndRepPeriodResponse globalResponse = handleSubscription(body, periodsToServe);

        subscriptionService.update(safeParseLong(subscriptionId), body);
        // notify about updated subscription
        if (!periodsToServe.isEmpty() && !globalResponse.getMuted()) {
            notifyPublisher.publishNotification("controller requested notification for client with URI: " +
                    body.getNotificationURI(), body.getId());
        }

        String subscriptionUrl = serverHost + SUBSCRIPTIONS_URI + "/" + subscriptionId;
        responseHeaders.set("Location", subscriptionUrl);
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(body);
    }

    private GetGlobalNotifMethodAndRepPeriodResponse handleSubscription(
            NnwdafEventsSubscription body,
            List<Integer> periodsToServe) throws NwdafFailureException {
        List<Integer> negotiatedFeaturesList = negotiateSupportedFeatures(body);
        logger.info("negotiatedFeaturesList:{}", negotiatedFeaturesList);

        GetGlobalNotifMethodAndRepPeriodResponse globalResponse = getGlobalNotifMethodAndRepPeriod(body.getEvtReq());
        GetNotifMethodAndRepPeriodsResponse getResponse = getNotifMethodAndRepPeriods(
                body.getEventSubscriptions(),
                globalResponse.getNotificationMethod(),
                globalResponse.getRepetionPeriod());
        for (int i = 0; i < getResponse.getInvalidEvents().size(); i++) {
            body.getEventSubscriptions().remove((int) getResponse.getInvalidEvents().get(i));
        }
        updateSubscriptionContainedAoIs(body);
        List<Boolean> canServeSubscription = checkCanServeSubscriptions(getResponse,
                body,
                kafkaProducer, metricsService, metricsCacheService,
                globalResponse.getImmRep(),
                nwdafSubProperties.consume(),
                webClient,
                nwdafSubProperties.consumerUrl()
                );
        // check the amount of subscriptions that need to be notified
        for (int i = 0; i < canServeSubscription.size(); i++) {
            if (canServeSubscription.get(i)) {
                periodsToServe.add(getResponse.getEventIndexToRepPeriodMap().get(i));
            }
        }
        // if no subscriptions need notifying mute the subscription
        if (periodsToServe.isEmpty()) {
            if (body.getEvtReq() == null) {
                body.setEvtReq(new ReportingInformation());
            }
            body.getEvtReq().notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.DEACTIVATE));
        }
        return globalResponse;
    }

    @Override
    public ResponseEntity<Void> deleteNWDAFEventsSubscription(String subscriptionId) {
        try {
            subscriptionService.delete(safeParseLong(subscriptionId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SneakyThrows
    public ResponseEntity<List<NnwdafEventsSubscription>> getNWDAFEventsSubscriptions(Optional<String> notificationUri) {
        List<NnwdafEventsSubscription> res;
        if (notificationUri.isPresent()) {
            res = subscriptionService.findAllByNotifURI(notificationUri.get());
        } else {
            res = subscriptionService.findAll();
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @SneakyThrows
    public ResponseEntity<NnwdafEventsSubscription> getNWDAFEventsSubscription(String subscriptionId) {
        NnwdafEventsSubscription res;
        try {
            res = subscriptionService.findById(safeParseLong(subscriptionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
