package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

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
        String querry = "select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId) time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId, areaOfInterestId";
        if (Objects.equals(columns, "")) {

        } else {
            querry += columns;
        }
        querry += " from ue_communication_metrics where time > NOW() - cast(:no_secs as interval)";
        if (params != null) {
            querry += " and " + params;
        }
        querry += " GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId, areaOfInterestId;";
        // System.out.println(querry);
        // +" ORDER BY time_bucket(cast(:offset as interval), time) DESC, time,
        // nfInstanceId, nfSetId;"
        return entityManager.createNativeQuery(querry, UeCommunicationTable.class).setParameter("offset", offset)
                .setParameter("no_secs", no_secs).getResultList();
    }
}
