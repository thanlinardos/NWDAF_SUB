package io.nwdaf.eventsubscription.repository.eventnotification;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import jakarta.transaction.Transactional;



@Repository("eventnotification")
@EntityScan("io.nwdaf.eventsubscription.repository.eventnotification")
public interface NotificationRepository  extends JpaRepository<NnwdafNotificationTable, Long>{
    @Modifying
	@Transactional
    @Query(value = "truncate table nnwdaf_notification", nativeQuery = true)
    void truncate();
}
