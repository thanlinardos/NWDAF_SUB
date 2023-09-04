drop table if exists postgres.public.nnwdaf_events_subscription;

create table if not exists postgres.public.nnwdaf_events_subscription(
    id SERIAL PRIMARY KEY,
    sub jsonb
);

CREATE DATABASE test
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\connect test

drop table if exists test.public."nnwdaf_notification";

create table if not exists test.public."nnwdaf_notification"(
    id SERIAL PRIMARY KEY,
    notif jsonb
);

-- insert into test.."nnwdaf_notification" (notif) values ('{"eventNotifications":[{"event":null, "start":null, "expiry":null, "timeStampGen":null, "failNotifyCode":null, "rvWaitTime":null, "anaMetaInfo":null, "nfLoadLevelInfos":[{"nfType":null, "nfInstanceId":null, "nfSetId":null, "nfStatus":null, "nfCpuUsage":100, "nfMemoryUsage":null, "nfStorageUsage":null, "nfLoadLevelAverage":null, "nfLoadLevelpeak":null, "nfLoadAvgInAoi":null, "snssai":null, "confidence":null}], "nsiLoadLevelInfos":null, "sliceLoadLevelInfo":null, "svcExps":null, "qosSustainInfos":null, "ueComms":null, "ueMobs":null, "userDataCongInfos":null, "abnorBehavrs":null, "nwPerfs":null, "dnPerfInfos":null, "disperInfos":null, "redTransInfos":null, "wlanInfos":null, "smccExps":null}], "subscriptionId":205, "notifCorrId":null, "oldSubscriptionId":null}');
