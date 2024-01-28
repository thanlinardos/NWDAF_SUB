package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeCommunicationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomUeCommunicationRepositoryImpl implements CustomUeCommunicationRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<UeCommunicationTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                        String offset, String columns) {
        if (params != null) {
            params += " and " + params;
        } else {
            params = "";
        }
        if(Objects.equals(columns, "")) {

        }
        String query = """
        select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) 
        time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId, 
         """ + columns + """
         areaOfInterestId from ue_communication_metrics where time > NOW() - cast(:no_secs as interval) 
        """ + params + """
        GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId, areaOfInterestId;
        """;
        return entityManager.createNativeQuery(query, UeCommunicationTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps() {
        String query = """
        select distinct time from compressed_ue_communication_metrics order by time desc;
        """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class).getResultList();
    }
}
