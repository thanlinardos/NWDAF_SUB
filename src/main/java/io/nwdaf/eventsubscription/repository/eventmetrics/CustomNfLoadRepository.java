package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;

public interface CustomNfLoadRepository {
    	List<NfLoadLevelInformationTable> findAllInLastIntervalByFilterAndOffset(String filter,String no_secs,String offset, String columns);
}
