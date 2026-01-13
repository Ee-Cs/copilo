@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-25
set MAVEN_OPTS="--enable-native-access=ALL-UNNAMED"
set SPRING_PROFILES_ACTIVE=local
set HR_RED=@powershell -Command Write-Host "======================================================================" -foreground "Red"

call c:\tools\apache-maven-3.9.11\bin\mvn --quiet clean install
%HR_RED%
%HR_RED%
%HR_RED%
pause
::start "A-Second" /MAX c:\tools\apache-maven-3.9.11\bin\mvn --quiet --projects a-second spring-boot:run
::start "A-First"  /MAX c:\tools\apache-maven-3.9.11\bin\mvn --quiet --projects a-first  spring-boot:run
start "Z-Second" /MAX c:\tools\apache-maven-3.9.11\bin\mvn --quiet --projects z-second spring-boot:run
start "Z-First"  /MAX c:\tools\apache-maven-3.9.11\bin\mvn --quiet --projects z-first  spring-boot:run