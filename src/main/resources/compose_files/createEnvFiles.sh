#!/bin/bash

# shellcheck disable=SC2016
echo 'SPRING.DATASOURCE.NAME: eventsubscription
SPRING.DATASOURCE.URL: jdbc:postgresql://db:5434/postgres?currentSchema=public
SPRING.DATASOURCE.USERNAME: "${POSTGRES_USER}"
SPRING.DATASOURCE.PASSWORD: "${POSTGRES_PASSWORD}"
EVENTNOTIFICATION.DATASOURCE.NAME: eventnotification
EVENTNOTIFICATION.DATASOURCE.URL: jdbc:postgresql://db:5434/test?currentSchema=public
EVENTNOTIFICATION.DATASOURCE.USERNAME: "${POSTGRES_USER}"
EVENTNOTIFICATION.DATASOURCE.PASSWORD: "${POSTGRES_PASSWORD}"
EVENTMETRICS.DATASOURCE.NAME: eventmetrics
EVENTMETRICS.DATASOURCE.URL: jdbc:postgresql://timescaledb:5433/metrics?currentSchema=public
EVENTMETRICS.DATASOURCE.USERNAME: "${POSTGRES_USER}"
EVENTMETRICS.DATASOURCE.PASSWORD: "${POSTGRES_PASSWORD}"' > db.env

echo 'client_port=10000
server_port=8081
server_host=nwdafSub
ext_server_host=nwdafSub
client_host=nwdafSubClient
ext_client_host=nwdafSubClient
client_max_port=10025
server_max_port=8089
c=1

server_host2=nwdafSub2
server_port2=8079
server_host3=nwdafSub3
server_port3=8078

server_consume_host=nwdafSubConsume
server_consume_port=8082
server_consume2_host=nwdafSubConsume2
server_consume2_port=8083
server_consume3_host=nwdafSubConsume3
server_consume3_port=8085

number_of_clients=20
client_port2=10001
client_host2=nwdafSubClient2
client_port3=10002
client_host3=nwdafSubClient3
client_port4=10003
client_host4=nwdafSubClient4
client_port5=10004
client_host5=nwdafSubClient5

default_host=localhost

POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

prom_host=localhost
prom_port=9090

collector_host=nwdafSubCollector
collector_port=8091

collector_host2=nwdafSubCollector
collector_port2=8092

collector_host3=nwdafSubCollector
collector_port3=8093

kafka1_host=kafka1
kafka1_ext_host=kafka1
kafka1_port=39092

kafka2_host=kafka2
kafka2_ext_host=kafka2
kafka2_port=39093

kafka3_host=kafka3
kafka3_ext_host=kafka3
kafka3_port=39095

redis_host=redis

nef_host=3gppnef
nef_port=4443
nef_username=admin@my-email.com
nef_password=pass
nef_group_id=A5F799B4-732-26-eb6bA8f1cF12

TIMEZONE=EET' > .env

echo '# server_host => replace with server host ip
# client_host => replace with client host ip
' > ips.env

cat .env >> ips.env

cat .env > ../../../../../nwdaf_sub_collector/.env

chmod +x ips.env
chmod +x db.env
chmod +x .env
chmod +x ../../../../../nwdaf_sub_collector/.env