package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeCommunicationTable;

@Repository("eventUeCommunicationMetrics")
@EntityScan("io.nwdaf.eventsubscription.repository.eventmetrics")
public interface UeCommunicationMetricsRepository extends JpaRepository<UeCommunicationTable, OffsetDateTime>,
        CustomUeCommunicationRepository {

    List<UeCommunicationTable> findAll();

    @Query(value = "select * from ue_communication_metrics where date_trunc('seconds',cast(time as timestamp)) = date_trunc('seconds',cast(? as timestamp))", nativeQuery = true)
    List<UeCommunicationTable> findAllByTime(OffsetDateTime time);

    @Query(value = "select * from ue_communication_metrics where date_trunc('seconds',cast(time as timestamp)) = date_trunc('seconds',cast(?1 as timestamp)) and (?2)", nativeQuery = true)
    List<UeCommunicationTable> findAllByTimeAndFilter(OffsetDateTime time, String filter);

    @Query(value = "select * from ue_communication_metrics where time > NOW() - INTERVAL '1 second' and (?1)", nativeQuery = true)
    List<UeCommunicationTable> findAllInLastSecondByFilter(String filter);

    @Query(value = "select * from ue_communication_metrics where time > NOW() - INTERVAL '1 second'", nativeQuery = true)
    List<UeCommunicationTable> findAllInLastSecond();

    @Query(value = "select * from ue_communication_metrics where time > NOW() - INTERVAL '?2 second' and (?1)", nativeQuery = true)
    List<UeCommunicationTable> findAllInLastIntervalByFilter(String filter, Integer no_secs);

    @Query(value = "select * from ue_communication_metrics where time > NOW() - INTERVAL '? second'", nativeQuery = true)
    List<UeCommunicationTable> findAllInLastInterval(Integer no_secs);

    // @Query(value="select id, sub from nf_load_metrics where data @>
    // '{\"notificationURI\":\"http://localhost:8082/client\"}'",
    // nativeQuery=true)
    // List<UeCommunicationTable> findAllByNotifURI(String
    // filterForClientURI);

    @Modifying
    @Transactional
    @Query(value = "truncate table ue_communication_metrics", nativeQuery = true)
    void truncate();
}
