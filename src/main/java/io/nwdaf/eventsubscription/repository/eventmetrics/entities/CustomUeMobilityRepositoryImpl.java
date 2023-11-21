package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeMobilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomUeMobilityRepositoryImpl implements CustomUeMobilityRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<UeMobilityTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
            String offset, String columns) {
        if (params != null) {
            params += " and " + params;
        } else {
            params = "";
        }
        if (Objects.equals(columns, "")) {

        }
        String query = """
        select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) 
        time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId 
         """ + columns + """
         from ue_mobility_metrics where time > NOW() - cast(:no_secs as interval) 
        """ + params + """
        GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId;
        """;
        return entityManager.createNativeQuery(query, UeMobilityTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .getResultList();
    }
}
