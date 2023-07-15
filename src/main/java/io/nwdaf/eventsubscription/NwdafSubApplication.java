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
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.api.config.NwdafSubProperties;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication
@EnableAsync
@EntityScan({"io.nwdaf.eventsubscription.repository"})
public class NwdafSubApplication {
	
	@Autowired
	private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(NwdafSubApplication.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private RestTemplate template = null;
	
	public static void main(String[] args) {
		SpringApplication.run(NwdafSubApplication.class, args);
		
		
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run() throws JsonProcessingException{
		
//		String apiRoot = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url");
//		CreateSubscriptionRequestBuilder rbuilder = new CreateSubscriptionRequestBuilder();
//		NnwdafEventsSubscription bodyObject = rbuilder.InitSubscriptionRequest(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
//		return args -> {
//			HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(bodyObject);
//			ResponseEntity<NnwdafEventsSubscription> res = restTemplate.postForEntity(
//					apiRoot+"/nwdaf-eventsubscription/v1/subscriptions",req, NnwdafEventsSubscription.class);
//			System.out.println("Location:"+res.getHeaders().getFirst("Location"));
//			log.info(res.getBody().toString());
//		};
		
		String clientURL = "http://localhost:8082/client";
		String prometheusURL = "http://localhost:9090/api/v1/query";
		Long id = 202l;
		
//			log.info(res.getBody().toString());
		return args -> {
//			for(int i=0;i<100;i++) {
//				NnwdafEventsSubscriptionNotification notification = new NnwdafEventsSubscriptionNotification();
//				OffsetDateTime now = OffsetDateTime.now();
//				String nowString = now.toString();
//				PrometheusRequestModel reqModel = new PrometheusRequestModel();
//				reqModel.setQuery("container_cpu_usage_seconds_total");
//				reqModel.setTime(now);
//				HttpHeaders headers = new HttpHeaders();
//				headers.set("Content-Type", "application/x-www-form-urlencoded");
//				headers.set("Accept-Encoding", "gzip, deflate, br");
//				headers.set("Connection", "keep-alive");
//				headers.set("Accept", "*/*");
//				String encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes());
//				log.info(encoding);
//				headers.set(HttpHeaders.AUTHORIZATION, "Basic "+encoding);
//				String json = objectMapper.writeValueAsString(reqModel);
//				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//				map.add("query","container_cpu_usage_seconds_total");
//				map.add("time",nowString);
//				HttpEntity<MultiValueMap<String, String>> reqToPrometheus = new HttpEntity<>(map,headers);
//				
//				HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//				httpRequestFactory.setConnectTimeout(1000);
//				httpRequestFactory.setReadTimeout(2000);
//				HttpClient httpClient = HttpClientBuilder.create().build();
//				httpRequestFactory.setHttpClient(httpClient);
//				template = new RestTemplate(httpRequestFactory);
//				
//				String rtVal = template.postForObject(prometheusURL, reqToPrometheus, String.class);
//				DefaultQueryResult<VectorData> result = ConvertUtil.convertQueryResultString(rtVal);
//
//				String resTime=null;
//				Double value=null;
//				for(VectorData vectorData : result.getResult()) {
//					if(vectorData.getMetric().get("name")!=null) {
//						log.info(String.format("%s", vectorData.getMetric().get("name")));
//						log.info(String.format("%s %10.2f ",
//									OffsetDateTime.ofInstant(Instant.ofEpochMilli(Math.round(vectorData.getDataValue().getTimestamp()*1000)), ZoneId.of("UTC")),
//									vectorData.getDataValue().getValue()
//									));
//						if(vectorData.getMetric().get("name").equals("pg_container") && vectorData.getMetric().get("cpu").equals("cpu00")) {
//							resTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(Math.round(vectorData.getDataValue().getTimestamp()*1000)), ZoneId.of("UTC")).toString();
//							value = vectorData.getDataValue().getValue();
//						}
//					}
//					
//				}
//				
//				Integer container_cpu_usage_seconds_total = (int) Math.round(value);
//				log.info(resTime+": "+value);
//				notification.setSubscriptionId(id.toString());
//				
//				EventNotification evnot = new EventNotification().event(new NwdafEvent().event(NwdafEventEnum.NF_LOAD)).timeStampGen(OffsetDateTime.parse(resTime));
//				
//				evnot.addNfLoadLevelInfosItem(new NfLoadLevelInformation().nfCpuUsage(container_cpu_usage_seconds_total).nfMemoryUsage(30).nfStorageUsage(100));
//				notification.addEventNotificationsItem(evnot);
//			
//				HttpEntity<NnwdafEventsSubscriptionNotification> req = new HttpEntity<>(notification);
//				ResponseEntity<NnwdafEventsSubscriptionNotification> res = template.postForEntity(clientURL+"/notify",req, NnwdafEventsSubscriptionNotification.class);
//				System.out.println("Location:"+res.getHeaders().getFirst("Location"));
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		};
	}
	
	public static Logger getLogger() {
		return NwdafSubApplication.log;
	}
}
