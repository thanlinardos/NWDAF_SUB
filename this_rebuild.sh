docker compose -f src/main/resources/compose_files/docker-compose.yml down nwdafSub &&
cd ../nwdaf_library &&
mvn -DskipTests install &&
cd ../NWDAF_SUB &&
mvn -DskipTests install &&
docker build . --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT &&
docker compose -f src/main/resources/compose_files/docker-compose.yml up nwdafSub -d &&
docker logs nwdafSub --follow