@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set IMAGE_DIR=..\target\runtime_image
SET REP_DIR=c:\Users\KP\.m2\repository
set MOD_PATH=$JAVA_HOME/jmods;^
%REP_DIR%\kp\Study20_service\1.0.0-SNAPSHOT\Study20_service-1.0.0-SNAPSHOT.jar;^
%REP_DIR%\kp\Study20_service-client\1.0.0-SNAPSHOT\Study20_service-client-1.0.0-SNAPSHOT.jar;^
%REP_DIR%\kp\Study20_service-provider\1.0.0-SNAPSHOT\Study20_service-provider-1.0.0-SNAPSHOT.jar
set MODULES=kp.client,kp.service,kp.provider
set MODULE=kp.client/kp.client.KpServiceClient

if exist %IMAGE_DIR% rmdir /S /Q %IMAGE_DIR%
call %JAVA_HOME%\bin\jlink --module-path %MOD_PATH% --add-modules %MODULES% --output %IMAGE_DIR%
call %IMAGE_DIR%\bin\java --module %MODULE%
pause