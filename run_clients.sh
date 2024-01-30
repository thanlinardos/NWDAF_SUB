docker compose --profile dev -f ../nwdaf_sub_collector/NEF_emulator/docker-compose.yml up -d &&
docker compose -f dockprom/docker-compose.yml up -d &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubClient &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubClient2 &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubClient3 &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubClient4 &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubClient5 &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubCollector &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubCollector2 &&
docker logs nwdafSubCollector --follow