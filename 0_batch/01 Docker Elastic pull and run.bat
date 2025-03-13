@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set M2_HOME=c:\\tools\\apache-maven-3.9.9
set ELASTICSEARCH_VERSION=8.17.3

docker container rm --force elasticsearch
docker container rm --force kibana

docker network create elastic

docker pull docker.elastic.co/elasticsearch/elasticsearch:%ELASTICSEARCH_VERSION%

docker run --name elasticsearch ^
 --net elastic ^
 --publish 9200:9200 ^
 --publish 9300:9300 ^
 --env "discovery.type=single-node" ^
 --tty ^
 docker.elastic.co/elasticsearch/elasticsearch:%ELASTICSEARCH_VERSION%
pause
