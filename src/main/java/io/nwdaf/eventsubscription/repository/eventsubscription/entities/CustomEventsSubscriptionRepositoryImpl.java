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
    public List<NnwdafEventsSubscriptionTable> findAllInLastFilter(String filter) {
        String query = """
                select * from nnwdaf_events_subscription where sub @> 
                """ + filter + ";";
        return (List<NnwdafEventsSubscriptionTable>) entityManager.createNativeQuery(query, NnwdafEventsSubscriptionTable.class).getResultList();
    }

}
