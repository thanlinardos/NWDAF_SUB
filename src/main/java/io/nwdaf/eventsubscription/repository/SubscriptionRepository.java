package io.nwdaf.eventsubscription.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionTable;

@Repository
public interface SubscriptionRepository extends JpaRepository<NnwdafEventsSubscriptionTable, Long>{
	
	
}
