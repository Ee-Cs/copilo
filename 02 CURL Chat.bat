@echo off
set CURL=c:\tools\curl-7.58.0\bin\curl.exe -s -g -H "Accept: application/json" -H "Content-Type: application/json"
::call %CURL% -X GET http://localhost:9091/chats/numbers/10000
::echo.
::pause

set LIMIT=1
echo LIMIT %LIMIT%
call %CURL% -X GET http://localhost:9093/chats/numbers/%LIMIT%
echo.
pause

set LIMIT=5
echo LIMIT %LIMIT%
call %CURL% -X GET http://localhost:9093/chats/numbers/%LIMIT%
echo.
pause