@echo off
set CURL=c:\tools\curl-7.58.0\bin\curl.exe -s -g -H "Accept: application/json" -H "Content-Type: application/json"
:: call %CURL% -X GET http://localhost:9091/chats/numbers/10000
::echo.
::pause

call %CURL% -X GET http://localhost:9093/chats/aaa
echo.
pause

echo LIMIT ONE
call %CURL% -X GET http://localhost:9093/chats/numbers/1
echo.
pause

echo LIMIT TWO
call %CURL% -X GET http://localhost:9093/chats/numbers/2
echo.
pause