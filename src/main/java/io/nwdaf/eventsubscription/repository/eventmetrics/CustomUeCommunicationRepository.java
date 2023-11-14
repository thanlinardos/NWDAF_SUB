package io.nwdaf.eventsubscription.repository.eventmetrics;

import java.util.List;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeCommunicationTable;

public interface CustomUeCommunicationRepository {

    List<UeCommunicationTable> findAllInLastIntervalByFilterAndOffset(String filter, String no_secs, String offset, String columns);
}
