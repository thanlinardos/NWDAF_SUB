package io.nwdaf.eventsubscription.repository.eventsubscription;


import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

@Repository("eventsubscription")
@EntityScan("io.nwdaf.eventsubscription.repository.eventsubscription")
public interface SubscriptionRepository extends JpaRepository<NnwdafEventsSubscriptionTable, Long>{
	
	static final String FILTER_FOR_JSONB_COLUMN =
	            "select id, sub from nnwdaf_events_subscription where sub @> ";
	
	List<NnwdafEventsSubscriptionTable> findAll();
	
	@Query(value="select id, sub from nnwdaf_events_subscription where sub @> '{\"notificationURI\":\"http://localhost:8082/client\"}'",
			nativeQuery=true)
	List<NnwdafEventsSubscriptionTable> findAllByNotifURI(String filterForClientURI);
	
	
}
