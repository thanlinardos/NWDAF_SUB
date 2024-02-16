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
rm hosts sanHosts.txt sanClientHosts.txt -f
echo "# NWDAF_SUB project:" >> hosts
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
echo "Installing hosts to /etc/hosts"

if [[ $(uname -s) = *"NT"* ]]; then
    echo "Running on Windows. Trying to save hosts file..."
    powershell.exe -Command "\
        \$customHostsFile = 'hosts'; \
        \if (Test-Path \$customHostsFile) { \
        \Add-Content -Path 'C:\Windows\System32\drivers\etc\hosts' -Value '\$(Get-Content \$customHostsFile)'\
        \else {\
        \Write-Output 'Custom hosts file not found. Skipping appending to Windows hosts file.'\
        \}\
        "
    echo "Hosts file updated (Windows)"

elif [[ $(uname -s) = *"Linux"* ]]; then
  echo "Running on Linux. Trying to save hosts file to /etc/hosts..."
  sudo cat hosts | sudo tee -a /etc/hosts >/dev/null &&
  echo "Hosts file updated (Linux)"
fi
