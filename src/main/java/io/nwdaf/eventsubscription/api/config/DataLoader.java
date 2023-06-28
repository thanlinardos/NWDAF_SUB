package io.nwdaf.eventsubscription.api.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.SubscriptionRepository;

//@Profile("!dev")
@ConditionalOnProperty(name="nnwdaf-eventsubscription.init")
@Component
public class DataLoader implements CommandLineRunner{
	
	private final SubscriptionRepository repository;
	private final ObjectMapper objectMapper;
	
	public DataLoader(SubscriptionRepository repository,ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if(repository.count()==0) {
		try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/content.json")){
			repository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<NnwdafEventsSubscription>>() {}));
		}
		}
		
	}

}
