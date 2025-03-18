@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set M2_HOME=c:\\tools\\apache-maven-3.9.9
set MODULE_PATH=service\target\Study20_service.jar;^
service-provider\target\Study20_service-provider.jar;^
service-client\target\Study20_service-client.jar
set MODULE=kp.client/kp.client.KpServiceClient

pushd %cd%
cd ..
call %M2_HOME%\bin\mvn clean install
call %JAVA_HOME%\bin\java --module-path %MODULE_PATH% --module %MODULE%
pause
popd