#!/bin/bash

# Check if .env file exists and source it to load environment variables
if [ -f ../compose_files/.env ]; then
    source ../compose_files/.env
fi
# Define the number of clients you want to generate
num_clients=$1
if [[ -n "$1" ]]
then
    num_clients=$1
elif [[ -n "$number_of_clients" ]]
then
    num_clients=$number_of_clients
else
    num_clients=5
fi
server=${server_host:-"nwdafSub"}
ext_server=${ext_server_host:-"127.0.0.1"}
kafka1=${kafka1_host:-"kafka1"}
kafka2=${kafka2_host:-"kafka2"}
kafka3=${kafka3_host:-"kafka3"}
client=${client_host:-"nwdafSubClient"}
ext_client=${ext_client_host:-"127.0.0.1"}
redis=${redis_host:-"redis"}
nef=${nef_host:-"3gppnef"}
# Loop through and generate host names and write them to hosts & sanHosts.txt
echo "127.0.0.1 $kafka1" >> hosts
echo "127.0.0.1 $kafka2" >> hosts
echo "127.0.0.1 $kafka3" >> hosts
echo "127.0.0.1 $server" >> hosts
echo "127.0.0.1 $client" >> hosts
echo "127.0.0.1 $redis" >> hosts
echo "127.0.0.1 $nef" >> hosts
echo "127.0.0.1" >> sanHosts.txt
echo "$server" >> sanHosts.txt
echo "$ext_server" >> sanHosts.txt
echo "$ext_client" >> sanHosts.txt
echo "localhost" >> sanHosts.txt

echo "localhost" >> sanClientHosts.txt
echo "127.0.0.1" >> sanClientHosts.txt
echo "$client" >> sanClientHosts.txt
echo "$ext_server" >> sanClientHosts.txt
echo "$ext_client" >> sanClientHosts.txt
for ((i=2; i<=$num_clients; i++)); do
    echo "$client$i" >> sanClientHosts.txt
    echo "127.0.0.1 $client$i" >> hosts
done

echo "Host names generated and saved to hosts & sanHosts.txt files"