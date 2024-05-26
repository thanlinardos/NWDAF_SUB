package io.nwdaf.eventsubscription.repository.eventmetrics;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.AreaOfInterestTable;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("areaOfInterestRepository")
@EntityScan("io.nwdaf.eventsubscription.repository.eventmetrics")
public interface AreaOfInterestRepository extends JpaRepository<AreaOfInterestTable, Long> {

    @Modifying
    @Transactional
    @Query(value = "truncate table area_of_interest", nativeQuery = true)
    void truncate();

    @Query(value = "SELECT * FROM area_of_interest WHERE uuid = :id", nativeQuery = true)
    Optional<AreaOfInterestTable> findByUuid(UUID id);

    @Modifying
    @Transactional
    @Query(value = "delete from area_of_interest WHERE uuid = :id", nativeQuery = true)
    void deleteByUuid(UUID id);

    List<AreaOfInterestTable> findAllByIntGroupId(String groupId);
}