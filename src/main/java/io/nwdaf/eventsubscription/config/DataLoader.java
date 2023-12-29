package io.nwdaf.eventsubscription.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.repository.eventsubscription.SubscriptionRepository;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

//@Profile("!dev")
@ConditionalOnProperty(name="nnwdaf-eventsubscription.init")
@Component
public class DataLoader implements CommandLineRunner{
	
	private final SubscriptionRepository repository;
	final ObjectMapper objectMapper;
	
	public DataLoader(SubscriptionRepository repository,ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if(repository.count()==0) {
		try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/content.json")){
			repository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<NnwdafEventsSubscriptionTable>>() {}));
		}
		}
		
	}

}
