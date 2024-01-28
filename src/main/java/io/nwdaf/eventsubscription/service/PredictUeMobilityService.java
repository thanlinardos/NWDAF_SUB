package io.nwdaf.eventsubscription.service;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.KalmanFilterModel;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.nwdaf.eventsubscription.utilities.ConvertUtil.setAccuracy;

@Service
public class PredictUeMobilityService {
    public void predictUePathFromScript() {
        try {
            String pythonScriptPath = "predict_ue_path.py";
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);

        } catch (InterruptedException | IOException e) {
            NwdafSubApplication.getLogger().error("Error in PytkalmanFilterModel = {KalmanFilterModel@20537} hon script execution", e);
        }
    }

    public List<PointUncertaintyCircleResult> testPredictPath(KalmanFilterModel kalmanFilterModel, List<PointUncertaintyCircleResult> points, DecimalFormat precisionFormat) {
        if (points.getFirst() == null || points.getFirst().getLatitude() == null || points.getFirst().getLongitude() == null)
            return new ArrayList<>();

        List<PointUncertaintyCircleResult> predictedPath = new ArrayList<>();
        predictedPath.add(points.getFirst());
        double initialUncertainty = points.getFirst().getUncertainty();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < points.size(); i++) {
            RealVector estimatedState;
            if (i % 3 == 0) {
                estimatedState = kalmanFilterModel
                        .predictAndCorrect(points.get(i).getLongitude() + random.nextDouble(2 * 1e-4),
                                points.get(i).getLatitude() + random.nextDouble(2 * 1e-4), 0);
            } else {
                estimatedState = kalmanFilterModel.predictAndCorrect(points.get(i).getLongitude(), points.get(i).getLatitude(), 0);
            }
            predictedPath.add(new PointUncertaintyCircleResult(
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(0))),
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(1))),
                    initialUncertainty));
        }

        return predictedPath;
    }
}
