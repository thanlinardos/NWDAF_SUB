package io.nwdaf.eventsubscription.repository.eventmetrics;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.OffsetDateTime;
import java.util.List;

@NoRepositoryBean
public interface CustomEventMetricsRepository<T> {
    List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end);

    List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end, Integer offset);

    List<OffsetDateTime> findAvailableHistoricMetricsTimeStamps(String start, String end);

    OffsetDateTime findOldestTimeStamp();

    OffsetDateTime findOldestHistoricTimeStamp();

    List<T> findAllInLastIntervalByFilterAndOffset(String filter, String no_secs, String end, String offset, String columns, Boolean historic);
}
