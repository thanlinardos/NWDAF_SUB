package io.nwdaf.eventsubscription.repository.redis;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.repository.redis.entities.NfLoadLevelInformationHash;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.OffsetDateTime;


@Repository("redis")
@EntityScan("io.nwdaf.eventsubscription.repository.redis")
public interface RedisMetricsRepository extends JpaRepository<NfLoadLevelInformationHash, String> {
    List<NfLoadLevelInformationHash> findByAreaOfInterestId(UUID areaOfInterestId);

    Optional<NfLoadLevelInformationHash> findById(String id);

    List<NfLoadLevelInformationHash> findByNfInstanceId(UUID nfInstanceId);

    List<NfLoadLevelInformationHash> findByTime(OffsetDateTime time);
}