To build the docker image from windows uncomment the first part of the dockerfile and:
    docker build . --tag thanlinardos/nwdaf_sub:0.0.2-SNAPSHOT   
resulting image name: NWDAF_SUB:0.0.2-SNAPSHOT
From linux uncomment the second part of the dockerfile after running:
    mvn clean install
and run the docker build command.

Then start all the containers:
    docker compose -f .\dockprom\docker-compose.yml up -d
    docker compose -f .\src\main\resources\compose_files\docker-compose.yml up -d
Or only for the databases:
    docker compose -f .\src\main\resources\compose_files\docker-compose-db.yml up -d

