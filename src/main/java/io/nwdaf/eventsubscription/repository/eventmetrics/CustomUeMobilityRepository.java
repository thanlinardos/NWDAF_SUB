package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.customModel.CellLocationType;
import io.nwdaf.eventsubscription.customModel.LocationInfoWithSupi;
import io.nwdaf.eventsubscription.customModel.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.model.LocationInfo;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

public interface CustomUeMobilityRepository extends CustomEventMetricsRepository<UeMobilityTable> {

    List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String filter, String no_secs, String end, String offset, boolean returnSupi);

    @SuppressWarnings("unchecked")
    List<String> getSupis();

    @SuppressWarnings("unchecked")
    List<String> getSupis(String groupId);

    List<LocationInfo> getLocationInfos(String groupId, CellLocationType type, ObjectMapper objectMapper);

    List<LocationInfo> getLocationInfos(CellLocationType type, ObjectMapper objectMapper);

    List<LocationInfoWithSupi> getLocationInfosWithSupi(String groupId, Optional<String> supi, CellLocationType cellType, ObjectMapper objectMapper);
}
