drop table nnwdaf_notification;

create table if not exists nnwdaf_notification(
    id SERIAL PRIMARY KEY,
    notif jsonb
);

