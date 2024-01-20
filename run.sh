docker compose -f src/main/resources/compose_files/docker-compose-db.yml up -d &&
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml up -d &&
docker compose --profile dev -f ../nwdaf_sub_collector/NEF_emulator/docker-compose.yml up -d &&
docker compose -f dockprom/docker-compose.yml up -d