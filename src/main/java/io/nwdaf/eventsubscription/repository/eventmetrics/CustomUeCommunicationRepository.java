package io.nwdaf.eventsubscription.repository.eventmetrics;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeCommunicationTable;

import java.util.List;

public interface CustomUeCommunicationRepository extends CustomEventMetricsRepository<UeCommunicationTable> {
    List<String> getSupis();

    List<String> getSupis(String groupId);
}
