mvn -DskipTests clean install
cd ./nwdaf_sub_collector
# mvn --no-transfer-progress clean package native:compile -Pnative -DSkiptests
cd -
sudo docker build ./NWDAF_SUB --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT
sudo docker build ./NWDAF_SUB_CLIENT --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT
sudo docker build ./nwdaf_sub_collector --tag thanlinardos/nwdaf_sub_collector:0.0.2-SNAPSHOT
sudo docker compose -f NWDAF_SUB/src/main/resources/compose_files/docker-compose.yml down
sudo docker compose -f NWDAF_SUB/src/main/resources/compose_files/docker-compose.yml up -d
sudo docker compose -f NWDAF_SUB/dockprom/docker-compose.yml down
sudo docker compose -f NWDAF_SUB/dockprom/docker-compose.yml up -d
