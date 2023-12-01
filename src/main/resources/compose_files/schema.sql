\connect postgres

drop table if exists public.nnwdaf_events_subscription;

create table if not exists public.nnwdaf_events_subscription
(
    id  SERIAL PRIMARY KEY,
    sub jsonb
);


\connect test

ALTER SYSTEM SET shared_buffers = '2048MB';
ALTER SYSTEM SET work_mem = '19MB';
ALTER SYSTEM SET temp_buffers = '1MB';
ALTER SYSTEM SET max_connections = 100;

drop index if exists ix_data_time;
drop table if exists public."nnwdaf_notification";

CREATE EXTENSION IF NOT EXISTS timescaledb;

create table if not exists public."nnwdaf_notification"
(
    time  TIMESTAMPTZ NOT NULL,
    id    UUID NOT NULL,
    notif jsonb,
    notif_ref UUID
);

SELECT create_hypertable('nnwdaf_notification', 'time');
-- CREATE INDEX IF NOT EXISTS ix_data_time ON public.nnwdaf_notification (notif, time DESC, id);
SELECT add_retention_policy('nnwdaf_notification', INTERVAL '3 hour');

-- insert into test.."nnwdaf_notification" (notif) values ('{"eventNotifications":[{"event":null, "start":null, "expiry":null, "timeStampGen":null, "failNotifyCode":null, "rvWaitTime":null, "anaMetaInfo":null, "nfLoadLevelInfos":[{"nfType":null, "nfInstanceId":null, "nfSetId":null, "nfStatus":null, "nfCpuUsage":100, "nfMemoryUsage":null, "nfStorageUsage":null, "nfLoadLevelAverage":null, "nfLoadLevelpeak":null, "nfLoadAvgInAoi":null, "snssai":null, "confidence":null}], "nsiLoadLevelInfos":null, "sliceLoadLevelInfo":null, "svcExps":null, "qosSustainInfos":null, "ueComms":null, "ueMobs":null, "userDataCongInfos":null, "abnorBehavrs":null, "nwPerfs":null, "dnPerfInfos":null, "disperInfos":null, "redTransInfos":null, "wlanInfos":null, "smccExps":null}], "subscriptionId":205, "notifCorrId":null, "oldSubscriptionId":null}');
