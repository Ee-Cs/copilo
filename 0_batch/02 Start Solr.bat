@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-23

:: This Solr version runs without errors. Solr version 9.8.1 failed.
set SOLR_DIR=D:\TOOLS\SOLR-9.6.0

%SOLR_DIR%\bin\solr.cmd start -c -f -Dsolr.modules=sql
pause