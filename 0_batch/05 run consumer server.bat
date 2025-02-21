@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set JAR=..\microservices-consumer\target\Study10_microservices-consumer-1.0.0-SNAPSHOT.jar
if exist %JAR% call %JAVA_HOME%\bin\java -jar %JAR%
pause