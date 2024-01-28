docker compose -f src/main/resources/compose_files/docker-compose.yml down &&
./build.sh &&
./run_all.sh &&
docker logs nwdafSub --follow