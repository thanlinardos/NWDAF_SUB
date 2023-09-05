First build the library to the local maven repository by going to the nwdaf_library project and running "mvn clean install"
To build the docker image:
    - Windows:
        uncomment the first part of the dockerfile and
        ./mvnw -DskipTests clean install
        docker build . --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT
    - Linux:
        uncomment the second part of the dockerfile and
        mvn -DskipTests clean install && sudo docker build . --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT

Then start all the containers:
    docker compose -f .\dockprom\docker-compose.yml up -d
    docker compose -f .\src\main\resources\compose_files\docker-compose.yml up -d
Or only for the databases:
    docker compose -f .\src\main\resources\compose_files\docker-compose-db.yml up -d

To install the ssl certificates for the client & server:

cd src/main/resources/certificates && sudo cp local-cert.crt /usr/local/share/ca-certificates/ && sudo cp local-client-cert.crt /usr/local/share/ca-certificates/ && sudo update-ca-certificates && sudo apt-get update && sudo apt-get install libnss3-tools
<br>
For installing them in chrome:
<p>
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_cert -i local-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0')
</p>
<p>
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_client_cert -i local-client-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0')
</p>