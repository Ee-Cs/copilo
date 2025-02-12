@echo off
set EXEC_KAFKA_TOPICS=docker exec -t ptc-kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092
set TOPICS=prod-1, prod-2, cons-1, cons-2

%EXEC_KAFKA_TOPICS% --list 
echo ------------------------------------------------------------------------------------------
for %%T in (%TOPICS%) do (
	%EXEC_KAFKA_TOPICS% --describe --topic %%T
	echo ------------------------------------------------------------------------------------------
)
pause