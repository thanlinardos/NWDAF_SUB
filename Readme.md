# NWDAF_SUB Project
## Description

This project is an implementation of the NWDAF event_subscription service operation according to the 3GPP Standards.

## Usage

### Installing

In order to build and install this project you are going to need:
  - Java JDK 17
  - Apache Maven

### Folder structure

Here's a folder structure for the project:

```
NWDAF_SUB/     # Root directory.
|- dockprom/        # Dockprom dockerized project for collecting prometheus metrics from containers + graphana (more info [here](https://github.com/stefanprodan/dockprom)).
|- src/          # Source directory with java code & resources folder.
    |- main/
        |- java/io/nwdaf/eventsubscription/
        |- resources/
            |- certificates/    # The self-signed certificates required for using HTTPS with TLS handshakes between nwdaf_sub , nwdaf_sub_client & browser used to access the app endpoints
            |- compose_files/    # The compose files for the dockerized project split into database, kafka and global related containers
            |- application.properties    # The properties file of the project (they can be overriden directly by environment variables through compose files or by command line arguements)
            |- application-production.properties    # The properties file of the project when production profile is in use
|- scripts/       # Miscellaneous (python) scripts.
|- superproject/  # Build scripts for all project components -> jars.
|- pom.xml      # Maven project dependencies,info & build targets.
|- run.sh      # Run the database,kafka & dockprom containers.
|- run_all.sh      # Run all the containers (includes dockerized nwdaf_sub,nwdaf_sub_client & nwdaf_sub_collector applications).
|- build.sh      # Initiates building project components to jars with scripts under superproject/.
```

### Setup configuration

Edit the *.env* file to configure the host and port for each component of the project (along with postgres databases' credentials -> they all use the same credentials):

```env
---
server_host=https://nwdafSub
---
default_host=https://localhost

POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

prom_host=prometheus
prom_port=9090
---
```
#### Install certificates
In Windows:
    Go to Manage computer certificates , then right click on Trusted Root Certification Authorities
    and: All Tasks -> Import. You need to import the local-cert.crt and local-client-cert.crt files inside the src/main/resources/certificates/ directory.
In Linux:
    From the project root directory run the following commands:
 
    
    cd src/main/resources/certificates && 
    sudo cp local-cert.crt /usr/local/share/ca-certificates/ && 
    sudo cp local-client-cert.crt /usr/local/share/ca-certificates/ && 
    sudo update-ca-certificates && 
    sudo apt-get update && 
    sudo apt-get install libnss3-tools
    
    
Then to install them in chrome browser:
    
    
    while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_cert -i local-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') && 
    while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_client_cert -i local-client-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0')
    
    
#### Build
First make sure you have cloned in the same parent directory the below projects:
    - NWDAF_SUB              # This project (can handle multiple clients at the same time)
    - NWDAF_SUB_CLIENT       # Example client to simulate a generic network function + simple frontend for creating/updating/deleting subscriptions & receiving notifications from a NWDAF_SUB instance through the browser
    - nwdaf_sub_collector    # Used to collect data exposed by a prometheus instance (and optionally generate dummy data)
    - nwdaf_library          # Contains dependencies for all other components
Building the project is as simple as going to the project root directory and running:
```sh
./build.sh
```
That will install all project artifacts as jar files to the local user maven repository (~/user/.m2/repository is the default location)
#### Run

To run the required containers:    (in linux make sure to use sudo as necessary)
```sh
./run.sh
```
After that check whether kafka1,kafka2 and kafka3 have started successfully & also check kafka-gen logs (kafka-gen closes automatically after generating a uuid inside compose_files/clusterID/).
In windows sometimes the script files inside compose_files/scripts & the clusterID file don't work corrently because of the linebreak ^M (in vscode in the bottom right you can change the type from CRLF to LF).
Also take care that the project is inside a directory with the appopriate read/write permissions so that the docker containers can run successfully.
For testing whether kafka is working refer to [Useful Kafka Commands]
#### Run_all

To run the whole project in a dockerized environment:
```sh
./run_all.sh
```
The above command will generate 5 client containers by default according to the file: compose_files/docker-compose.yml
which will run in port 10000 and above.

#### Useful Kafka Commands
- make a topic:
    ```sh
    docker exec -ti kafka1 /usr/bin/kafka-topics --create  --bootstrap-server kafka1:19092 --replication-factor 1 --partitions 4 --topic test_topic
    ```

- send data:
    ```sh
    docker exec -ti kafka1 /usr/bin/kafka-console-producer --bootstrap-server kafka1:19092 --topic test_topic
    ```
- consume data:
    ```sh
    docker exec -ti kafka1 /usr/bin/kafka-console-consumer --bootstrap-server kafka1:19092 --topic test_topic --from-beginning
    ```

## References

- [Pandoc](http://pandoc.org/)
- [Pandoc Manual](http://pandoc.org/MANUAL.html)
- [Wikipedia: Markdown](http://wikipedia.org/wiki/Markdown)
