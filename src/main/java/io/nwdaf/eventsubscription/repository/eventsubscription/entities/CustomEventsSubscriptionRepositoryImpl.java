package io.nwdaf.eventsubscription.repository.eventsubscription.entities;

import io.nwdaf.eventsubscription.repository.eventsubscription.CustomEventSubscriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomEventsSubscriptionRepositoryImpl implements CustomEventSubscriptionRepository{

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    
}
