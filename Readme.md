To build the docker image:
    docker build . --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT   
resulting image name: NWDAF_SUB:0.0.1-SNAPSHOT

Then start all the containers:
    docker compose -f .\dockprom\docker-compose.yml up -d
    docker compose -f .\src\main\resources\docker-compose.yml up -d
Or only for the databases:
    docker compose -f .\src\main\resources\docker-compose-db.yml up -d

