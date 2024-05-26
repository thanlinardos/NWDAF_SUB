package io.nwdaf.eventsubscription.service;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.model.KalmanFilterModel;
import io.nwdaf.eventsubscription.customModel.PointUncertaintyCircleResult;
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
            NwdafSubApplication.getLogger().error("Error in PytkalmanFilterModel = {KalmanFilterModel@20537} hon script execution", e);
        }
    }

    public List<PointUncertaintyCircleResult> testPredictPath(KalmanFilterModel kalmanFilterModel,
                                                              List<PointUncertaintyCircleResult> points,
                                                              DecimalFormat precisionFormat,
                                                              boolean returnSupi) {
        if (points.getFirst() == null || points.getFirst().getLatitude() == null || points.getFirst().getLongitude() == null)
            return new ArrayList<>();

        List<PointUncertaintyCircleResult> predictedPath = new ArrayList<>();
        predictedPath.add(points.getFirst());
        double initialUncertainty = points.getFirst().getUncertainty();

        for (int i = 1; i < points.size(); i++) {
            RealVector estimatedState = kalmanFilterModel.predictAndCorrect(points.get(i).getLongitude(), points.get(i).getLatitude(), 0);
            predictedPath.add(new PointUncertaintyCircleResult(
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(1))),
                    Double.parseDouble(precisionFormat.format(estimatedState.getEntry(0))),
                    initialUncertainty,
                    returnSupi ? points.get(i).getSupi() : null));
        }

        return predictedPath;
    }
}
