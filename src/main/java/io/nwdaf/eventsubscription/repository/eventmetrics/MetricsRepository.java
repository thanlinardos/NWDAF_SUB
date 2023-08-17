package io.nwdaf.eventsubscription.repository.eventmetrics;


import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository("eventmetrics")
@EntityScan("io.nwdaf.eventsubscription.repository.eventmetrics")
public interface MetricsRepository extends JpaRepository<NfLoadLevelInformationTable, OffsetDateTime>{
	
	List<NfLoadLevelInformationTable> findAll();
	
	@Query(value="select * from nf_load_metrics where date_trunc('seconds',cast(time as timestamp)) = date_trunc('seconds',cast(? as timestamp))",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllByTime(OffsetDateTime time);
	
	@Query(value="select * from nf_load_metrics where date_trunc('seconds',cast(time as timestamp)) = date_trunc('seconds',cast(?1 as timestamp)) and (?2)",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllByTimeAndFilter(OffsetDateTime time,String filter);

	@Query(value="select * from nf_load_metrics where time > NOW() - INTERVAL '1 second' and (?1)",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastSecondByFilter(String filter);
	
	@Query(value="select * from nf_load_metrics where time > NOW() - INTERVAL '1 second'",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastSecond();

	@Query(value="select * from nf_load_metrics where time > NOW() - INTERVAL '?2 second' and (?1)",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastIntervalByFilter(String filter,Integer no_secs);
	
	@Query(value="select * from nf_load_metrics where time > NOW() - INTERVAL '? second'",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastInterval(Integer no_secs);

	@Query(value="select distinct on (time_bucket(:offset, time), nfInstanceId, nfSetId) time_bucket(:offset, time)"+
	" AS time , data , nfInstanceId, nfSetId,"+
    "CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,"+
	"CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,"+
	"CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi"+
	" from nf_load_metrics where time > NOW() - INTERVAL  ':no_secs' SECOND"+
	" and (:filter)"+
	" GROUP BY time_bucket(:offset, time) ,data, nfInstanceId, nfSetId"+
	" ORDER BY time_bucket(:offset, time) DESC;",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(@Param("filter") String filter,@Param("no_secs") Integer no_secs,@Param("offset") String offset);

	@Query(value="select distinct on (time_bucket('?2 second', time), nfInstanceId, nfSetId) time_bucket('?2 second', time)"+
	" AS time , data , nfInstanceId, nfSetId,"+
    "CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,"+
	"CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,"+
	"CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,"+
	"CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi"+
	" from nf_load_metrics where time > NOW() - INTERVAL '?1 second'"+
	" GROUP BY time_bucket('?2 second', time) ,data, nfInstanceId, nfSetId"+
	" ORDER BY time_bucket('?2 second', time) DESC;",
			nativeQuery=true)
	List<NfLoadLevelInformationTable> findAllInLastIntervalByOffset(Integer no_secs,Integer offset);
	
	@Query(value="select * from ue_mobility_metrics",
			nativeQuery=true)
	List<UeMobilityTable> findAllMobilityTables();
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="insert into ue_mobility_metrics(time,data) VALUES (?1,cast(?2 as jsonb))",
			nativeQuery=true)
	void saveMobilityTable(OffsetDateTime time,String data);
	
	
	
//	@Query(value="select id, sub from nf_load_metrics where data @> '{\"notificationURI\":\"http://localhost:8082/client\"}'",
//			nativeQuery=true)
//	List<NfLoadLevelInformationTable> findAllByNotifURI(String filterForClientURI);
	
	
}
