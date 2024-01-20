package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.model.PointUncertaintyCircle;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeMobilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.math3.linear.RealVector;

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
    @Override
    @SuppressWarnings("unchecked")
    public List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                                         String offset) {
        if (params != null) {
            params += " and " + params;
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
            from ue_mobility_metrics where time > NOW() - cast(:no_secs as interval) 
        """ + params + """
        GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId;
        """;
        return entityManager.createNativeQuery(query, PointUncertaintyCircleResult.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .getResultList();
    }
}
