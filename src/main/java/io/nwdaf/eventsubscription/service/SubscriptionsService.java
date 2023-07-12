package io.nwdaf.eventsubscription.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.ClientApplication;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;

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
		List<NnwdafEventsSubscriptionTable> tables = repository.findAll();
		List<NnwdafEventsSubscription> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			res.add(objectMapper.readValue((new JSONObject(tables.get(i).getSub())).toString(),NnwdafEventsSubscription.class));
		}
		return res;
	}
	public List<NnwdafEventsSubscription> findAllByNotifURI(String clientURI) throws JsonMappingException, JsonProcessingException {
		final String filter = "'{\"notificationURI\":\""+clientURI+"\"}'";
		List<NnwdafEventsSubscriptionTable> tables = repository.findAllByNotifURI(filter);
		List<NnwdafEventsSubscription> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			res.add(objectMapper.readValue((new JSONObject(tables.get(i).getSub())).toString(),NnwdafEventsSubscription.class));
		}
		return res;
	}
}
