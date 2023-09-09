package io.nwdaf.eventsubscription.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.bcel.Const;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.repository.eventmetrics.MetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Service
public class MetricsService {
	
	private final MetricsRepository repository;
	
	// @Autowired
	public MetricsService(MetricsRepository repository) {
		this.repository = repository;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public NfLoadLevelInformationTable create(NfLoadLevelInformation body) {
		NfLoadLevelInformationTable body_table = new NfLoadLevelInformationTable();
		body_table.setData(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		body_table.setTime(body.getTimeStamp());
		body_table.setNfInstanceId(body.getNfInstanceId());
		body_table.setNfSetId(body.getNfSetId());
		body_table.setAreaOfInterestId(body.getAreaOfInterestId());
		// NwdafSubApplication.getLogger().info("nfloadlevelinfo saved: "+body_table.getData().toString());
		return repository.save(body_table);
	}
	
	// NF_LOAD
	public List<NfLoadLevelInformation> findAllByTimeAndFilter(OffsetDateTime time,String params) throws JsonMappingException, JsonProcessingException{
		List<NfLoadLevelInformationTable> tables;
		if(params!=null){
			tables = repository.findAllByTimeAndFilter(time,params);
		}
		else{
			tables = repository.findAllByTime(time);
		}
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			if(tables.get(i)!=null){
				NfLoadLevelInformation info = objectMapper.readValue((new JSONObject(tables.get(i).getData())).toString(),NfLoadLevelInformation.class);
				info.setTime(tables.get(i).getTime().toInstant());
				res.add(info);
			}
		}
		return res;
	}

	// NF_LOAD
	public List<NfLoadLevelInformation> findAllInLastIntervalByFilter(String params,Integer no_secs) throws JsonMappingException, JsonProcessingException{
		List<NfLoadLevelInformationTable> tables;
		if(no_secs!=null){
			if(params!=null){
				tables = repository.findAllInLastIntervalByFilter(params,no_secs);
			}
			else{
				tables = repository.findAllInLastInterval(no_secs);
			}
		}
		else{
			if(params!=null){
				tables = repository.findAllInLastSecondByFilter(params);
			}
			else{
				tables = repository.findAllInLastSecond();
			}
		}
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			if(tables.get(i)!=null){
				NfLoadLevelInformation info = objectMapper.readValue((new JSONObject(tables.get(i).getData())).toString(),NfLoadLevelInformation.class);
				info.setTime(tables.get(i).getTime().toInstant());
				res.add(info);
			}
		}
		return res;
	}
	
	// NF_LOAD
	public List<NfLoadLevelInformation> findAllInLastIntervalByFilterAndOffset(String params,Integer no_secs,Integer offset,String columns) throws JsonMappingException, JsonProcessingException{
		List<NfLoadLevelInformationTable> tables;
		if(no_secs == null){
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if(offset==0){
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		tables = repository.findAllInLastIntervalByFilterAndOffset(params,no_secs+" second",offset+" second",columns);
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			if(tables.get(i)!=null){
				NfLoadLevelInformation info = objectMapper.readValue((new JSONObject(tables.get(i).getData())).toString(),NfLoadLevelInformation.class);
				info.setTime(tables.get(i).getTime().toInstant());
				info.setNfCpuUsage(tables.get(i).getNfCpuUsage());
				info.setNfMemoryUsage(tables.get(i).getNfMemoryUsage());
				info.setNfStorageUsage(tables.get(i).getNfStorageUsage());
				info.setNfLoadLevelAverage(tables.get(i).getNfLoadLevelAverage());
				info.setNfLoadLevelpeak(tables.get(i).getNfLoadLevelpeak());
				info.setNfLoadAvgInAoi(tables.get(i).getNfLoadAvgInAoi());
				res.add(info);
			}
		}
		return res;
	}

	// UE_MOBILITY
	public UeMobilityTable create(UeMobility body) throws JsonProcessingException, JSONException {
		UeMobilityTable body_table = new UeMobilityTable();
		body_table.setData(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		body_table.setTime(body.getTs());
		String data=null;
		data = new JSONObject(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(body)).toString();
		NwdafSubApplication.getLogger().info(data);
		repository.saveMobilityTable(body.getTs(),data);
		return body_table;
	}
}