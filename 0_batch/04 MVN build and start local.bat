@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-24
set M2_HOME=c:\\tools\\apache-maven-3.9.9
set MAVEN_OPTS="--enable-native-access=ALL-UNNAMED"
set SPRING_PROFILES_ACTIVE=local
::set SKIP_TESTS=-DskipTests

call %M2_HOME%\bin\mvn -f ..\pom.xml %SKIP_TESTS% clean install
::pause

:third
set PROJECT=third
echo -------------------------------------------------------------------------- %PROJECT%
start "%PROJECT%" /MAX %M2_HOME%\bin\mvn -f ..\pom.xml --quiet --projects %PROJECT% spring-boot:run

:second
set PROJECT=second
echo -------------------------------------------------------------------------- %PROJECT%
start "%PROJECT%" /MAX %M2_HOME%\bin\mvn -f ..\pom.xml --quiet --projects %PROJECT% spring-boot:run

:first
set PROJECT=first
echo -------------------------------------------------------------------------- %PROJECT%
start "%PROJECT%" /MAX %M2_HOME%\bin\mvn -f ..\pom.xml --quiet --projects %PROJECT% spring-boot:run
pause