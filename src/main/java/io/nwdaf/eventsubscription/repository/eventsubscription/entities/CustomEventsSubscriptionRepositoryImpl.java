package io.nwdaf.eventsubscription.repository.eventsubscription.entities;

import io.nwdaf.eventsubscription.repository.eventsubscription.CustomEventSubscriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customEventSubscription")
public class CustomEventsSubscriptionRepositoryImpl implements CustomEventSubscriptionRepository {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<NnwdafEventsSubscriptionTable> findAllInLastFilter(String filter, Boolean not) {
        String reverse = not ? " not" : "";
        String query = "select * from nnwdaf_events_subscription where" + reverse + " sub @> " + filter + ";";
        return (List<NnwdafEventsSubscriptionTable>) entityManager.createNativeQuery(query, NnwdafEventsSubscriptionTable.class).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findAllIdsInLastFilter(String filter, Boolean not) {
        String reverse = not ? " not" : "";
        String query = "select id from nnwdaf_events_subscription where" + reverse + " sub @> " + filter + ";";
        return (List<Long>) entityManager.createNativeQuery(query, Long.class).getResultList();
    }

}
