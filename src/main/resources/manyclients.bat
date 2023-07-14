docker compose -f C:\Users\thanl\Documents\eclipse-workspace-spring\NWDAF_SUB_CLIENT\dockprom\docker-compose.yml up -d
docker compose up -d
for /l %%x in (10001, 1, 10025) do (
   echo %%x
   (echo client_port=%%x && echo server_port=8080 && echo server_host=http://localhost && echo client_host=http://localhost && echo client_max_port=8085 && echo server_max_port=8070 && echo c=%%x)> ./temp.env
   docker compose -f docker-compose-client.yml --env-file ./temp.env -p client_%%x up -d
)
