package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.customModel.CellLocationType;
import io.nwdaf.eventsubscription.customModel.LocationInfoWithSupi;
import io.nwdaf.eventsubscription.customModel.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.model.LocationInfo;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomEventMetricsRepository;
import io.nwdaf.eventsubscription.repository.eventmetrics.CustomUeMobilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static io.nwdaf.eventsubscription.utilities.OtherUtil.parseLocationInfo;

public class CustomUeMobilityRepositoryImpl implements CustomEventMetricsRepository<UeMobilityTable>, CustomUeMobilityRepository {

    @PersistenceContext(unitName = "eventmetricsEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<UeMobilityTable> findAllInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                        String end, String offset, String columns, Boolean historic) {
        if (params != null) {
            params = " and " + params;
        } else {
            params = "";
        }
        String table = historic ? " compressed_ue_mobility_metrics" : " ue_mobility_metrics";
        String query = """
                select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId, areaOfInterestId) 
                time_bucket(cast(:offset as interval), time) AS time , data, supi, intGroupId, areaOfInterestId
                 """ + columns + """
                 from """ + table + """
                 where time >= NOW() - cast(:no_secs as interval) 
                 and time <= NOW() - cast(:end as interval) 
                """ + params + """
                 GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId, areaOfInterestId;
                """;
        return entityManager.createNativeQuery(query, UeMobilityTable.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PointUncertaintyCircleResult> findAllUeLocationInLastIntervalByFilterAndOffset(String params, String no_secs,
                                                                                               String end, String offset, boolean returnSupi) {
        if (params != null && !params.isEmpty()) {
            params = " and " + params;
        } else {
            params = "";
        }
        String query = """
                select distinct on (time_bucket(cast(:offset as interval), time), supi, intGroupId, areaOfInterestId) cast(jsonb_path_query(
                    data, '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lat') as double precision) AS latitude,
                    cast(jsonb_path_query(data,
                    '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lon') as double precision) AS longitude,
                    cast(jsonb_path_query(data,
                    '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.uncertainty') as double precision) AS uncertainty
                """ + (returnSupi ? ", supi" : ", null as supi") + """
                     from ue_mobility_metrics where time >= NOW() - cast(:no_secs as interval)
                    and time <= NOW() - cast(:end as interval) 
                """ + params + """
                 GROUP BY time_bucket(cast(:offset as interval), time), time, data, supi, intGroupId, areaOfInterestId;
                """;
        return entityManager.createNativeQuery(query, PointUncertaintyCircleResult.class)
                .setParameter("offset", offset)
                .setParameter("no_secs", no_secs)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end) {
        String query = """
                select distinct date_trunc('second', time) as time from ue_mobility_metrics 
                where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
                order by time desc;
                """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableMetricsTimeStamps(String start, String end, Integer period) {
        String query = """
                select distinct (time_bucket(cast(:offset as interval), time)) as time from ue_mobility_metrics 
                where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
                order by time desc;
                """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OffsetDateTime> findAvailableHistoricMetricsTimeStamps(String start, String end) {
        String query = """
                select distinct time from compressed_ue_mobility_metrics 
                where time >= NOW() - cast(:start as interval) and time <= NOW() - cast(:end as interval) 
                order by time desc;
                """;
        return entityManager.createNativeQuery(query, OffsetDateTime.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public OffsetDateTime findOldestTimeStamp() {
        String query = """
                select distinct time from ue_mobility_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    public OffsetDateTime findOldestHistoricTimeStamp() {
        String query = """
                select distinct time from compressed_ue_mobility_metrics order by time limit 1;
                """;
        return (OffsetDateTime) entityManager.createNativeQuery(query, OffsetDateTime.class).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getSupis() {
        String query = """
                select distinct supi from ue_mobility_metrics;
                """;
        return entityManager.createNativeQuery(query).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getSupis(String groupId) {
        if (groupId == null) return List.of();
        String query = """
                select distinct supi from ue_mobility_metrics where intGroupId =
                """ + " '" + groupId + "';";
        return entityManager.createNativeQuery(query)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LocationInfo> getLocationInfos(String groupId, CellLocationType type, ObjectMapper objectMapper) {
        String query;

        if (type != null) {

            String typePath = switch (type) {
                case NR_LOCATION -> "nrLocation";
                case EUTRA_LOCATION -> "eutraLocation";
                case N3GA_LOCATION -> "n3gaLocation";
                case UTRA_LOCATION -> "utraLocation";
                case GERA_LOCATION -> "geraLocation";
            };

            query = """
                    select distinct locInfo
                    from ue_mobility_metrics, jsonb_array_elements(data->'locInfos') as locInfo
                    where time >= NOW() - cast('1 sec' as interval)
                    and intgroupid = '""" + groupId + "'" + """
                    and data->'locInfos' is not null
                    and locInfo->'loc'->'""" + typePath + "'" + """
                    is not null;
                    """;
        } else {
            query = """
                    select distinct locInfo from ue_mobility_metrics, jsonb_array_elements(data->'locInfos') as locInfo
                    where time >= NOW() - cast('1 sec' as interval)
                    and intgroupid = '""" + groupId + "'" + """
                    and data->'locInfos' is not null;
                    """;
        }

        List<LocationInfo> result = new ArrayList<LocationInfo>(entityManager.createNativeQuery(query)
                .getResultList()
                .stream().map(o -> {
                    try {
                        return objectMapper.readValue((String) o, LocationInfo.class);
                    } catch (IOException e) {
                        NwdafSubApplication.getLogger().error("Error parsing LocationInfo from database", e);
                        return null;
                    }
                }).toList());

        parseLocationInfo(result);

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LocationInfo> getLocationInfos(CellLocationType type, ObjectMapper objectMapper) {
        String query;

        if (type != null) {

            String typePath = switch (type) {
                case NR_LOCATION -> "nrLocation";
                case EUTRA_LOCATION -> "eutraLocation";
                case N3GA_LOCATION -> "n3gaLocation";
                case UTRA_LOCATION -> "utraLocation";
                case GERA_LOCATION -> "geraLocation";
            };

            query = """
                    select distinct locInfo
                    from ue_mobility_metrics, jsonb_array_elements(data->'locInfos') as locInfo
                    where time >= NOW() - cast('1 sec' as interval) and
                    data->'locInfos' is not null
                    and locInfo->'loc'->'""" + typePath + "'" + """
                    is not null;
                    """;
        } else {
            query = """
                    select distinct locInfo from ue_mobility_metrics, jsonb_array_elements(data->'locInfos') as locInfo
                    where time >= NOW() - cast('1 sec' as interval) and
                    data->'locInfos' is not null;
                    """;
        }

        List<LocationInfo> result = new ArrayList<LocationInfo>(entityManager.createNativeQuery(query)
                .getResultList()
                .stream().map(o -> {
                    try {
                        return objectMapper.readValue((String) o, LocationInfo.class);
                    } catch (IOException e) {
                        NwdafSubApplication.getLogger().error("Error parsing LocationInfo from database", e);
                        return null;
                    }
                }).toList());

        parseLocationInfo(result);

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LocationInfoWithSupi> getLocationInfosWithSupi(String groupId, Optional<String> supi, CellLocationType type, ObjectMapper objectMapper) {

        String typeQuery = Optional.of(type).map(t -> {
            String typePath = switch (t) {
                case NR_LOCATION -> "nrLocation";
                case EUTRA_LOCATION -> "eutraLocation";
                case N3GA_LOCATION -> "n3gaLocation";
                case UTRA_LOCATION -> "utraLocation";
                case GERA_LOCATION -> "geraLocation";
            };
            return " and locInfo->'loc'->'" + typePath + "' is not null";
        }).orElse("");

        String supiQuery = supi.map(s -> " and supi = '" + s + "'").orElse("");

        String groupIdQuery = groupId != null ? " and intGroupId = '" + groupId + "'" : "";

        String query = """
                select distinct locInfo, supi
                from ue_mobility_metrics, jsonb_array_elements(data->'locInfos') as locInfo
                where time >= NOW() - cast('1 sec' as interval)
                and data->'locInfos' is not null
                """ + groupIdQuery + typeQuery + supiQuery + ";";

        List<LocationInfoWithSupi> result = new ArrayList<LocationInfoWithSupi>(entityManager.createNativeQuery(query)
                .getResultList()
                .stream()
                .map(o -> {
                    try {
                        return new LocationInfoWithSupi((String) ((Object[]) o)[1], objectMapper.readValue((String) ((Object[]) o)[0], LocationInfo.class));
                    } catch (IOException e) {
                        NwdafSubApplication.getLogger().error("Error parsing LocationInfo from database", e);
                        return null;
                    }
                })
                .toList());

        parseLocationInfo(result);

        return result;
    }
}
