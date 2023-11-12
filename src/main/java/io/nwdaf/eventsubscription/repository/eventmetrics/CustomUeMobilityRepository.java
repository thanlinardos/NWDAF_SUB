package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

public interface CustomUeMobilityRepository {

    List<UeMobilityTable> findAllInLastIntervalByFilterAndOffset(String filter,String no_secs,String offset, String columns);
}
