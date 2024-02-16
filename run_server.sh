docker compose -f src/main/resources/compose_files/docker-compose-db.yml up -d &&
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml up -d &&
docker compose -f dockprom/docker-compose.yml up -d grafana
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSub &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d nwdafSubConsume &&
docker logs nwdafSubNotifier --follow