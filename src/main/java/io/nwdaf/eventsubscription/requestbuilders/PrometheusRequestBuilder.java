package io.nwdaf.eventsubscription.requestbuilders;

import org.apache.hc.client5.http.classic.HttpClient;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.TimeZone;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bdwise.prometheus.client.converter.ConvertUtil;
import com.bdwise.prometheus.client.converter.query.DefaultQueryResult;
import com.bdwise.prometheus.client.converter.query.VectorData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.EventNotification;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;

public class PrometheusRequestBuilder {
	
	private RestTemplate template;
	
	private RestTemplate setupTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		HttpClient httpClient = HttpClientBuilder.create().build();
		httpRequestFactory.setHttpClient(httpClient);
		return new RestTemplate(httpRequestFactory);
	}
	private HttpEntity<MultiValueMap<String, String>> setupRequest(OffsetDateTime now,String query) {
		String nowString = now.toString();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		headers.set("Accept-Encoding", "gzip, deflate, br");
		headers.set("Connection", "keep-alive");
		headers.set("Accept", "*/*");
		String encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes());
		headers.set(HttpHeaders.AUTHORIZATION, "Basic "+encoding);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("query",query);
		map.add("time",nowString);
		HttpEntity<MultiValueMap<String, String>> reqToPrometheus = new HttpEntity<>(map,headers);
		return reqToPrometheus;
	}
	
	public NnwdafEventsSubscriptionNotification execute(EventSubscription eventSub,Long id,NnwdafEventsSubscriptionNotification notification,String prometheus_url,String containerNamesEnv) throws JsonProcessingException {
		Logger log = NwdafSubApplication.getLogger();
		OffsetDateTime now = OffsetDateTime.now();
		//setup the connection rest template
		template = setupTemplate();
		
		NwdafEventEnum eType = eventSub.getEvent().getEvent();
		NotificationBuilder notifBuilder = new NotificationBuilder();
		
		switch(eType) {
		case NF_LOAD:
			String queryString = "container_cpu_usage_seconds_total{name=~\"";
			List<String> containerNames = new ArrayList<>(Arrays.asList(containerNamesEnv.split(",")));
			for(int i=0;i<containerNames.size();i++) {
				queryString+=containerNames.get(i);
				if(i!=containerNames.size()-1) {
					queryString+="|";
				}
			}
			queryString+="\"}";
//			System.out.println("query: "+queryString);
			HttpEntity<MultiValueMap<String, String>> reqToPrometheus = setupRequest(now,queryString);
//			long start = System.nanoTime();
			String rtVal = template.postForObject(prometheus_url, reqToPrometheus, String.class);
//			long diff = (System.nanoTime()-start) / 1000000l;
//        	System.out.println("prometheus actual post req delay: "+diff+"ms");
			DefaultQueryResult<VectorData> result = ConvertUtil.convertQueryResultString(rtVal);
	
			String resTime=null;
			Double value=null;
			List<Double> means = new ArrayList<>();
			List<Integer> counters = new ArrayList<>();
			List<List<Integer>> data = new ArrayList<>();
			List<List<String>> dataOptionals = new ArrayList<>();
			List<Double> maxs = new ArrayList<>();
			for(int i=0;i<containerNames.size();i++) {
				data.add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null)));
				dataOptionals.add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null)));
				means.add(0.0);
				counters.add(0);
				maxs.add(0.0);
			}
			for(VectorData vectorData : result.getResult()) {
				if(vectorData.getMetric().get("name")!=null) {
					if(containerNames.contains(vectorData.getMetric().get("name"))) {
						Integer index = containerNames.indexOf(vectorData.getMetric().get("name"));
						resTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(Math.round(vectorData.getDataValue().getTimestamp()*1000)), TimeZone.getDefault().toZoneId()).toString();
						value = vectorData.getDataValue().getValue();
						means.set(index,means.get(index)+value);
						if(value>maxs.get(index)) {
							maxs.set(index,value);
						}
						counters.set(index,counters.get(index)+1);
//						log.info(String.format("%s", vectorData.getMetric().get("name")));
//						log.info(String.format("%s %10.2f ",
//									OffsetDateTime.ofInstant(Instant.ofEpochMilli(Math.round(vectorData.getDataValue().getTimestamp()*1000)), TimeZone.getDefault().toZoneId()),
//									vectorData.getDataValue().getValue()
//									));
					}
				}
				
			}
			List<Integer> meanInts = new ArrayList<>();
			for(int i=0;i<means.size();i++) {
				means.set(i, means.get(i)/counters.get(i));
				meanInts.add((int) Math.round(means.get(i)));
//				data.get(i).set(0,(int) Math.round(means.get(i)));
			}
			for(int i=0;i<maxs.size();i++) {
				data.get(i).set(0,(int) Math.round(maxs.get(i)));
			}
//			log.info(data.toString());
			notification = notifBuilder.addEvent(notification, NwdafEventEnum.NF_LOAD, null, null, now, null, null, null, data, dataOptionals);
	
			break;
		default:
			break;
	}
		
		return notification;
	}
}
