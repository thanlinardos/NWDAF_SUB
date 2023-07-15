for /l %%x in (10000, 1, 10004) do (
   echo %%x
   docker compose -f docker-compose-client.yml -p dockprom down
)
docker compose down
docker compose -f C:\Users\thanl\Documents\eclipse-workspace-spring\NWDAF_SUB_CLIENT\dockprom\docker-compose.yml down