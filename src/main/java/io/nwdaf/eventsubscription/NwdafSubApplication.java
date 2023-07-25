package io.nwdaf.eventsubscription;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;


import io.nwdaf.eventsubscription.api.config.NwdafSubProperties;
import io.nwdaf.eventsubscription.notify.DataCollectionPublisher;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication
@EnableAsync
@EntityScan({"io.nwdaf.eventsubscription.repository"})
public class NwdafSubApplication {
	
	// @Autowired
	// private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(NwdafSubApplication.class);
	
	@Autowired
	private NotifyPublisher notifyPublisher;
	
	@Autowired
	private DataCollectionPublisher dataCollectionPublisher;
	
//	@Autowired
//	MetricsService metricsService;
	
	public static void main(String[] args) {
		SpringApplication.run(NwdafSubApplication.class, args);
		
		
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run() throws JsonProcessingException{
		
		// String clientURL = env.getProperty("nnwdaf-eventsubscription.client.dev_url");
		// String prometheusURL = env.getProperty("nnwdaf-eventsubscription.prometheus_url");
		
		return args -> {
//			NfLoadLevelInformation nfload = new NfLoadLevelInformation().time(Instant.now())
//					.nfCpuUsage(100).nfMemoryUsage(50)
//					.nfInstanceId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
//					.nfType(new NFType().nfType(NFTypeEnum.AMF))
//					.nfStatus(new NfStatus().statusRegistered(1).statusUnregistered(2).statusUndiscoverable(3));
//			metricsService.create(nfload);
//			
//			UeMobility uemob = new UeMobility().duration(12).ts(OffsetDateTime.now())
//					.addLocInfosItem(new LocationInfo()
//							.ratio(1)
//							.loc(new UserLocation()
//									.eutraLocation(new EutraLocation()
//											.ageOfLocationInformation(0)
//											.ecgi(new Ecgi()
//													.eutraCellId("1F3A41B")
//													.nid("FFFFFFFFFFF"))
//													.tai(new Tai()
//															.tac("FFFF")))));
//			metricsService.create(uemob);
			String params="";
			Long subId = 0l;
			
			dataCollectionPublisher.publishDataCollection(params);
			notifyPublisher.publishNotification(subId);			
		};
	}
	
	public static Logger getLogger() {
		return NwdafSubApplication.log;
	}
	
}
