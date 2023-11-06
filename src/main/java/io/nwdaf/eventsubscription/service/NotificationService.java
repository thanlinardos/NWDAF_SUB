package io.nwdaf.eventsubscription.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.repository.eventnotification.NotificationRepository;
import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;

@Service
public class NotificationService {
	
	private final NotificationRepository repository;
	
	// @Autowired
	public NotificationService(NotificationRepository repository) {
		this.repository = repository;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Async
	public void create(NnwdafEventsSubscriptionNotification body) {
		NnwdafNotificationTable body_table = new NnwdafNotificationTable();
		body_table.setNotif(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		repository.save(body_table);
	}

	public boolean truncate(){
		try{
			repository.truncate();
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
}
