set /a counter=1
for /l %%x in (10000, 1, 10050) do (
   echo %%x
   (echo client_port=%%x && echo server_port=8080 && echo server_host=http://localhost && echo client_host=http://localhost && echo client_max_port=8085 && echo server_max_port=8070 && echo c=%%x)> ./temp.env
   docker compose -f docker-compose-client.yml --env-file ./temp.env -p client_%%x up -d
   set /a counter+=1
)
