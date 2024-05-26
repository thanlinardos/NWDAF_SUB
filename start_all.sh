docker compose -f src/main/resources/compose_files/docker-compose-db.yml start &&
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml start &&
docker compose -f dockprom/docker-compose.yml start &&
docker compose --profile dev -f ../nwdaf_sub_collector/NEF_emulator/docker-compose.yml start &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up -d