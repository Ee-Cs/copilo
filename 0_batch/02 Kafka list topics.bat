@echo off
set EXEC_KAFKA_TOPICS=docker exec -t spr-kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092

%EXEC_KAFKA_TOPICS% --list 
echo ------------------------------------------------------------------------------------------
for %%T in (kp-1 kp-2) do (
	%EXEC_KAFKA_TOPICS% --describe --topic %%T
	echo ------------------------------------------------------------------------------------------
)
pause