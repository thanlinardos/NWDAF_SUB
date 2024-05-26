package io.nwdaf.eventsubscription.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.repository.eventsubscription.CustomEventSubscriptionRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

import static io.nwdaf.eventsubscription.service.StartUpService.getAssignedSubscriptions;
import static io.nwdaf.eventsubscription.service.ServiceUtils.parseTables;

@Service
public class SubscriptionsService {

    private final SubscriptionRepository repository;
    private final CustomEventSubscriptionRepository customRepository;
    private final ObjectMapper objectMapper;

    public SubscriptionsService(SubscriptionRepository repository,
                                CustomEventSubscriptionRepository customRepository,
                                ObjectMapper objectMapper) {
        this.repository = repository;
        this.customRepository = customRepository;
        this.objectMapper = objectMapper;
    }


    public NnwdafEventsSubscriptionTable create(NnwdafEventsSubscription body) {
        NnwdafEventsSubscriptionTable table = new NnwdafEventsSubscriptionTable();
        table.setData(objectMapper.convertValue(body, new TypeReference<>() {
        }));
        return repository.save(table);
    }

    public List<NnwdafEventsSubscriptionTable> create(List<NnwdafEventsSubscription> subscriptions) {
        List<NnwdafEventsSubscriptionTable> tables = new ArrayList<>();
        for (NnwdafEventsSubscription subscription : subscriptions) {
            NnwdafEventsSubscriptionTable table = new NnwdafEventsSubscriptionTable();
            table.setData(objectMapper.convertValue(subscription, new TypeReference<>() {
            }));
            tables.add(table);
        }
        return repository.saveAll(tables);
    }

    public List<NnwdafEventsSubscription> findAll() throws JsonProcessingException {
        List<NnwdafEventsSubscriptionTable> tables = repository.findAll();
        return parseTables(tables, NnwdafEventsSubscription.class, objectMapper);
    }

    public List<NnwdafEventsSubscription> findAllByNotifURI(String clientURI) throws JsonProcessingException {
        final String filter = "'{\"notificationURI\":\"" + clientURI + "\"}'";
        List<NnwdafEventsSubscriptionTable> tables = customRepository.findAllInLastFilter(filter, false);
        return parseTables(tables, NnwdafEventsSubscription.class, objectMapper);
    }

    public List<NnwdafEventsSubscription> findAllByActive(NotificationFlag.NotificationFlagEnum notifFlag)
            throws JsonProcessingException {
        final String filter = "'{\"evtReq\": {\"notifFlag\": {\"notifFlag\": \"" + notifFlag + "\"}}}'";
        List<NnwdafEventsSubscriptionTable> tables = customRepository.findAllInLastFilter(filter, true);
        return parseTables(tables, NnwdafEventsSubscription.class, objectMapper);
    }

    public NnwdafEventsSubscriptionTable update(Long id, NnwdafEventsSubscription body) {
        NnwdafEventsSubscriptionTable table = new NnwdafEventsSubscriptionTable();
        table.setData(objectMapper.convertValue(body, new TypeReference<>() {
        }));
        table.setId(id);
        return repository.save(table);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean truncate() {
        try {
            repository.truncate();
            return true;
        } catch (Exception e) {
            NwdafSubApplication.getLogger().error("Error truncating subscriptions table", e);
            return false;
        }
    }

    public NnwdafEventsSubscription findById(Long subscriptionId) {
        Optional<NnwdafEventsSubscriptionTable> table = repository.findById(subscriptionId);
        NnwdafEventsSubscription subscription = null;
        if (table.isPresent()) {
            try {
                subscription = parseTables(List.of(table.get()), NnwdafEventsSubscription.class, objectMapper).getFirst();
                subscription.setId(table.get().getId());
            } catch (JsonProcessingException e) {
                NwdafSubApplication.getLogger().error("Error parsing subscription", e);
            }
        }
        return subscription;
    }

    public List<Long> findAllIdsByActive(NotificationFlag.NotificationFlagEnum notifFlag) throws Exception {
        final String filter = "'{\"evtReq\": {\"notifFlag\": {\"notifFlag\": \"" + notifFlag + "\"}}}'";
        return customRepository.findAllIdsInLastFilter(filter, true);
    }

    public List<NnwdafEventsSubscription> findAllAssignedSubscriptions() throws Exception {
        List<NnwdafEventsSubscriptionTable> tables = repository.findAllById(getAssignedSubscriptions());
        return parseTables(tables, NnwdafEventsSubscription.class, objectMapper);
    }
}
