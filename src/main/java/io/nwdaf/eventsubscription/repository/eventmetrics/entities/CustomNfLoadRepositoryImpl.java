package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.util.List;
import java.util.Objects;

import io.nwdaf.eventsubscription.repository.eventmetrics.CustomNfLoadRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomNfLoadRepositoryImpl implements CustomNfLoadRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
            String offset, String columns) {
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
        String query = """
        select distinct on (time_bucket(cast(:offset as interval), time), nfInstanceId, nfSetId) 
        time_bucket(cast(:offset as interval), time) AS time , data , nfInstanceId, nfSetId, 
        """ + columns + """
           areaOfInterestId from nf_load_metrics where time > NOW() - cast(:no_secs as interval) 
          """ + filter + """
         GROUP BY time_bucket(cast(:offset as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;
         """;
        // System.out.println(query);
        // +" ORDER BY time_bucket(cast(:offset as interval), time) DESC, time,
        // nfInstanceId, nfSetId;"
        return entityManager.createNativeQuery(query, NfLoadLevelInformationTable.class).setParameter("offset", offset)
                .setParameter("no_secs", no_secs).getResultList();
    }
}
