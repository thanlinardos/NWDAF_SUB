package io.nwdaf.eventsubscription.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NwdafEvent;

public interface SubscriptionRepository extends ListCrudRepository<NnwdafEventsSubscription, Integer>{
	
	List<NnwdafEventsSubscription> findAllByNotificationURIEquals(String clientURI);
	NnwdafEventsSubscription findOneByNotificationURIEquals(String clientURI);
	
	@Query("""
	    SELECT * FROM NnwdafEventsSubscription
	    WHERE eventSubscriptions.event = :event
	""")
	List<NnwdafEventsSubscription> listByEventsSubscriptions(@Param("event") NwdafEvent event);
}
