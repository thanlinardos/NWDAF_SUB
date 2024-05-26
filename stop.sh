docker compose -f src/main/resources/compose_files/docker-compose.yml down
docker compose -f src/main/resources/compose_files/docker-compose-db.yml stop
docker compose -f src/main/resources/compose_files/docker-compose-kafka.yml stop
docker compose --profile dev -f ../nwdaf_sub_collector/NEF_emulator/docker-compose.yml stop
docker compose -f dockprom/docker-compose.yml stop