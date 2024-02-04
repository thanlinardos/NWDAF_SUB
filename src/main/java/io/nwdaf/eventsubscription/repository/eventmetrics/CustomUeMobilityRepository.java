package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

public interface CustomUeMobilityRepository extends CustomEventMetricsRepository<UeMobilityTable> {

    List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String filter, String no_secs, String end, String offset);
}
