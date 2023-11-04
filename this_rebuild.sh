mvn -DskipTests install &&
docker build . --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT &&
docker restart nwdafSub &&
docker logs nwdafSub --follow