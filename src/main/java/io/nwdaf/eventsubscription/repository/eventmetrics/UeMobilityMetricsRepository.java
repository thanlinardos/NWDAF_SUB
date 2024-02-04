package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Repository("eventUeMobilityMetrics")
@EntityScan("io.nwdaf.eventsubscription.repository.eventmetrics")
public interface UeMobilityMetricsRepository extends JpaRepository<UeMobilityTable, OffsetDateTime>,
		CustomUeMobilityRepository {

	@Modifying
	@Transactional
	@Query(value = "truncate table ue_mobility_metrics", nativeQuery = true)
	void truncate();
}
