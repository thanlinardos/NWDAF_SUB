mvn -DskipTests install
cd ./nwdaf_sub_collector
# mvn --no-transfer-progress clean package native:compile -Pnative -DSkiptests
cd -
docker build ./NWDAF_SUB --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT
docker build ./NWDAF_SUB_CLIENT --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT
docker build ./nwdaf_sub_collector --tag thanlinardos/nwdaf_sub_collector:0.0.1-SNAPSHOT
