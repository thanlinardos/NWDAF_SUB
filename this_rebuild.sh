docker compose -f src/main/resources/compose_files/docker-compose.yml down nwdafSub &&
docker compose -f src/main/resources/compose_files/docker-compose.yml down nwdafSubConsume &&
docker compose -f src/main/resources/compose_files/docker-compose.yml down nwdafSubConsume2 &&
docker compose -f src/main/resources/compose_files/docker-compose.yml down nwdafSubConsume3 &&
cd ../nwdaf_library &&
mvn -DskipTests install &&
cd ../NWDAF_SUB &&
mvn -DskipTests install &&
docker build . --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up nwdafSubConsume -d &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up nwdafSubConsume2 -d &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up nwdafSubConsume3 -d &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up nwdafSub -d &&
docker logs nwdafSubNotifier --follow