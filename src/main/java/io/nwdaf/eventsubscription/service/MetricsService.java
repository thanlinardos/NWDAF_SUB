package io.nwdaf.eventsubscription.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.repository.eventmetrics.NfLoadMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.UeMobilityMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Service
public class MetricsService {

	private final NfLoadMetricsRepository nfLoadRepository;
	private final UeMobilityMetricsRepository ueMobilityRepository;

	public MetricsService(NfLoadMetricsRepository nfLoadRepository, UeMobilityMetricsRepository ueMobilityMetricsRepository) {
		this.nfLoadRepository = nfLoadRepository;
		this.ueMobilityRepository = ueMobilityMetricsRepository;
	}
	
	@Async
	public void asyncCreate(NfLoadLevelInformation body) {
		NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
		nfLoadRepository.save(bodyTable);
	}

	public NfLoadLevelInformationTable create(NfLoadLevelInformation body) throws JsonProcessingException {
		NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
		return nfLoadRepository.save(bodyTable);
	}

	// NF_LOAD
	public List<NfLoadLevelInformation> findAllByTimeAndFilter(OffsetDateTime time, String params) {
		List<NfLoadLevelInformationTable> tables;
		if (params != null) {
			tables = nfLoadRepository.findAllByTimeAndFilter(time, params);
		} else {
			tables = nfLoadRepository.findAllByTime(time);
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
	public List<NfLoadLevelInformation> findAllInLastIntervalByFilter(String params, Integer no_secs) {
		List<NfLoadLevelInformationTable> tables;
		if (no_secs != null) {
			if (params != null) {
				tables = nfLoadRepository.findAllInLastIntervalByFilter(params, no_secs);
			} else {
				tables = nfLoadRepository.findAllInLastInterval(no_secs);
			}
		} else {
			if (params != null) {
				tables = nfLoadRepository.findAllInLastSecondByFilter(params);
			} else {
				tables = nfLoadRepository.findAllInLastSecond();
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
			Integer offset, String columns) {
		List<NfLoadLevelInformationTable> tables;
		if (no_secs == null) {
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if (offset == 0) {
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		tables = nfLoadRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second", offset + " second",
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
	public UeMobilityTable create(UeMobility body) throws JsonProcessingException {
		UeMobilityTable bodyTable = new UeMobilityTable(body);
		return ueMobilityRepository.save(bodyTable);
	}

	public List<UeMobility> findAllUeMobilityInLastIntervalByFilterAndOffset(String params, Integer no_secs,
			Integer offset, String columns) {
		List<UeMobilityTable> tables;
		if (no_secs == null) {
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if (offset == 0) {
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		tables = ueMobilityRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second",
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
			nfLoadRepository.truncate();
			ueMobilityRepository.truncate();
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
}