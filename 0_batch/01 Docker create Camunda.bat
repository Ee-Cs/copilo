@echo off
set COMPOSE_FILE=docker-config\docker-compose-core.yaml
:: set COMPOSE_FILE=docker-config\docker-compose.yaml
set ENV_FILE=docker-config\environment\camunda-env.properties
cd ..
docker compose --file %COMPOSE_FILE% --env-file %ENV_FILE% up --detach
pause