@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23
set FULL_JAR=.\target\Study17-jar-with-dependencies.jar
pushd %cd%
cd ..
chcp 65001 > nul 2>&1
if exist %FULL_JAR% call %JAVA_HOME%\bin\java -jar %FULL_JAR%
pause
popd