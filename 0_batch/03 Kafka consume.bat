@echo off
docker exec -it spr-kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic kp-2
pause