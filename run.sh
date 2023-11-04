docker compose -f src/main/resources/compose_files/docker-compose-db.yml down &&
docker compose -f src/main/resources/compose_files/docker-compose-db.yml up -d &&
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml down &&
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml up -d &&
docker compose -f dockprom/docker-compose.yml down &&
docker compose -f dockprom/docker-compose.yml up -d