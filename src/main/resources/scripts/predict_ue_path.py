import geopandas as gpd
from shapely.geometry import Point
import numpy as np
import pandas as pd
from filterpy.kalman import KalmanFilter
import psycopg2
import matplotlib.pyplot as plt
import time


def fetch_data_from_database():
    # Connect to the PostgreSQL database
    connection = psycopg2.connect(
        database="metrics",
        user="postgres",
        password="password",
        host="localhost",
        port="5433",
    )

    # Replace 'your_table_name' with the actual name of your table
    query = ("select distinct on (time_bucket(cast('1 second' as interval), time), supi, intGroupId) jsonb_path_query("
             "data, '$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lat') AS latitude, jsonb_path_query(data, "
             "'$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.point.lon') AS longitude, jsonb_path_query(data,"
             "'$.locInfos[0].loc.nrLocation.pointUncertaintyCircle.uncertainty') AS uncertainty "
             "from ue_mobility_metrics where "
             "time > NOW() - cast('1 hour' as interval) GROUP BY time_bucket(cast('1 second' as interval), time), "
             "time, data, supi, intGroupId;")
    data = pd.read_sql_query(query, connection)
    print(data)

    # Close the database connection
    connection.close()

    return data


def kalman_filter(data):
    # Create a Kalman Filter with state_size=4 (x, y, x_velocity, y_velocity)
    kf = KalmanFilter(dim_x=4, dim_z=2)

    # Initial state [x, y, x_velocity, y_velocity]
    kf.x = np.array([data['longitude'].iloc[0], data['latitude'].iloc[0], 0, 0])

    # State transition matrix
    kf.F = np.array([[1, 0, 1, 0],
                     [0, 1, 0, 1],
                     [0, 0, 1, 0],
                     [0, 0, 0, 1]])

    # Measurement matrix
    kf.H = np.array([[1, 0, 0, 0],
                     [0, 1, 0, 0]])

    # Process noise covariance
    kf.P *= 1e3

    # Measurement noise covariance
    kf.R = np.diag([data['uncertainty'].iloc[0] ** 2, data['uncertainty'].iloc[0] ** 2])

    # Initialize filtered positions
    filtered_positions = np.zeros((len(data), 2))

    # Update the Kalman Filter with each measurement
    for i in range(len(data)):
        measurement = np.array([data['longitude'].iloc[i], data['latitude'].iloc[i]])
        kf.predict()
        kf.update(measurement)
        filtered_positions[i, :] = kf.x[:2]

    return filtered_positions


def timing_decorator(func):
    def wrapper(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        elapsed_time = end_time - start_time
        print(f"{func.__name__} took {elapsed_time} seconds to run")
        return result

    return wrapper


@timing_decorator
def main():
    # Fetch data from the PostgreSQL database
    data = fetch_data_from_database()

    # Apply Kalman filter to predict the path
    filtered_positions = kalman_filter(data)

    # Create a GeoDataFrame for plotting
    geometry = [Point(lon, lat) for lon, lat in zip(filtered_positions[:, 0], filtered_positions[:, 1])]
    gdf = gpd.GeoDataFrame(geometry=geometry, crs="EPSG:4326")

    # Plot the predicted path
    gdf.plot(marker='o', color='red', markersize=5)
    gpd.GeoSeries([Point(lon, lat) for lon, lat in zip(data['longitude'], data['latitude'])]).plot(marker='o',
                                                                                                   color='blue',
                                                                                                   markersize=5)
    plt.show()


if __name__ == "__main__":
    main()
