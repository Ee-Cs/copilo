@echo off
set SITE=http://localhost:%1
set CURL=c:\tools\curl-7.58.0\bin\curl.exe
set CURL=%CURL% -s -g -H "Accept: application/json" -H "Content-Type: application/json"
set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

:chat_words
%HR_YELLOW%
powershell -Command Write-Host "CHAT WITH WORDS" -foreground "Green"
%CURL% -X GET %SITE%/chats/words/3
echo.

:chat_numbers
%HR_YELLOW%
powershell -Command Write-Host "CHAT WITH NUMBERS" -foreground "Green"
%CURL% -X GET %SITE%/chats/numbers/10000
echo.

:finish
%HR_RED%
powershell -Command Write-Host "FINISH" -foreground "Red"