package io.nwdaf.eventsubscription.service;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.KalmanFilterModel;
import io.nwdaf.eventsubscription.repository.eventmetrics.entities.PointUncertaintyCircleResult;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
            NwdafSubApplication.getLogger().error("Error in Python script execution", e);
        }
    }

    public List<PointUncertaintyCircleResult> approximatePath(List<PointUncertaintyCircleResult> points, DecimalFormat precisionFormat) {

        List<PointUncertaintyCircleResult> approximatedPath = new ArrayList<>();

        KalmanFilterModel kalmanFilterModel = new KalmanFilterModel(points.getFirst().getLongitude(), points.getFirst().getLatitude(), points.getFirst().getUncertainty());

        for (PointUncertaintyCircleResult point : points) {
            RealVector estimatedState = kalmanFilterModel.predictAndCorrect(point.getLongitude(), point.getLatitude(), point.getUncertainty());
            approximatedPath.add(new PointUncertaintyCircleResult(
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(1))),
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(0))),
                    null));
        }

        return approximatedPath;
    }
}
