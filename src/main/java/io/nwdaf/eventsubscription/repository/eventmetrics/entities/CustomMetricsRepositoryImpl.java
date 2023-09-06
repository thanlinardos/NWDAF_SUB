package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.util.List;


import io.nwdaf.eventsubscription.repository.eventmetrics.CustomMetricsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomMetricsRepositoryImpl implements CustomMetricsRepository{

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked") 
    public List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,String offset, String columns) {
        String querry = "select distinct on (time_bucket(cast(:offset as interval), time), nfInstanceId, nfSetId) time_bucket(cast(:offset as interval), time) AS time , data , nfInstanceId, nfSetId,";
        if(columns == ""){
            querry += 
                "CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,"+
                "CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,"+
                "CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,"+
                "CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,"+
                "CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,"+
                "CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi,";
        }
        else{
            querry += columns;
        }
        querry += "areaOfInterestId" + " from nf_load_metrics where time > NOW() - cast(:no_secs as interval)";
        if(params!=null){
            querry += " and " + params;
        }
        querry += " GROUP BY time_bucket(cast(:offset as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;";
        // System.out.println(querry);
        // +" ORDER BY time_bucket(cast(:offset as interval), time) DESC, time, nfInstanceId, nfSetId;"
        return entityManager.createNativeQuery(querry, NfLoadLevelInformationTable.class).setParameter("offset",offset).setParameter("no_secs",no_secs).getResultList();
    }
}
