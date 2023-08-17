#!/bin/bash

counter=8084
while [ $counter -le 8088 ]
do
echo client: $counter
docker compose -f 'C:/Users/thanl/Documents/eclipse-workspace-spring/NWDAF_SUB/src/main/resources/docker-compose-client.yml' up client_port=$counter
((counter++))
done

echo All done
