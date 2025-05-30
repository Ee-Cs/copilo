@echo off
set PROJECT=study26
set COMPOSE_FILE=..\docker-config\compose.yaml

docker container rm --force first > nul 2>&1
docker container rm --force second > nul 2>&1
docker container rm --force third > nul 2>&1
docker image rm --force %PROJECT%-first > nul 2>&1
docker image rm --force %PROJECT%-second > nul 2>&1
docker image rm --force %PROJECT%-third > nul 2>&1
echo ------------------------------------------------------------------------------------------
docker compose down
docker compose -f %COMPOSE_FILE% -p %PROJECT% up --detach
echo ------------------------------------------------------------------------------------------
docker compose -f %COMPOSE_FILE% -p %PROJECT% ps
echo ------------------------------------------------------------------------------------------
docker compose -f %COMPOSE_FILE% -p %PROJECT% images
:: docker network inspect %PROJECT%_net
pause