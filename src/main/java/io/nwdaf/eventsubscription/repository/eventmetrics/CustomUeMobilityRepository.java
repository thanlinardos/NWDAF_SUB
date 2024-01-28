package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.time.OffsetDateTime;
import java.util.List;

import io.nwdaf.eventsubscription.model.PointUncertaintyCircle;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;
import org.apache.commons.math3.linear.RealVector;

public interface CustomUeMobilityRepository {

    List<UeMobilityTable> findAllInLastIntervalByFilterAndOffset(String filter,String no_secs,String offset, String columns);

    List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String filter, String no_secs, String offset);

    List<OffsetDateTime> findAvailableMetricsTimeStamps();
}
