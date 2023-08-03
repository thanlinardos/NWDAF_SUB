drop table "nnwdaf_notification";

create table if not exists "nnwdaf_notification"(
    id SERIAL PRIMARY KEY,
    notif jsonb
);

insert into "nnwdaf_notification" (notif) values ('{"eventNotifications":[{"event":null, "start":null, "expiry":null, "timeStampGen":null, "failNotifyCode":null, "rvWaitTime":null, "anaMetaInfo":null, "nfLoadLevelInfos":[{"nfType":null, "nfInstanceId":null, "nfSetId":null, "nfStatus":null, "nfCpuUsage":100, "nfMemoryUsage":null, "nfStorageUsage":null, "nfLoadLevelAverage":null, "nfLoadLevelpeak":null, "nfLoadAvgInAoi":null, "snssai":null, "confidence":null}], "nsiLoadLevelInfos":null, "sliceLoadLevelInfo":null, "svcExps":null, "qosSustainInfos":null, "ueComms":null, "ueMobs":null, "userDataCongInfos":null, "abnorBehavrs":null, "nwPerfs":null, "dnPerfInfos":null, "disperInfos":null, "redTransInfos":null, "wlanInfos":null, "smccExps":null}], "subscriptionId":205, "notifCorrId":null, "oldSubscriptionId":null}');
