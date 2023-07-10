package io.nwdaf.eventsubscription.repository.eventsubscription;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<NnwdafEventsSubscriptionTable, Long>{
	
	
}
