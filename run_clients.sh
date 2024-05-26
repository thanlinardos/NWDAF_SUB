docker compose --profile dev -f ../nwdaf_sub_collector/NEF_emulator/docker-compose.yml up -d &&
docker compose -f dockprom/docker-compose.yml up -d &&
docker compose --env-file src/main/resources/compose_files/ips.env \
-f src/main/resources/compose_files/docker-compose.yml up -d \
nwdafSubClient nwdafSubClient2 nwdafSubClient3 nwdafSubClient4 nwdafSubClient5 nwdafSubCollector nwdafSubCollector2 &&
docker logs nwdafSubCollector --follow