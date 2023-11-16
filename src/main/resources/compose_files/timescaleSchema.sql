\connect metrics;

DROP MATERIALIZED VIEW IF EXISTS metrics.public.compressed_nf_load_metrics_hour CASCADE;
DROP MATERIALIZED VIEW IF EXISTS metrics.public.compressed_ue_mobility_metrics_hour CASCADE;
DROP MATERIALIZED VIEW IF EXISTS metrics.public.compressed_ue_communication_metrics_hour CASCADE;

DROP MATERIALIZED VIEW IF EXISTS metrics.public.nf_load_metrics_minute CASCADE;
DROP MATERIALIZED VIEW IF EXISTS metrics.public.ue_mobility_metrics_minute CASCADE;
DROP MATERIALIZED VIEW IF EXISTS metrics.public.ue_communication_metrics_minute CASCADE;

DROP TABLE IF EXISTS metrics.public.compressed_nf_load_metrics;
DROP TABLE IF EXISTS metrics.public.compressed_ue_mobility_metrics;
DROP TABLE IF EXISTS metrics.public.compressed_ue_communication_metrics;

DROP TABLE IF EXISTS metrics.public.nf_load_metrics;
DROP TABLE IF EXISTS metrics.public.ue_mobility_metrics;
DROP TABLE IF EXISTS metrics.public.ue_communication_metrics;

CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE IF NOT EXISTS metrics.public.nf_load_metrics (
  time TIMESTAMPTZ,
  data JSONB,
  nfInstanceId UUID,
  nfSetId varchar(100),
  nfCpuUsage int,
  nfMemoryUsage int,
  nfStorageUsage int,
  nfLoadLevelAverage int,
  nfLoadLevelpeak int,
  nfLoadAvgInAoi int,
  areaOfInterestId UUID
);
SELECT create_hypertable('nf_load_metrics','time');

CREATE INDEX IF NOT EXISTS ix_data_time ON metrics.public.nf_load_metrics (data, time DESC, nfInstanceId);
SELECT add_retention_policy('nf_load_metrics', INTERVAL '1 day');


CREATE TABLE IF NOT EXISTS metrics.public.ue_mobility_metrics (
  time TIMESTAMPTZ,
  data JSONB,
  supi varchar(100),
  intGroupId varchar(100)
);
SELECT create_hypertable('ue_mobility_metrics','time');

CREATE INDEX IF NOT EXISTS ix_data_time_ue_mobility_metrics ON metrics.public.ue_mobility_metrics (data, time DESC, supi);
SELECT add_retention_policy('ue_mobility_metrics', INTERVAL '1 day');

CREATE TABLE IF NOT EXISTS metrics.public.ue_communication_metrics (
                                                                  time TIMESTAMPTZ,
                                                                  data JSONB,
                                                                  supi varchar(100),
                                                                  intGroupId varchar(100),
                                                                  areaOfInterestId UUID
    );
SELECT create_hypertable('ue_communication_metrics','time');

CREATE INDEX IF NOT EXISTS ix_data_time_ue_communication_metrics ON metrics.public.ue_communication_metrics (data, time DESC, supi);
SELECT add_retention_policy('ue_communication_metrics', INTERVAL '1 day');

-- 1 HOUR VIEWS
CREATE MATERIALIZED VIEW nf_load_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data , nfInstanceId, nfSetId,
    CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,
    CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,
    CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,
    CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,
    CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,
    CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi,
    areaOfInterestId from nf_load_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;

SELECT add_continuous_aggregate_policy('nf_load_metrics_hour',
                                       start_offset => INTERVAL '1 day',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '5 minute');

CREATE MATERIALIZED VIEW ue_mobility_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data, supi, intGroupId
from ue_mobility_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, supi, intGroupId;

SELECT add_continuous_aggregate_policy('ue_mobility_metrics_hour',
                                       start_offset => INTERVAL '1 day',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '5 minute');

CREATE MATERIALIZED VIEW ue_communication_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data, supi, intGroupId,
 areaOfInterestId from ue_communication_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, supi, intGroupId,
 areaOfInterestId;

SELECT add_continuous_aggregate_policy('ue_communication_metrics_hour',
                                       start_offset => INTERVAL '1 day',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '5 minute');

-- COMPRESSED TABLES (1 MIN)
CREATE TABLE IF NOT EXISTS metrics.public.compressed_nf_load_metrics (
                                                              time TIMESTAMPTZ,
                                                              data JSONB,
                                                              nfInstanceId UUID,
                                                              nfSetId varchar(100),
                                                              nfCpuUsage int,
                                                              nfMemoryUsage int,
                                                              nfStorageUsage int,
                                                              nfLoadLevelAverage int,
                                                              nfLoadLevelpeak int,
                                                              nfLoadAvgInAoi int,
                                                              areaOfInterestId UUID
);
SELECT create_hypertable('compressed_nf_load_metrics','time');
CREATE INDEX IF NOT EXISTS ix_data_time_compressed_nf_load_metrics ON metrics.public.compressed_nf_load_metrics (data, time DESC, nfInstanceId);

CREATE TABLE IF NOT EXISTS metrics.public.compressed_ue_mobility_metrics (
                                                                  time TIMESTAMPTZ,
                                                                  data JSONB,
                                                                  supi varchar(100),
                                                                  intGroupId varchar(100)
);
SELECT create_hypertable('compressed_ue_mobility_metrics','time');
CREATE INDEX IF NOT EXISTS ix_data_time_compressed_ue_mobility_metrics ON metrics.public.compressed_ue_mobility_metrics (data, time DESC, supi);

CREATE TABLE IF NOT EXISTS metrics.public.compressed_ue_communication_metrics (
                                                                       time TIMESTAMPTZ,
                                                                       data JSONB,
                                                                       supi varchar(100),
                                                                       intGroupId varchar(100),
                                                                       areaOfInterestId UUID
);
SELECT create_hypertable('compressed_ue_communication_metrics','time');
CREATE INDEX IF NOT EXISTS ix_data_time_compressed_ue_communication_metrics ON metrics.public.compressed_ue_communication_metrics (data, time DESC, supi);

-- COMPRESSED TABLE VIEWS (1 HOUR)
CREATE MATERIALIZED VIEW compressed_nf_load_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data , nfInstanceId, nfSetId,
       CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,
       CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,
       CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,
       CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,
       CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,
       CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi,
       areaOfInterestId from compressed_nf_load_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;

SELECT add_continuous_aggregate_policy('compressed_nf_load_metrics_hour',
                                       start_offset => INTERVAL '1 month',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '10 minute');

CREATE MATERIALIZED VIEW compressed_ue_mobility_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data, supi, intGroupId
from compressed_ue_mobility_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, supi, intGroupId;

SELECT add_continuous_aggregate_policy('compressed_ue_mobility_metrics_hour',
                                       start_offset => INTERVAL '1 month',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '10 minute');

CREATE MATERIALIZED VIEW compressed_ue_communication_metrics_hour
            WITH (timescaledb.continuous) AS
select time_bucket(cast('1 hour' as interval), time) AS time , data, supi, intGroupId, areaofinterestid
from compressed_ue_communication_metrics
GROUP BY time_bucket(cast('1 hour' as interval), time), time, data, supi, intGroupId, areaofinterestid;

SELECT add_continuous_aggregate_policy('compressed_ue_communication_metrics_hour',
                                       start_offset => INTERVAL '1 month',
                                       end_offset => INTERVAL '1 hour',
                                       schedule_interval => INTERVAL '10 minute');

CREATE EXTENSION IF NOT EXISTS pg_cron;
-- 1 MINUTE JOBS
SELECT cron.schedule('*/10 * * * *', $$
INSERT INTO metrics.public.compressed_nf_load_metrics
select distinct on (time_bucket(cast('1 minute' as interval), time), nfInstanceId, nfSetId)
    time_bucket(cast('1 minute' as interval), time) AS time , data , nfInstanceId, nfSetId,
    CAST(ROUND(AVG(CAST(data->>'nfCpuUsage' as numeric))) as integer) AS nfCpuUsage,
    CAST(ROUND(AVG(CAST(data->>'nfMemoryUsage' as numeric))) as integer) AS nfMemoryUsage,
    CAST(ROUND(AVG(CAST(data->>'nfStorageUsage' as numeric))) as integer) AS nfStorageUsage,
    CAST(ROUND(AVG(CAST(data->>'nfLoadLevelAverage' as numeric))) as integer) AS nfLoadLevelAverage,
    CAST(ROUND(MAX(CAST(data->>'nfLoadLevelpeak' as numeric))) as integer) AS nfLoadLevelpeak,
    CAST(ROUND(AVG(CAST(data->>'nfLoadAvgInAoi' as numeric))) as integer) AS nfLoadAvgInAoi,
    areaOfInterestId from nf_load_metrics
    where time > NOW() - cast('10 minute' as interval)
GROUP BY time_bucket(cast('1 minute' as interval), time), time, data, nfInstanceId, nfSetId, areaofinterestid;
$$);

SELECT cron.schedule('*/10 * * * *', $$
INSERT INTO metrics.public.compressed_ue_mobility_metrics
select distinct on (time_bucket(cast('1 minute' as interval), time), supi, intGroupId)
    time_bucket(cast('1 minute' as interval), time) AS time , data, supi, intGroupId
from ue_mobility_metrics
where time > NOW() - cast('10 minute' as interval)
GROUP BY time_bucket(cast('1 minute' as interval), time), time, data, supi, intGroupId;
$$);

SELECT cron.schedule('*/10 * * * *', $$
INSERT INTO metrics.public.compressed_ue_communication_metrics
select distinct on (time_bucket(cast('1 minute' as interval), time), supi, intGroupId)
    time_bucket(cast('1 minute' as interval), time) AS time , data, supi, intGroupId, areaOfInterestId
from ue_communication_metrics
where time > NOW() - cast('10 minute' as interval)
GROUP BY time_bucket(cast('1 minute' as interval), time), time, data, supi, intGroupId, areaOfInterestId;
$$);