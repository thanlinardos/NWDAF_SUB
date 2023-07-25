package io.nwdaf.eventsubscription.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.repository.eventmetrics.MetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Service
public class MetricsService {
	
	private final MetricsRepository repository;
	
	@Autowired
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
		NwdafSubApplication.getLogger().info(body_table.getData().toString());
		return repository.save(body_table);
	}
	
	public List<NfLoadLevelInformation> findAllByTime(OffsetDateTime time) throws JsonMappingException, JsonProcessingException{
		List<NfLoadLevelInformationTable> tables = repository.findAllByTime(time);
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			NfLoadLevelInformation info = objectMapper.readValue((new JSONObject(tables.get(i).getData())).toString(),NfLoadLevelInformation.class);
			info.setTime(tables.get(i).getTime().toInstant());
			res.add(info);
		}
		return res;
	}
	
	public List<NfLoadLevelInformation> findAllInLastSecond() throws JsonMappingException, JsonProcessingException{
		List<NfLoadLevelInformationTable> tables = repository.findAllInLastSecond();
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for(int i=0;i<tables.size();i++) {
			NfLoadLevelInformation info = objectMapper.readValue((new JSONObject(tables.get(i).getData())).toString(),NfLoadLevelInformation.class);
			info.setTime(tables.get(i).getTime().toInstant());
			res.add(info);
		}
		return res;
	}
	
	public UeMobilityTable create(UeMobility body) {
		UeMobilityTable body_table = new UeMobilityTable();
		body_table.setData(objectMapper.convertValue(body,new TypeReference<Map<String, Object>>() {}));
		body_table.setTime(body.getTs());
		String data=null;
		try {
			data = new JSONObject(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(body)).toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		NwdafSubApplication.getLogger().info(data);
		repository.saveMobilityTable(body.getTs(),data);
		return body_table;
	}
}