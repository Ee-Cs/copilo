@echo off
:: password taken from 'elasticsearch' Docker Container run console output:
set ELASTIC_PASSWORD=mo_GXqfBS8t6G*8mYhHc
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set M2_HOME=c:\\tools\\apache-maven-3.9.9
pushd %cd%
cd ..
call %M2_HOME%\bin\mvn clean install exec:java  -Dexec.args="%ELASTIC_PASSWORD%"
pause
popd