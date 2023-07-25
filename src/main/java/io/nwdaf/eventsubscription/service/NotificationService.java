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
	
	@Autowired
	public NotificationService(NotificationRepository repository) {
		this.repository = repository;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Async
	public void create(NnwdafEventsSubscriptionNotification body) {
		NnwdafNotificationTable body_table = new NnwdafNotificationTable();
		body_table.setNotif(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
//		NwdafSubApplication.getLogger().info(body_table.getNotif().toString());
		repository.save(body_table);
	}
}
