@echo off
docker exec -it ptc-kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cons-1
pause