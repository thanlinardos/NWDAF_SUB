package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

public interface CustomMetricsRepository {
    	List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(String filter,String no_secs,String offset, String columns);
    	List<UeMobilityTable> findAllUeMobilityInLastIntervalByFilterAndOffset(String filter,String no_secs,String offset, String columns);
}
