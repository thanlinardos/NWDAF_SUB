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

DROP PROCEDURE IF EXISTS public.delete_old_notifications;

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
SELECT add_retention_policy('nnwdaf_notification', INTERVAL '30 minute');

CREATE OR REPLACE PROCEDURE public.delete_old_notifications(job_id INT,
                                                                           config JSONB DEFAULT '{"pastOffset":"30 minute"}')
    LANGUAGE plpgsql AS
$$
BEGIN
    RAISE NOTICE 'Executing delete_old_notifications job % with config %', job_id, config;
    DELETE FROM public."nnwdaf_notification" WHERE time < NOW() - cast(config ->> 'pastOffset' as interval);
END
$$;

-- CALL delete_old_notifications('30 minute');
SELECT add_job('delete_old_notifications', '10 minute',
               config => '{"pastOffset":"30 minute"}');