package io.nwdaf.eventsubscription.repository.redis;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.repository.redis.entities.NfLoadLevelId;
import io.nwdaf.eventsubscription.repository.redis.entities.NfLoadLevelInformationCached;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.OffsetDateTime;



@Repository("redis")
@EntityScan("io.nwdaf.eventsubscription.repository.redis")
public interface RedisMetricsRepository extends JpaRepository<NfLoadLevelInformationCached,String>{
    List<NfLoadLevelInformationCached> findByAreaOfInterestId(UUID areaOfInterestId);
    Optional<NfLoadLevelInformationCached> findById(String id);
    List<NfLoadLevelInformationCached> findByIdObject(NfLoadLevelId idObject);
    List<NfLoadLevelInformationCached> findByNfInstanceId(UUID nfInstanceId);
    List<NfLoadLevelInformationCached> findByTime(OffsetDateTime time);
}