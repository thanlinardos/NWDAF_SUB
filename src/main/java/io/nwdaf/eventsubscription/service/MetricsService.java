package io.nwdaf.eventsubscription.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.repository.eventmetrics.MetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Service
public class MetricsService {

	private final MetricsRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	public MetricsService(MetricsRepository repository) {
		this.repository = repository;
	}
	
	@Async
	public void asyncCreate(NfLoadLevelInformation body) {
		NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
		System.out.println("nwdaf saved nf_load to timescale: "+bodyTable.getTime());
		repository.save(bodyTable);
	}

	public NfLoadLevelInformationTable create(NfLoadLevelInformation body) {
		NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
		return repository.save(bodyTable);
	}

	// NF_LOAD
	public List<NfLoadLevelInformation> findAllByTimeAndFilter(OffsetDateTime time, String params)
			throws JsonMappingException, JsonProcessingException {
		List<NfLoadLevelInformationTable> tables;
		if (params != null) {
			tables = repository.findAllByTimeAndFilter(time, params);
		} else {
			tables = repository.findAllByTime(time);
		}
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i) != null) {
				NfLoadLevelInformation info = NfLoadLevelInformation.fromMap(tables.get(i).getData());
				info.setTime(tables.get(i).getTime().toInstant());
				res.add(info);
			}
		}
		return res;
	}

	// NF_LOAD
	public List<NfLoadLevelInformation> findAllInLastIntervalByFilter(String params, Integer no_secs)
			throws JsonMappingException, JsonProcessingException {
		List<NfLoadLevelInformationTable> tables;
		if (no_secs != null) {
			if (params != null) {
				tables = repository.findAllInLastIntervalByFilter(params, no_secs);
			} else {
				tables = repository.findAllInLastInterval(no_secs);
			}
		} else {
			if (params != null) {
				tables = repository.findAllInLastSecondByFilter(params);
			} else {
				tables = repository.findAllInLastSecond();
			}
		}
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i) != null) {
				NfLoadLevelInformation info = NfLoadLevelInformation.fromMap(tables.get(i).getData());
				info.setTime(tables.get(i).getTime().toInstant());
				res.add(info);
			}
		}
		return res;
	}

	// NF_LOAD
	public List<NfLoadLevelInformation> findAllInLastIntervalByFilterAndOffset(String params, Integer no_secs,
			Integer offset, String columns) throws JsonMappingException, JsonProcessingException {
		List<NfLoadLevelInformationTable> tables;
		if (no_secs == null) {
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if (offset == 0) {
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		tables = repository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second", offset + " second",
				columns);
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i) != null) {
				NfLoadLevelInformation info = NfLoadLevelInformation.fromMap(tables.get(i).getData());
				if (tables.get(i).getTime() != null) {
					info.time(tables.get(i).getTime().toInstant());
					// System.out.println("nwdaf read nf_load from timescale: "+info.getTimeStamp()+" (table: "+tables.get(i).getTime()+")");
				}
				info.nfCpuUsage(tables.get(i).getNfCpuUsage())
						.nfMemoryUsage(tables.get(i).getNfMemoryUsage())
						.nfStorageUsage(tables.get(i).getNfStorageUsage())
						.nfLoadLevelAverage(tables.get(i).getNfLoadLevelAverage())
						.nfLoadLevelpeak(tables.get(i).getNfLoadLevelpeak())
						.nfLoadAvgInAoi(tables.get(i).getNfLoadAvgInAoi());
				res.add(info);
			}
		}
		return res;
	}

	// UE_MOBILITY
	public UeMobilityTable create(UeMobility body) throws JsonProcessingException, JSONException {
		UeMobilityTable body_table = new UeMobilityTable(body);
		String data=null;
		data = new JSONObject(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(body)).toString();
		repository.saveMobilityTable(body.getTs(),data);
		return body_table;
	}

	public List<UeMobility> findAllUeMobilityInLastIntervalByFilterAndOffset(String params, Integer no_secs,
			Integer offset, String columns) throws JsonMappingException, JsonProcessingException {
		List<UeMobilityTable> tables;
		if (no_secs == null) {
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if (offset == 0) {
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		tables = repository.findAllUeMobilityInLastIntervalByFilterAndOffset(params, no_secs + " second",
				offset + " second", columns);
		List<UeMobility> res = new ArrayList<>();
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i) != null) {
				UeMobility info = UeMobility.fromMap(tables.get(i).getData());
				if (tables.get(i).getTime() != null) {
					info.time(tables.get(i).getTime().toInstant());
				}
				res.add(info);
			}
		}
		return res;
	}

	public boolean truncate(){
		try{
			repository.truncateNfLoad();
			repository.truncateUeMobility();
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
}