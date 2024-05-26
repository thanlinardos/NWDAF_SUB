package io.nwdaf.eventsubscription.repository.eventsubscription;

import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;

import java.util.List;

public interface CustomEventSubscriptionRepository {
    List<NnwdafEventsSubscriptionTable> findAllInLastFilter(String filter, Boolean not);

    List<Long> findAllIdsInLastFilter(String filter, Boolean not);
}
