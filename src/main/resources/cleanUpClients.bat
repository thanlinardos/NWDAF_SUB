set /a counter=1
for /l %%x in (10000, 1, 10050) do (
   echo %%x
   docker compose -f docker-compose-client.yml -p client_%%x down
   set /a counter+=1
)
