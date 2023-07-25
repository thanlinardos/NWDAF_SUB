DROP TABLE nf_load_metrics;
DROP TABLE ue_mobility_metrics;

CREATE EXTENSION IF NOT EXISTS timescaledb;
CREATE TABLE IF NOT EXISTS nf_load_metrics (
  time TIMESTAMPTZ,
  data JSONB,
  nfInstanceId UUID,
  nfSetId varchar(100)
);
SELECT create_hypertable('nf_load_metrics','time');

CREATE INDEX ix_data_time ON nf_load_metrics (data, time DESC, nfInstanceId);


CREATE TABLE IF NOT EXISTS ue_mobility_metrics (
  time TIMESTAMPTZ,
  data JSONB
);
SELECT create_hypertable('ue_mobility_metrics','time');

CREATE INDEX ix_data_time_ue_mobility_metrics ON ue_mobility_metrics (data, time DESC);