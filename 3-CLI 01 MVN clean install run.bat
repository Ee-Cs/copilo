@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-24
cd c:\Temp\Study01\3-client
call c:\Temp\apache-maven-3.9.9\bin\mvn clean install spring-boot:run
::call c:\tools\apache-maven-3.9.11\bin\mvn clean install exec:java
pause