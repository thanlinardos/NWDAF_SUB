drop table if exists public.nnwdaf_events_subscription;

create table if not exists public.nnwdaf_events_subscription(
    id SERIAL PRIMARY KEY,
    sub jsonb
);

-- CREATE DATABASE test
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'en_US.utf8'
--     LC_CTYPE = 'en_US.utf8'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;

\connect test

drop table if exists public."nnwdaf_notification";

CREATE EXTENSION IF NOT EXISTS timescaledb;

create table if not exists public."nnwdaf_notification"(
    time TIMESTAMPTZ,
    id UUID,
    notif jsonb
);

SELECT create_hypertable('nnwdaf_notification','time');
CREATE INDEX IF NOT EXISTS ix_data_time ON public.nnwdaf_notification (notif, time DESC, id);
SELECT add_retention_policy('nnwdaf_notification', INTERVAL '1 day');

-- insert into test.."nnwdaf_notification" (notif) values ('{"eventNotifications":[{"event":null, "start":null, "expiry":null, "timeStampGen":null, "failNotifyCode":null, "rvWaitTime":null, "anaMetaInfo":null, "nfLoadLevelInfos":[{"nfType":null, "nfInstanceId":null, "nfSetId":null, "nfStatus":null, "nfCpuUsage":100, "nfMemoryUsage":null, "nfStorageUsage":null, "nfLoadLevelAverage":null, "nfLoadLevelpeak":null, "nfLoadAvgInAoi":null, "snssai":null, "confidence":null}], "nsiLoadLevelInfos":null, "sliceLoadLevelInfo":null, "svcExps":null, "qosSustainInfos":null, "ueComms":null, "ueMobs":null, "userDataCongInfos":null, "abnorBehavrs":null, "nwPerfs":null, "dnPerfInfos":null, "disperInfos":null, "redTransInfos":null, "wlanInfos":null, "smccExps":null}], "subscriptionId":205, "notifCorrId":null, "oldSubscriptionId":null}');

CREATE EXTENSION IF NOT EXISTS pg_cron;
