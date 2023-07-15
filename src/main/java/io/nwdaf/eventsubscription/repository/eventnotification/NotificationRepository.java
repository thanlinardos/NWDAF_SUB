package io.nwdaf.eventsubscription.repository.eventnotification;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;



@Repository("eventnotification")
@EntityScan("io.nwdaf.eventsubscription.repository.eventnotification")
public interface NotificationRepository  extends JpaRepository<NnwdafNotificationTable, Long>{

}
