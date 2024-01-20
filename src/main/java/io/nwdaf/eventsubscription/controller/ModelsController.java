package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.PredictUeMobilityService;
import io.nwdaf.eventsubscription.utilities.BenchmarkUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.nwdaf.eventsubscription.utilities.ConvertUtil.singlePrecisionFormat;

@RestController
public class ModelsController {
    private final PredictUeMobilityService predictUeMobilityService;
    private final MetricsService metricsService;

    public ModelsController(PredictUeMobilityService predictUeMobilityService, MetricsService metricsService) {
        this.predictUeMobilityService = predictUeMobilityService;
        this.metricsService = metricsService;
    }

    @GetMapping("/models/predict-ue-path")
    public ResponseEntity<List<PointUncertaintyCircleResult>> predictUePath() {
        List<PointUncertaintyCircleResult> points = metricsService
                .getUeLocationInLastIntervalByFilterAndOffset(null, 600, 1);
        BenchmarkUtil benchmarkUtil = new BenchmarkUtil();
        benchmarkUtil.start();
        List<PointUncertaintyCircleResult> estimatedPath = predictUeMobilityService.approximatePath(points, singlePrecisionFormat);
        benchmarkUtil.end();
        System.out.println("approximatePath took " + benchmarkUtil.toMillisStr());
        System.out.println("estimatedPath size = " + estimatedPath.size());
        return ResponseEntity.ok().body(estimatedPath);
    }
}
