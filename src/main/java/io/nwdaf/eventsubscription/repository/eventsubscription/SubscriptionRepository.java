package io.nwdaf.eventsubscription.repository.eventsubscription;


import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import jakarta.transaction.Transactional;

@Repository("eventsubscription")
@EntityScan("io.nwdaf.eventsubscription.repository.eventsubscription")
public interface SubscriptionRepository
        extends JpaRepository<NnwdafEventsSubscriptionTable, Long> {

    String FILTER_FOR_JSONB_COLUMN = "select id, sub from nnwdaf_events_subscription where sub @> ";

    List<NnwdafEventsSubscriptionTable> findAll();

    @Query(value = "update nnwdaf_events_subscription set sub @> ?1 where id=?2",
            nativeQuery = true)
    NnwdafEventsSubscriptionTable update(NnwdafEventsSubscription body, Long id);

    @Override
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "truncate table nnwdaf_events_subscription", nativeQuery = true)
    void truncate();

}
