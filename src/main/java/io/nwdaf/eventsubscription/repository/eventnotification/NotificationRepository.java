package io.nwdaf.eventsubscription.repository.eventnotification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository  extends JpaRepository<NnwdafNotificationTable, Long>{

}
