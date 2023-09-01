package io.nwdaf.eventsubscription.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

@Service
public class SubscriptionsService {
	
	private final SubscriptionRepository repository;
	
	@Autowired
	public SubscriptionsService(SubscriptionRepository repository) {
		this.repository = repository;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	

	public NnwdafEventsSubscriptionTable create(NnwdafEventsSubscription body) {
		NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
		body_table.setSub(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		return repository.save(body_table);
	}
	
	public List<NnwdafEventsSubscription> findAll() throws JsonMappingException, JsonProcessingException {
//		long st_sub = System.nanoTime();
		List<NnwdafEventsSubscriptionTable> tables = repository.findAll();
//		NwdafSubApplication.getLogger().info("actual sub query time: "+(System.nanoTime()-st_sub)/1000000l);
		List<NnwdafEventsSubscription> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(tables.get(i).getSub())).toString(),NnwdafEventsSubscription.class);
			sub.setId(tables.get(i).getId());
			res.add(sub);
		}
		
		return res;
	}
	public List<NnwdafEventsSubscription> findAllByNotifURI(String clientURI) throws JsonMappingException, JsonProcessingException {
		final String filter = "'{\"notificationURI\":\""+clientURI+"\"}'";
		List<NnwdafEventsSubscriptionTable> tables = repository.findAllByNotifURI(filter);
		List<NnwdafEventsSubscription> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			NnwdafEventsSubscription sub = objectMapper.readValue((new JSONObject(tables.get(i).getSub())).toString(),NnwdafEventsSubscription.class);
			sub.setId(tables.get(i).getId());
			res.add(sub);
		}
		return res;
	}

    public NnwdafEventsSubscriptionTable update(Long id,NnwdafEventsSubscription body) {
		NnwdafEventsSubscriptionTable body_table = new NnwdafEventsSubscriptionTable();
		body_table.setSub(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		body_table.setId(id);
		return repository.save(body_table);
    }
	public NnwdafEventsSubscriptionTable delete(Long id) {
		return repository.delete(id);
    }
}
