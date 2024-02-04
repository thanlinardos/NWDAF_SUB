package io.nwdaf.eventsubscription.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.repository.eventsubscription.CustomEventSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

@Service
public class SubscriptionsService {

    private final SubscriptionRepository repository;
    private final CustomEventSubscriptionRepository customRepository;

    // @Autowired
    public SubscriptionsService(SubscriptionRepository repository, CustomEventSubscriptionRepository customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Autowired
    private ObjectMapper objectMapper;


    public NnwdafEventsSubscriptionTable create(NnwdafEventsSubscription body) {
        NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
        body_table.setSub(objectMapper.convertValue(body, new TypeReference<>() {
        }));
        return repository.save(body_table);
    }

    public List<NnwdafEventsSubscriptionTable> create(List<NnwdafEventsSubscription> bodies) {
        List<NnwdafEventsSubscriptionTable> body_tables = new ArrayList<>();
        for (NnwdafEventsSubscription body : bodies) {
            NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
            body_table.setSub(objectMapper.convertValue(body, new TypeReference<>() {
            }));
            body_tables.add(body_table);
        }
        return repository.saveAll(body_tables);
    }

    public List<NnwdafEventsSubscription> findAll() throws JsonProcessingException {
//        BenchmarkUtil benchmarkUtil = new BenchmarkUtil();
//        benchmarkUtil.start();
        List<NnwdafEventsSubscriptionTable> tables = repository.findAll();
//        NwdafSubApplication.getLogger().info("actual sub query time: " + benchmarkUtil.end().toMillisStr());
        List<NnwdafEventsSubscription> res = new ArrayList<>();
        for (NnwdafEventsSubscriptionTable table : tables) {
            NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(table.getSub())).toString(), NnwdafEventsSubscription.class);
            sub.setId(table.getId());
            res.add(sub);
        }

        return res;
    }

    public List<NnwdafEventsSubscription> findAllByNotifURI(String clientURI) throws JsonProcessingException {
        final String filter = "'{\"notificationURI\":\"" + clientURI + "\"}'";
        List<NnwdafEventsSubscriptionTable> tables = customRepository.findAllInLastFilter(filter, false);
        List<NnwdafEventsSubscription> res = new ArrayList<>();
        for (NnwdafEventsSubscriptionTable table : tables) {
            NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(table.getSub())).toString(), NnwdafEventsSubscription.class);
            sub.setId(table.getId());
            res.add(sub);
        }
        return res;
    }

    public List<NnwdafEventsSubscription> findAllByActive(NotificationFlag.NotificationFlagEnum notifFlag) throws JsonProcessingException {
        final String filter = "'{\"evtReq\": {\"notifFlag\": {\"notifFlag\": \"" + notifFlag + "\"}}}'";
        List<NnwdafEventsSubscriptionTable> tables = customRepository.findAllInLastFilter(filter, true);
        List<NnwdafEventsSubscription> res = new ArrayList<>();
        for (NnwdafEventsSubscriptionTable table : tables) {
            NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(table.getSub())).toString(), NnwdafEventsSubscription.class);
            sub.setId(table.getId());
            res.add(sub);
        }
        return res;
    }

    public NnwdafEventsSubscriptionTable update(Long id, NnwdafEventsSubscription body) {
        NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
        body_table.setSub(objectMapper.convertValue(body, new TypeReference<>() {
        }));
        body_table.setId(id);
        return repository.save(body_table);
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
        NnwdafEventsSubscription sub = null;
        if (table.isPresent()) {
            try {
                sub = objectMapper.readValue((new JSONObject(table.get().getSub())).toString(), NnwdafEventsSubscription.class);
                sub.setId(table.get().getId());
            } catch (JsonProcessingException e) {
                NwdafSubApplication.getLogger().error("Error parsing subscription", e);
            }
        }
        return sub;
    }
}
