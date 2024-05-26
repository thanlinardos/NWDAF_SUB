package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.time.OffsetDateTime;
import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomEventMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeCommunicationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomUeCommunicationRepositoryImpl implements CustomEventMetricsRepository<UeCommunicationTable>, CustomUeCommunicationRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<UeCommunicationTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                             String end, String offset, String columns, Boolean historic) {
        if (params != null) {
            params = " and " + params;
        } else {
            params = "";
        }
        String table = historic ? " compressed_ue_communication_metrics" : " ue_communication_metrics";

        String query = """
                select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) 
                time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId, 
                 """ + columns + """
                areaOfInterestId from """ + table + """
                 where time >= NOW() - cast(:no_secs as interval) 
                 and time <= NOW() - cast(:end as interval) 
                """ + params + """
                GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId, areaOfInterestId;
                """;
        return entityManager.createNativeQuery(query, UeCommunicationTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end) {
        String query = """
                select distinct date_trunc('second', time) as time from ue_communication_metrics 
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
                select distinct (time_bucket(cast(:offset as interval), time)) as time from ue_communication_metrics 
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
                select distinct time from compressed_ue_communication_metrics 
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
                select distinct date_trunc('second', time) as time from ue_communication_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    public OffsetDateTime findOldestHistoricTimeStamp() {
        String query = """
                select distinct time from compressed_ue_communication_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getSupis() {
        String query = """
                select distinct supi from ue_communication_metrics;
                """;
        return entityManager.createNativeQuery(query).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getSupis(String groupId) {
        if (groupId == null) return List.of();
        String query = """
                select distinct supi from ue_communication_metrics where intGroupId =
                """ + " '" + groupId + "';";
        return entityManager.createNativeQuery(query)
                .getResultList();
    }
}
