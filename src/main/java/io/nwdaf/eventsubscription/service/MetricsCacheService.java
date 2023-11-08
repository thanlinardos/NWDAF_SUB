package io.nwdaf.eventsubscription.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.repository.redis.RedisMetricsRepository;
import io.nwdaf.eventsubscription.repository.redis.entities.NfLoadLevelInformationHash;
import io.nwdaf.eventsubscription.utilities.Constants;

@Service
public class MetricsCacheService {
	@Autowired
	private RedisMetricsRepository repository;
	@Autowired
	private MetricsService metricsService;

	public NfLoadLevelInformationHash create(NfLoadLevelInformation body) {
		NfLoadLevelInformationHash bodyHash = new NfLoadLevelInformationHash(body);
		metricsService.asyncCreate(body);
		return repository.save(bodyHash);
	}

	public List<NfLoadLevelInformation> findAllByTimeAndFilter(OffsetDateTime time, String params) {
		List<NfLoadLevelInformationHash> entities;
		if (params != null) {
			entities = repository.findByTime(time).stream()
					.filter(e -> true)
					.collect(Collectors.toList());
		} else {
			entities = repository.findByTime(time);
		}
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				NfLoadLevelInformation info = entities.get(i).getData();
				info.setTime(entities.get(i).getTime().toInstant());
				res.add(info);
			}
		}
		if (res.size() == 0) {
			try{
			res = metricsService.findAllByTimeAndFilter(time, params);
			} catch(Exception e) {
				NwdafSubApplication.getLogger().error("Metrics Service threw: ", e);
			}
		}
		return res;
	}

	public List<NfLoadLevelInformation> findAllInLastIntervalByFilterAndOffset(EventSubscription eventSub,
			List<String> filterTypes, String params, Integer no_secs, Integer offset, String columns)
			throws JsonMappingException, JsonProcessingException {
		List<NfLoadLevelInformationHash> entities;
		if (no_secs == null) {
			no_secs = Constants.MIN_PERIOD_SECONDS;
		}
		if (offset == 0) {
			offset = Constants.MIN_PERIOD_SECONDS;
		}
		final long noSecsFinal = no_secs;
		final int offsetFinal = offset;
		int oldestIndex = 0;
		OffsetDateTime oldestDate = OffsetDateTime.now();
		// OffsetDateTime r0 =
		// repository.findAll().get(repository.findAll().size()-1).getTime();
		// System.out.println(r0 + " , "+OffsetDateTime.now());
		// System.out.println(r0.toInstant().toEpochMilli() >=
		// OffsetDateTime.now().minusSeconds(noSecsFinal).toInstant().toEpochMilli());
		entities = repository.findAll().stream()
				.filter(e -> e.getTime().toInstant().toEpochMilli() >= OffsetDateTime.now().minusSeconds(noSecsFinal)
						.toInstant().toEpochMilli() &&
						e.getTime().toLocalTime().toSecondOfDay() % offsetFinal == 0 &&
						(!filterTypes.contains("nfInstanceId")
								|| (e.getNfInstanceId() != null
										&& eventSub.getNfInstanceIds().contains(UUID.fromString(e.getNfInstanceId()))))
						&&
						(!filterTypes.contains("nfSetId") || (e.getData().getNfSetId() != null
								&& eventSub.getNfSetIds().contains(e.getData().getNfSetId())))
						&&
						(!filterTypes.contains("aoi") || (e.getData().getAreaOfInterestId() != null && eventSub
								.getNetworkArea().getContainedAreaIds().contains(e.getData().getAreaOfInterestId())))
						&&
						(!filterTypes.contains("snssai") || (e.getData().getSnssai() != null
								&& eventSub.getSnssaia().contains(e.getData().getSnssai())))
						&&
						(!filterTypes.contains("nfType") || (e.getData().getNfType() != null
								&& eventSub.getNfTypes().contains(e.getData().getNfType()))))
				.collect(Collectors.toList());
		List<NfLoadLevelInformation> res = new ArrayList<>();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				NfLoadLevelInformation info = entities.get(i).getData();
				if (entities.get(i).getTime() != null) {
					info.time(entities.get(i).getTime().toInstant());
					if (entities.get(i).getTime().isBefore(oldestDate)) {
						oldestIndex = i;
						oldestDate = entities.get(i).getTime();
					}
				}
				info.nfCpuUsage(entities.get(i).getNfCpuUsage())
						.nfMemoryUsage(entities.get(i).getNfMemoryUsage())
						.nfStorageUsage(entities.get(i).getNfStorageUsage())
						.nfLoadLevelAverage(entities.get(i).getNfLoadLevelAverage())
						.nfLoadLevelpeak(entities.get(i).getNfLoadLevelpeak())
						.nfLoadAvgInAoi(entities.get(i).getNfLoadAvgInAoi());
				res.add(info);
			}
		}
		// if redis doesn't have enough data then look inside timescaleDB
		long availableNoSecs = (Instant.now().toEpochMilli() - oldestDate.toInstant().toEpochMilli()) / 1000;
		if (availableNoSecs < noSecsFinal) {
			// List<NfLoadLevelInformation> postgresRes =
			// metricsService.findAllInLastIntervalByFilterAndOffset(params, no_secs,
			// offset, columns);
			if (oldestIndex == 0) {
				// postgresRes.addAll(res);
				// return postgresRes;
			}
			// res.addAll(postgresRes);
		}
		return res;
	}
}
