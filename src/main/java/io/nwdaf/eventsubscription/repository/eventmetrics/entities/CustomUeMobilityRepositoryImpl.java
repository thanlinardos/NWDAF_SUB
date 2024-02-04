package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomEventMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeMobilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomUeMobilityRepositoryImpl implements CustomEventMetricsRepository<UeMobilityTable>, CustomUeMobilityRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<UeMobilityTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                        String end, String offset, String columns, Boolean historic) {
        if (params != null) {
            params += " and " + params;
        } else {
            params = "";
        }
        String table = historic ? " compressed_ue_mobility_metrics" : " ue_mobility_metrics";
        String query = """
                select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) 
                time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId 
                 """ + columns + """
                 from """ + table + """
                 where time >= NOW() - cast(:no_secs as interval) 
                 and time <= NOW() - cast(:end as interval) 
                """ + params + """
                GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId;
                """;
        return entityManager.createNativeQuery(query, UeMobilityTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                                               String end, String offset) {
        if (params != null && !params.isEmpty()) {
            if (params.startsWith("data")) {
                params += " and " + params;
            } else {
                params = " and " + params + " ";
            }
        } else {
            params = "";
        }
        String query = """
                select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) cast(jsonb_path_query(
                    data, '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lat') as double precision) AS latitude,
                    cast(jsonb_path_query(data,
                    '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lon') as double precision) AS longitude,
                    cast(jsonb_path_query(data,
                    '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.uncertainty') as double precision) AS uncertainty
                    from ue_mobility_metrics where time >= NOW() - cast(:no_secs as interval) 
                    and time <= NOW() - cast(:end as interval) 
                """ + params + """
                GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId;
                """;
        return entityManager.createNativeQuery(query, PointUncertaintyCircleResult.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end) {
        String query = """
                select distinct date_trunc('second', time) as time from ue_mobility_metrics 
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
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end, Integer period) {
        String query = """
                select distinct (time_bucket(cast(:offset as interval), time)) as time from ue_mobility_metrics 
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
    public List<OffsetDateTime> findAvailableHistoricMetricsTimeStamps(String start, String end) {
        String query = """
                select distinct time from compressed_ue_mobility_metrics 
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
                select distinct time from ue_mobility_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    public OffsetDateTime findOldestHistoricTimeStamp() {
        String query = """
                select distinct time from compressed_ue_mobility_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }
}
