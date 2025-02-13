@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set M2_HOME=c:\\tools\\apache-maven-3.9.9
set PROJECT=study05
set DOCKER_IMAGE_SND=eeengcs/study05_kafka-sender:1.0.0-SNAPSHOT
set DOCKER_IMAGE_PRC=eeengcs/study05_kafka-processor:1.0.0-SNAPSHOT
set DOCKER_IMAGE_REC=eeengcs/study05_kafka-receiver:1.0.0-SNAPSHOT
set DOCKER_FILE_SND=..\docker-config\Dockerfile-sender
set DOCKER_FILE_PRC=..\docker-config\Dockerfile-processor
set DOCKER_FILE_REC=..\docker-config\Dockerfile-receiver
set COMPOSE_FILE=docker-config\compose.yaml

pushd %cd%
cd ..
goto :mavenBuildOnDockerAndRun

:mavenBuildOnLocalhost
cd kafka-sender
call %M2_HOME%\bin\mvn clean install
cd ..\kafka-processor
call %M2_HOME%\bin\mvn clean install
cd ..\kafka-receiver
call %M2_HOME%\bin\mvn clean install
pause
goto :eof

:mavenBuildOnDockerAndRun
::docker compose -f %COMPOSE_FILE% down

cd kafka-sender
docker image rm --force %DOCKER_IMAGE_SND%
docker build -f %DOCKER_FILE_SND% --tag %DOCKER_IMAGE_SND% .
docker push %DOCKER_IMAGE_SND%

cd ..\kafka-processor
docker image rm --force %DOCKER_IMAGE_PRC%
docker build -f %DOCKER_FILE_PRC% --tag %DOCKER_IMAGE_PRC% .
docker push %DOCKER_IMAGE_PRC%

cd ..\kafka-receiver
docker image rm --force %DOCKER_IMAGE_REC%
docker build -f %DOCKER_FILE_REC% --tag %DOCKER_IMAGE_REC% .
docker push %DOCKER_IMAGE_REC%
cd ..

docker compose -f %COMPOSE_FILE% -p %PROJECT% up --detach
echo ------------------------------------------------------------------------------------------
docker compose -f %COMPOSE_FILE%  -p %PROJECT% ps
echo ------------------------------------------------------------------------------------------
docker compose -f %COMPOSE_FILE%  -p %PROJECT% images
popd
pause