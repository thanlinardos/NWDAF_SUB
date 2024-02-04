package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomEventMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomNfLoadRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomNfLoadRepositoryImpl implements CustomEventMetricsRepository<NfLoadLevelInformationTable>, CustomNfLoadRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs, String end,
            String offset, String columns, Boolean historic) {
        String filter;
        if (params != null) {
            filter = " and " + params;
        } else {
            filter = "";
        }
        if(Objects.equals(columns, "")) {
            columns += """
                    CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,
                    CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,
                    CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,
                    CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,
                    CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,
                    CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi,
                    """;
        }
        String table = historic ? " compressed_nf_load_metrics" : " nf_load_metrics";

        String query = """
        select distinct on (time_bucket(cast(:offset as interval), time), nfInstanceId, nfSetId) 
        time_bucket(cast(:offset as interval), time) AS time , data , nfInstanceId, nfSetId, 
        """ + columns + """
           areaOfInterestId from""" + table + """
           where time >= NOW() - cast(:no_secs as interval) 
           and time <= NOW() - cast(:end as interval) 
          """ + filter + """
         GROUP BY time_bucket(cast(:offset as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;
         """;
        return entityManager.createNativeQuery(query, NfLoadLevelInformationTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableHistoricMetricsTimeStamps(String start, String end) {
        String query = """
        select distinct time from compressed_nf_load_metrics 
        where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
        order by time desc;
        """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end) {
        String query = """
        select distinct date_trunc('second', time) as time from nf_load_metrics 
        where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
        order by time desc;
        """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end, Integer offset) {
        String query = """
        select distinct time_bucket(cast(:offset as interval), time) as time from nf_load_metrics 
        where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
        order by time desc;
        """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public OffsetDateTime findOldestTimeStamp() {
        String query = """
        select distinct time from nf_load_metrics order by time limit 1;
        """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    public OffsetDateTime findOldestHistoricTimeStamp() {
        String query = """
        select distinct time from compressed_nf_load_metrics order by time limit 1;
        """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }
}
