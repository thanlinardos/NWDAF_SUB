package io.nwdaf.eventsubscription.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.UeCommunication;
import io.nwdaf.eventsubscription.repository.eventmetrics.UeCommunicationMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeCommunicationTable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.repository.eventmetrics.NfLoadMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.UeMobilityMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.NfLoadLevelInformationTable;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.UeMobilityTable;

@Service
public class MetricsService {

    private final NfLoadMetricsRepository nfLoadRepository;
    private final UeMobilityMetricsRepository ueMobilityRepository;
    private final UeCommunicationMetricsRepository ueCommunicationMetricsRepository;

    public MetricsService(NfLoadMetricsRepository nfLoadRepository,
                          UeMobilityMetricsRepository ueMobilityMetricsRepository,
                          UeCommunicationMetricsRepository ueCommunicationMetricsRepository) {
        this.nfLoadRepository = nfLoadRepository;
        this.ueMobilityRepository = ueMobilityMetricsRepository;
        this.ueCommunicationMetricsRepository = ueCommunicationMetricsRepository;
    }

    @Async
    public void asyncCreate(NfLoadLevelInformation body) {
        NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
        nfLoadRepository.save(bodyTable);
    }

    public NfLoadLevelInformationTable createNfload(NfLoadLevelInformation body) {
        NfLoadLevelInformationTable bodyTable = new NfLoadLevelInformationTable(body);
        return nfLoadRepository.save(bodyTable);
    }

    public List<NfLoadLevelInformationTable> createAllNfload(List<NfLoadLevelInformation> body) {
        return nfLoadRepository.saveAll(body.stream().map(NfLoadLevelInformationTable::new).toList());
    }

    // NF_LOAD
    public List<NfLoadLevelInformation> findAllByTimeAndFilter(OffsetDateTime time, String params) {
        List<NfLoadLevelInformationTable> tables;
        if (params != null) {
            tables = nfLoadRepository.findAllByTimeAndFilter(time, params);
        } else {
            tables = nfLoadRepository.findAllByTime(time);
        }
        return getNfLoadLevelInformationFromTable(tables);
    }

    // NF_LOAD
    public List<NfLoadLevelInformation> findAllInLastIntervalByFilter(String params, Integer no_secs) {
        List<NfLoadLevelInformationTable> tables;
        if (no_secs != null) {
            if (params != null) {
                tables = nfLoadRepository.findAllInLastIntervalByFilter(params, no_secs);
            } else {
                tables = nfLoadRepository.findAllInLastInterval(no_secs);
            }
        } else {
            if (params != null) {
                tables = nfLoadRepository.findAllInLastSecondByFilter(params);
            } else {
                tables = nfLoadRepository.findAllInLastSecond();
            }
        }
        return getNfLoadLevelInformationFromTable(tables);
    }

    // NF_LOAD
    public List<NfLoadLevelInformation> findAllInLastIntervalByFilterAndOffset(String params,
                                                                               Long no_secs,
                                                                               Long end,
                                                                               Integer offset,
                                                                               String columns) {
        List<NfLoadLevelInformationTable> tables;
        if (no_secs == null) {
            no_secs = Long.valueOf(Constants.MIN_PERIOD_SECONDS);
        }
        if (offset == 0) {
            offset = Constants.MIN_PERIOD_SECONDS;
        }
        if (end == null) {
            end = 0L;
        }
        if(no_secs < 3600 * 24) {
            tables = nfLoadRepository.
                    findAllInLastIntervalByFilterAndOffset(params, no_secs + " second", end + " second", offset + " second", columns, false);
        } else {
            tables = nfLoadRepository.
                    findAllInLastIntervalByFilterAndOffset(params, no_secs + " second", end + " second", offset + " second", columns, true);
        }
        List<NfLoadLevelInformation> res = new ArrayList<>();
        for (NfLoadLevelInformationTable table : tables) {
            if (table != null) {
                NfLoadLevelInformation info = NfLoadLevelInformation.fromMap(table.getData());
                if (table.getTime() != null) {
                    info.time(table.getTime().toInstant());
                }
                info.nfCpuUsage(table.getNfCpuUsage())
                        .nfMemoryUsage(table.getNfMemoryUsage())
                        .nfStorageUsage(table.getNfStorageUsage())
                        .nfLoadLevelAverage(table.getNfLoadLevelAverage())
                        .nfLoadLevelpeak(table.getNfLoadLevelpeak())
                        .nfLoadAvgInAoi(table.getNfLoadAvgInAoi());
                res.add(info);
            }
        }
        return res;
    }

    // UE_MOBILITY
    public UeMobilityTable createUeMob(UeMobility body) {
        UeMobilityTable bodyTable = new UeMobilityTable(body);
        return ueMobilityRepository.save(bodyTable);
    }

    public List<UeMobilityTable> createAllUeMobs(List<UeMobility> body) {
        return ueMobilityRepository.saveAll(body.stream().map(UeMobilityTable::new).toList());
    }

    public List<UeMobility> findAllUeMobilityInLastIntervalByFilterAndOffset(String params, Long no_secs,
                                                                             Long end, Integer offset, String columns) {
        List<UeMobilityTable> tables;
        if (no_secs == null) {
            no_secs = Long.valueOf(Constants.MIN_PERIOD_SECONDS);
        }
        if (offset == 0) {
            offset = Constants.MIN_PERIOD_SECONDS;
        }
        if (end == null) {
            end = 0L;
        }
        if(no_secs < 3600 * 24) {
            tables = ueMobilityRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second",
                    end + " second", offset + " second", columns, false);
        } else {
            tables = ueMobilityRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second",
                    end + " second", offset + " second", columns, true);
        }
        List<UeMobility> res = new ArrayList<>();
        for (UeMobilityTable table : tables) {
            if (table != null) {
                UeMobility info = UeMobility.fromMap(table.getData());
                if (table.getTime() != null) {
                    info.time(table.getTime().toInstant());
                }
                res.add(info);
            }
        }
        return res;
    }

    // UE_COMM
    public UeCommunicationTable createUeComm(UeCommunication body) {
        UeCommunicationTable bodyTable = new UeCommunicationTable(body);
        return ueCommunicationMetricsRepository.save(bodyTable);
    }

    public List<UeCommunicationTable> createAllUeCommms(List<UeCommunication> body) {
        return ueCommunicationMetricsRepository.saveAll(body.stream().map(UeCommunicationTable::new).toList());
    }

    public List<UeCommunication> findAllUeCommunicationInLastIntervalByFilterAndOffset(String params, Long no_secs,
                                                                                       Long end, Integer offset, String columns) {
        List<UeCommunicationTable> tables;
        if (no_secs == null) {
            no_secs = Long.valueOf(Constants.MIN_PERIOD_SECONDS);
        }
        if (offset == 0) {
            offset = Constants.MIN_PERIOD_SECONDS;
        }
        if(end == null) {
            end = 0L;
        }
        if(no_secs < 3600 * 24) {
            tables = ueCommunicationMetricsRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second",
                    end + " second", offset + " second", columns, false);
        } else {
            tables = ueCommunicationMetricsRepository.findAllInLastIntervalByFilterAndOffset(params, no_secs + " second",
                    end + " second", offset + " second", columns, true);
        }
        List<UeCommunication> res = new ArrayList<>();
        for (UeCommunicationTable table : tables) {
            if (table != null) {
                UeCommunication info = UeCommunication.fromMap(table.getData());
                if (table.getTime() != null) {
                    info.time(table.getTime().toInstant());
                }
                res.add(info);
            }
        }
        return res;
    }

    public boolean truncate() {
        try {
            nfLoadRepository.truncate();
            ueMobilityRepository.truncate();
            return true;
        } catch (Exception e) {
            NwdafSubApplication.getLogger().error("Error truncating metrics tables", e);
            return false;
        }
    }

    public List<OffsetDateTime> findAvailableHistoricMetricsTimeStamps(NwdafEvent.NwdafEventEnum event, Long start, Long end) {
        String startInterval = start != null ? start + " second" : "1 second";
        String endInterval = end != null ? end + " second" : "0 second";
        try {
        return switch (event) {
            case NF_LOAD -> nfLoadRepository.findAvailableHistoricMetricsTimeStamps(startInterval, endInterval);
            case UE_MOBILITY -> ueMobilityRepository.findAvailableHistoricMetricsTimeStamps(startInterval, endInterval);
            case UE_COMM -> ueCommunicationMetricsRepository.findAvailableHistoricMetricsTimeStamps(startInterval, endInterval);
            default -> null;
        };
        } catch (Exception e) {
            NwdafSubApplication.getLogger().error("Error finding available metrics timestamps", e);
            return null;
        }
    }

    public List<OffsetDateTime> findAvailableConcurrentMetricsTimeStamps(NwdafEvent.NwdafEventEnum event, Long start, Long end) {
        String startInterval = start != null ? start + " second" : "1 second";
        String endInterval = end != null ? end + " second" : "0 second";
        try {
            return switch (event) {
                case NF_LOAD -> nfLoadRepository.findAvailableMetricsTimeStamps(startInterval, endInterval);
                case UE_MOBILITY -> ueMobilityRepository.findAvailableMetricsTimeStamps(startInterval, endInterval);
                case UE_COMM ->
                        ueCommunicationMetricsRepository.findAvailableMetricsTimeStamps(startInterval, endInterval);
                default -> null;
            };
        } catch (Exception e) {
            NwdafSubApplication.getLogger().error("Error finding available metrics timestamps", e);
            return null;
        }
    }

    public List<OffsetDateTime> findAvailableConcurrentMetricsTimeStampsWithOffset(NwdafEvent.NwdafEventEnum event, Long start, Long end, Integer offset) {
        String startInterval = start != null ? start + " second" : "1 second";
        String endInterval = end != null ? end + " second" : "0 second";
        try {
            return switch (event) {
                case NF_LOAD -> nfLoadRepository.findAvailableMetricsTimeStamps(startInterval, endInterval, offset);
                case UE_MOBILITY -> ueMobilityRepository.findAvailableMetricsTimeStamps(startInterval, endInterval, offset);
                case UE_COMM ->
                        ueCommunicationMetricsRepository.findAvailableMetricsTimeStamps(startInterval, endInterval, offset);
                default -> null;
            };
        } catch (Exception e) {
            NwdafSubApplication.getLogger().error("Error finding available metrics timestamps", e);
            return null;
        }
    }

    private List<NfLoadLevelInformation> getNfLoadLevelInformationFromTable(List<NfLoadLevelInformationTable> tables) {
        List<NfLoadLevelInformation> res = new ArrayList<>();
        for (NfLoadLevelInformationTable table : tables) {
            if (table != null) {
                NfLoadLevelInformation info = NfLoadLevelInformation.fromMap(table.getData());
                info.setTime(table.getTime().toInstant());
                res.add(info);
            }
        }
        return res;
    }

    public List<PointUncertaintyCircleResult> getUeLocationInLastIntervalByFilterAndOffset(String params, Integer no_secs,
                                                                                           Integer end, Integer offset) {
        List<PointUncertaintyCircleResult> result;
        if (no_secs == null) {
            no_secs = Constants.MIN_PERIOD_SECONDS;
        }
        if (offset == 0) {
            offset = Constants.MIN_PERIOD_SECONDS;
        }
        if(end == null) {
            end = 0;
        }
        result = ueMobilityRepository.findAllUeLocationInLastIntervalByFilterAndOffset(params, no_secs + " second",
                end + " second", offset + " second");
        return result;
    }

    public OffsetDateTime findOldestConcurrentMetricsTimeStamp(NwdafEvent.NwdafEventEnum event) {
        return switch (event) {
            case NF_LOAD -> nfLoadRepository.findOldestTimeStamp();
            case UE_MOBILITY -> ueMobilityRepository.findOldestTimeStamp();
            case UE_COMM -> ueCommunicationMetricsRepository.findOldestTimeStamp();
            default -> null;
        };
    }

    public OffsetDateTime findOldestHistoricMetricsTimeStamp(NwdafEvent.NwdafEventEnum event) {
        return switch (event) {
            case NF_LOAD -> nfLoadRepository.findOldestHistoricTimeStamp();
            case UE_MOBILITY -> ueMobilityRepository.findOldestHistoricTimeStamp();
            case UE_COMM -> ueCommunicationMetricsRepository.findOldestHistoricTimeStamp();
            default -> null;
        };
    }
}