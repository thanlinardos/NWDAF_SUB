package io.nwdaf.eventsubscription.model;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class KalmanFilterModel {

    private RealVector x; // State vector [x, y, v_x, v_y]
    private RealMatrix A; // State transition matrix
    private RealMatrix H; // Measurement matrix
    private RealMatrix Q; // Covariance matrix
    private RealMatrix R; // Measurement noise covariance matrix
    private RealMatrix B; // Control input matrix
    private RealMatrix P0; // Initial Measurement noise covariance matrix
    private KalmanFilter kalmanFilter;

    public KalmanFilterModel(double initialLongitude, double initialLatitude, double initialUncertainty) {
        // Initialize Kalman Filter parameters
        int stateDimension = 4; // Assuming 4D state space (x, y, v_x, v_y)
        int measurementDimension = 2; // Latitude and longitude are observed

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

    public RealVector predictAndCorrect(double longitude, double latitude, double uncertainty) {
        kalmanFilter.predict();
        kalmanFilter.correct(new ArrayRealVector(new double[]{longitude, latitude}));
        return kalmanFilter.getStateEstimationVector().getSubVector(0, 2);
    }
}
