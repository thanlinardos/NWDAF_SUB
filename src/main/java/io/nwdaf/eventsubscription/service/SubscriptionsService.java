package io.nwdaf.eventsubscription.service;


import java.util.ArrayList;
import java.util.List;

import io.nwdaf.eventsubscription.NwdafSubApplication;
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
	
	// @Autowired
	public SubscriptionsService(SubscriptionRepository repository) {
		this.repository = repository;
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
		for(NnwdafEventsSubscription body: bodies) {
			NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
			body_table.setSub(objectMapper.convertValue(body, new TypeReference<>() {
			}));
			body_tables.add(body_table);
		}
		return repository.saveAll(body_tables);
	}
	
	public List<NnwdafEventsSubscription> findAll() throws JsonProcessingException {
//		long st_sub = System.nanoTime();
		List<NnwdafEventsSubscriptionTable> tables = repository.findAll();
//		NwdafSubApplication.getLogger().info("actual sub query time: "+(System.nanoTime()-st_sub)/1000000l);
		List<NnwdafEventsSubscription> res = new ArrayList<>();
        for (NnwdafEventsSubscriptionTable table : tables) {
            NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(table.getSub())).toString(), NnwdafEventsSubscription.class);
            sub.setId(table.getId());
            res.add(sub);
        }
		
		return res;
	}

	public List<NnwdafEventsSubscription> findAllByNotifURI(String clientURI) throws JsonProcessingException {
		final String filter = "'{\"notificationURI\":\""+clientURI+"\"}'";
		List<NnwdafEventsSubscriptionTable> tables = repository.findAllByNotifURI(filter);
		List<NnwdafEventsSubscription> res = new ArrayList<>();
        for (NnwdafEventsSubscriptionTable table : tables) {
            NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(table.getSub())).toString(), NnwdafEventsSubscription.class);
            sub.setId(table.getId());
            res.add(sub);
        }
		return res;
	}

    public NnwdafEventsSubscriptionTable update(Long id,NnwdafEventsSubscription body) {
		NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
		body_table.setSub(objectMapper.convertValue(body, new TypeReference<>() {
        }));
		body_table.setId(id);
		return repository.save(body_table);
    }

	public NnwdafEventsSubscriptionTable delete(Long id) {
		return repository.delete(id);
    }

	public boolean truncate(){
		try{
			repository.truncate();
			return true;
		} catch(Exception e){
			NwdafSubApplication.getLogger().error("Error truncating subscriptions table", e);
			return false;
		}
	}
}
