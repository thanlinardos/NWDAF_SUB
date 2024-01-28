package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.model.KalmanFilterModel;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.service.MetricsService;
import io.nwdaf.eventsubscription.service.PredictUeMobilityService;
import io.nwdaf.eventsubscription.utilities.BenchmarkUtil;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import static io.nwdaf.eventsubscription.utilities.ConvertUtil.setAccuracy;
import static io.nwdaf.eventsubscription.utilities.ConvertUtil.singlePrecisionFormat;

@RestController
public class ModelsController {
    private final PredictUeMobilityService predictUeMobilityService;
    private final MetricsService metricsService;

    public ModelsController(PredictUeMobilityService predictUeMobilityService, MetricsService metricsService) {
        this.predictUeMobilityService = predictUeMobilityService;
        this.metricsService = metricsService;
    }

    @GetMapping("/models/estimate-ue-path")
    public ResponseEntity<List<PointUncertaintyCircleResult>> estimatePath() {
        List<PointUncertaintyCircleResult> points = metricsService
                .getUeLocationInLastIntervalByFilterAndOffset(null, 600, 1);
        System.out.println("estimatedPath size = " + points.size());
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < points.size(); i++) {
            if(i % 3 != 0) continue;
            points.get(i).setLatitude(points.get(i).getLatitude() + random.nextDouble(2*1e-4));
            points.get(i).setLongitude(points.get(i).getLongitude() + random.nextDouble(2*1e-4));
        }
        return ResponseEntity.ok().body(points);
    }

    @GetMapping("/models/predict-ue-path")
    public ResponseEntity<List<PointUncertaintyCircleResult>> predictPath() {
        List<PointUncertaintyCircleResult> points = metricsService
                .getUeLocationInLastIntervalByFilterAndOffset("supi = '202010000000003'", 3600, 1);
        if (points == null || points.isEmpty()) return ResponseEntity.noContent().build();
        BenchmarkUtil benchmarkUtil = new BenchmarkUtil();
        benchmarkUtil.start();
        KalmanFilterModel kalmanFilterModel = new KalmanFilterModel(points.getFirst().getLongitude(), points.getFirst().getLatitude(), 1e-4, 0);
        List<PointUncertaintyCircleResult> predictedPath = predictUeMobilityService.testPredictPath(kalmanFilterModel, points, singlePrecisionFormat);
        benchmarkUtil.end();
        System.out.println("predictedPath took " + benchmarkUtil.toMillisStr());
        System.out.println("input estimatedPath size = " + points.size());
        System.out.println("predictedPath size = " + predictedPath.size());
        int start = Math.max(predictedPath.size() - 1000, 0);
        return ResponseEntity.ok().body(predictedPath.subList(start, predictedPath.size()));
    }
}
