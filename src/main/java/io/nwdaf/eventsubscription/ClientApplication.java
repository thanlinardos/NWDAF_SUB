package io.nwdaf.eventsubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.TargetUeInformation;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.requestbuilders.CreateSubscriptionRequestBuilder;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;

@SpringBootApplication
public class ClientApplication {
	
	@Autowired
	private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);
	
	private ObjectMapper objectMapper;
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
		
		
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws JsonProcessingException{
		String apiRoot = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url");
		CreateSubscriptionRequestBuilder rbuilder = new CreateSubscriptionRequestBuilder();
		NnwdafEventsSubscription bodyObject = rbuilder.SubscriptionRequestBuilder();
		return args -> {
			HttpEntity<NnwdafEventsSubscription> res = restTemplate.postForObject(
					apiRoot+"/nwdaf-eventsubscription/v1/subscriptions",new HttpEntity<NnwdafEventsSubscription>(bodyObject), HttpEntity.class);
			System.out.println("Location:"+res.getHeaders().getFirst("Location"));
			log.info(res.toString());
		};
	}
	
	
}
