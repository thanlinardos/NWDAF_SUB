package io.nwdaf.eventsubscription.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

import static org.apache.lucene.spatial.util.GeoProjectionUtils.enuToLLA;
import static org.apache.lucene.spatial.util.GeoProjectionUtils.llaToENU;

public class KalmanFilterModel {

    private RealVector x; // State vector [x, y, v_x, v_y]
    private RealMatrix A; // State transition matrix
    private RealMatrix H; // Measurement matrix
    private RealMatrix Q; // Covariance matrix
    private RealMatrix R; // Measurement noise covariance matrix
    private RealMatrix B; // Control input matrix
    private RealMatrix P0; // Initial Measurement noise covariance matrix
    @Getter
    @Setter
    private KalmanFilter kalmanFilter;
    private List<Double> referencePointGeodetic;

    public KalmanFilterModel(double initialLongitude, double initialLatitude, double initialUncertainty, double initialAltitude) {
        // Initialize Kalman Filter parameters
        int stateDimension = 4; // Assuming 4D state space (x, y, v_x, v_y)
        int measurementDimension = 2; // Latitude and longitude are observed
        referencePointGeodetic = List.of(initialLatitude, initialLongitude, initialAltitude);

        // Initial state [x, y, v_x, v_y]
        x = new ArrayRealVector(new double[]{initialLongitude, initialLatitude, 0, 0});

        // State transition matrix
        A = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        // Measurement matrix
        H = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0}
        });

        // Covariance matrix
        Q = MatrixUtils.createRealIdentityMatrix(stateDimension).scalarMultiply(1e3);

        // Measurement noise covariance matrix
        R = new Array2DRowRealMatrix(new double[][]{
                {Math.pow(initialUncertainty, 2), 0d},
                {0d, Math.pow(initialUncertainty, 2)}
        });

        // Initial Measurement noise covariance matrix
//        P0 = new Array2DRowRealMatrix(new double[][] {
//                { 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d },
//                { 1d, 1d, 1d, 1d }
//        });

        ProcessModel processModel = new DefaultProcessModel(A, B, Q, x, P0);
        MeasurementModel measurementModel = new DefaultMeasurementModel(H, R);
        kalmanFilter = new KalmanFilter(processModel, measurementModel);
    }

    public RealVector predictAndCorrect(double longitude, double latitude, double altitude) {
        double[] result = new double[3];
        result = llaToENU(latitude, longitude, altitude, referencePointGeodetic.get(1), referencePointGeodetic.get(0), referencePointGeodetic.get(2), result);

        kalmanFilter.predict();
        kalmanFilter.correct(new ArrayRealVector(new double[]{result[0], result[1]}));
        RealVector currentEstimate = kalmanFilter.getStateEstimationVector().getSubVector(0, 2);
        double[] prediction = new double[3];
        prediction = enuToLLA(currentEstimate.getEntry(0), currentEstimate.getEntry(1), currentEstimate.getEntry(2), referencePointGeodetic.get(1), referencePointGeodetic.get(0), referencePointGeodetic.get(2), prediction);
        return new ArrayRealVector(new double[]{prediction[1], prediction[0]});
    }
}
