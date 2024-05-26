package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.customModel.*;
import io.nwdaf.eventsubscription.kafka.KafkaNotifierLoadBalanceConsumer;
import io.nwdaf.eventsubscription.model.KalmanFilterModel;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.PredictUeMobilityService;
import io.nwdaf.eventsubscription.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.nwdaf.eventsubscription.controller.http.ValidateModelsRequest.validateUePathParameters;
import static io.nwdaf.eventsubscription.utilities.CheckUtil.isValidSupi;
import static io.nwdaf.eventsubscription.utilities.Constants.MAX_PERIOD_SECONDS;
import static io.nwdaf.eventsubscription.utilities.Constants.MIN_PERIOD_SECONDS;
import static io.nwdaf.eventsubscription.utilities.ConvertUtil.singlePrecisionFormat;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.parseListToFilterList;
import static io.nwdaf.eventsubscription.utilities.ParserUtil.parseQuerryFilter;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.notifier", havingValue = "true")
@RestController
@CrossOrigin
public class ModelsController {

    private final Logger log = LoggerFactory.getLogger(ModelsController.class);

    private final PredictUeMobilityService predictUeMobilityService;
    private final MetricsService metricsService;

    public ModelsController(PredictUeMobilityService predictUeMobilityService, MetricsService metricsService) {
        this.predictUeMobilityService = predictUeMobilityService;
        this.metricsService = metricsService;
    }

    @GetMapping("/models/supis")
    public ResponseEntity<List<String>> getSupis(@RequestParam Optional<NwdafEvent.NwdafEventEnum> event,
                                                 @RequestParam Optional<String> groupId) {
        List<String> supis = new ArrayList<>();
        if (groupId.isPresent() && !groupId.get().matches(Regex.group_id))
            throw new IllegalArgumentException("Invalid groupId = " + groupId);

        if (event.isPresent() && event.get() == NwdafEvent.NwdafEventEnum.UE_MOBILITY) {
            supis.addAll(metricsService.getMobilitySupis(groupId));
        } else if (event.isPresent() && event.get() == NwdafEvent.NwdafEventEnum.UE_COMM) {
            supis.addAll(metricsService.getCommunicationSupis(groupId));
        } else {
            supis.addAll(metricsService.getMobilitySupis(groupId));
            supis.addAll(metricsService.getCommunicationSupis(groupId));
        }

        if (supis.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(supis);
    }

    @GetMapping("/models/get-cell-based-UELocation")
    public ResponseEntity<List<CellBasedUELocation>> getCellBasedLocation(@RequestParam Optional<String> groupId, @RequestParam Optional<String> supi) {
        if (groupId.isPresent() && !groupId.get().matches(Regex.group_id)) {
            throw new IllegalArgumentException("Invalid groupId = " + groupId);
        }
        if (supi.isPresent() && !isValidSupi(supi.get())) {
            throw new IllegalArgumentException("Invalid supi = " + supi);
        }

        List<CellBasedUELocation> cells = new ArrayList<>(metricsService
                .getCellBasedLocations(groupId, supi, CellLocationType.NR_LOCATION));
        if (cells.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(cells);
    }

    @GetMapping("/models/get-cell-location")
    public ResponseEntity<List<CellLocation>> getCellLocation(@RequestParam Optional<String> groupId) {
        if (groupId.isPresent() && !groupId.get().matches(Regex.group_id)) {
            throw new IllegalArgumentException("Invalid groupId = " + groupId);
        }
        if (KafkaNotifierLoadBalanceConsumer.getNefScenario() == null) return ResponseEntity.noContent().build();

        List<NefCell> nefCells = KafkaNotifierLoadBalanceConsumer.getNefScenario().getCells();
        if (nefCells == null || nefCells.isEmpty()) return ResponseEntity.noContent().build();

        List<CellLocation> cellLocations = nefCells.stream().map(OtherUtil::parseCellLocationFromNefCell).toList();

        if (cellLocations.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(cellLocations);
    }

    @GetMapping("/models/get-ue-path")
    public ResponseEntity<List<PointUncertaintyCircleResult>> estimatePaths(
            @RequestParam Optional<Integer> pastOffsetSeconds,
            @RequestParam Optional<Integer> endPastOffsetSeconds,
            @RequestParam Optional<Integer> period,
            @RequestParam Optional<String> groupId
    ) {

        int pastOffset = pastOffsetSeconds.orElse(3600);
        int endPastOffset = endPastOffsetSeconds.orElse(0);
        int periodValue = period.orElse(1);
        validateUePathParameters(groupId, pastOffset, endPastOffset, periodValue);

        List<PointUncertaintyCircleResult> points;
        if (groupId.isPresent()) {
            List<String> supis = metricsService.getMobilitySupis(groupId);
            if (supis.isEmpty()) return ResponseEntity.noContent().build();
            points = metricsService
                    .getUeLocationInLastIntervalByFilterAndOffset(
                            parseQuerryFilter(parseListToFilterList(supis, "supi")),
                            pastOffset,
                            endPastOffset,
                            periodValue,
                            true);
        } else {
            points = metricsService
                    .getUeLocationInLastIntervalByFilterAndOffset(null, pastOffset, endPastOffset, periodValue, true);
        }
        log.debug("estimatedPath size = {}", points.size());
//        addPointsUncertainty(points, 2 * 1e-4);
        return ResponseEntity.ok().body(points);
    }

    @GetMapping("/models/get-ue-path/{supi}")
    public ResponseEntity<List<PointUncertaintyCircleResult>> estimatePath(
            @RequestParam Optional<Integer> pastOffsetSeconds,
            @RequestParam Optional<Integer> endPastOffsetSeconds,
            @RequestParam Optional<Integer> period,
            @PathVariable String supi) {

        if (supi == null || !isValidSupi(supi)) throw new IllegalArgumentException("Invalid supi = " + supi);

        int pastOffset = pastOffsetSeconds.orElse(3600);
        int endPastOffset = endPastOffsetSeconds.orElse(0);
        int periodValue = period.orElse(1);
        if (pastOffset < 0 || endPastOffset < 0 || periodValue < MIN_PERIOD_SECONDS || periodValue > MAX_PERIOD_SECONDS)
            throw new IllegalArgumentException(
                    "Invalid pastOffset = " + pastOffset
                            + " or endPastOffset = " + endPastOffset
                            + " or period = " + periodValue);

        List<PointUncertaintyCircleResult> points = metricsService
                .getUeLocationInLastIntervalByFilterAndOffset(
                        "supi = '" + supi + "'",
                        pastOffset,
                        endPastOffset,
                        periodValue,
                        false);
        log.debug("estimatedPath size = {}", points.size());
        return ResponseEntity.ok().body(points);
    }

    @GetMapping("/models/predict-ue-path/{supi}")
    public ResponseEntity<List<PointUncertaintyCircleResult>> predictPath(
            @PathVariable String supi,
            @RequestParam Optional<Integer> pastOffsetSeconds,
            @RequestParam Optional<Integer> endPastOffsetSeconds,
            @RequestParam Optional<Integer> period) {

        if (supi == null || !isValidSupi(supi)) throw new IllegalArgumentException("Invalid supi = " + supi);

        int pastOffset = pastOffsetSeconds.orElse(3600);
        int endPastOffset = endPastOffsetSeconds.orElse(0);
        int periodValue = period.orElse(1);
        if (pastOffset < 0 || endPastOffset < 0 || periodValue < MIN_PERIOD_SECONDS || periodValue > MAX_PERIOD_SECONDS)
            throw new IllegalArgumentException(
                    "Invalid pastOffset = " + pastOffset
                            + " or endPastOffset = " + endPastOffset
                            + " or period = " + periodValue);

        List<PointUncertaintyCircleResult> points = metricsService
                .getUeLocationInLastIntervalByFilterAndOffset(
                        "supi = '" + supi + "'",
                        pastOffset,
                        endPastOffset,
                        periodValue,
                        false);
        if (points == null || points.isEmpty()) return ResponseEntity.noContent().build();
        BenchmarkUtil benchmarkUtil = new BenchmarkUtil();
        benchmarkUtil.start();

        KalmanFilterModel kalmanFilterModel = new KalmanFilterModel(points.getFirst().getLongitude(), points.getFirst().getLatitude(), 2 * 1e-4, 0);
        List<PointUncertaintyCircleResult> predictedPath = predictUeMobilityService
                .testPredictPath(kalmanFilterModel, points, singlePrecisionFormat, false);

        log.debug("predictedPath took {}", benchmarkUtil.end().toMillisStr());
        log.debug("input estimatedPath size = {}", points.size());
        log.debug("predictedPath size = {}", predictedPath.size());

//        int start = Math.max(predictedPath.size() - 1000, 0);
//        return ResponseEntity.ok().body(predictedPath.subList(start, predictedPath.size()));
        return ResponseEntity.ok().body(predictedPath);
    }

    @GetMapping("/models/predict-ue-path")
    public ResponseEntity<List<PointUncertaintyCircleResult>> predictPaths(
            @RequestParam Optional<String> groupId,
            @RequestParam Optional<Integer> pastOffsetSeconds,
            @RequestParam Optional<Integer> endPastOffsetSeconds,
            @RequestParam Optional<Integer> period) throws Exception {

        int pastOffset = pastOffsetSeconds.orElse(3600);
        int endPastOffset = endPastOffsetSeconds.orElse(0);
        int periodValue = period.orElse(1);
        if (pastOffset < 0 || endPastOffset < 0 || periodValue < MIN_PERIOD_SECONDS || periodValue > MAX_PERIOD_SECONDS)
            throw new IllegalArgumentException(
                    "Invalid pastOffset = " + pastOffset
                    + " or endPastOffset = " + endPastOffset
                    + " or period = " + periodValue);

        List<String> supis = metricsService.getMobilitySupis(groupId);
        if (supis.isEmpty()) return ResponseEntity.noContent().build();

        for (String supi : supis) {
            if (!isValidSupi(supi)) {
                if (groupId.isPresent()) throw new Exception("Found invalid supi = " + supi + " for groupId = " + groupId);
                else throw new Exception("Found invalid supi = " + supi);
            };
        }

        List<PointUncertaintyCircleResult> totalPredictedPath = new ArrayList<>();
        int estimatedPathSize = 0;

        BenchmarkUtil benchmarkUtil = new BenchmarkUtil();
        benchmarkUtil.start();
        for (String supi : supis) {
            List<PointUncertaintyCircleResult> points = metricsService
                    .getUeLocationInLastIntervalByFilterAndOffset(
                            "supi = '" + supi + "'",
                            pastOffset,
                            endPastOffset,
                            periodValue,
                            true);
            if (points == null || points.isEmpty()) return ResponseEntity.noContent().build();
            estimatedPathSize += points.size();

            KalmanFilterModel kalmanFilterModel = new KalmanFilterModel(points.getFirst().getLongitude(), points.getFirst().getLatitude(), 2 * 1e-4, 0);
            List<PointUncertaintyCircleResult> predictedPath = predictUeMobilityService
                    .testPredictPath(kalmanFilterModel, points, singlePrecisionFormat, true);
            totalPredictedPath.addAll(predictedPath);
        }

        log.debug("[no_supis = {}] predictedPath took {}", supis.size(), benchmarkUtil.end().toMillisStr());
        log.debug("input estimatedPath size = {}", estimatedPathSize);
        log.debug("predictedPath size = {}", totalPredictedPath.size());

        int start = Math.max(totalPredictedPath.size() - 1000, 0);
        return ResponseEntity.ok().body(totalPredictedPath.subList(start, totalPredictedPath.size()));
    }
}
