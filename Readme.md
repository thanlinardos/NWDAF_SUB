To build the docker image:
    .\mvnw -Pnative spring-boot:build-image
resulting image name: NWDAF_SUB:0.0.1-SNAPSHOT

Then start all the containers:
    docker compose up .

To build executable only:
    .\mvnw -Pnative native:compile

The excecutable will be in /target dir.