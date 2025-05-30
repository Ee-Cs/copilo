@echo off
set SITE=http://localhost:%1
set CURL=c:\tools\curl-7.58.0\bin\curl.exe
set CURL=%CURL% -s -g -H "Accept: application/json" -H "Content-Type: application/json"
set JQ=d:\tools\jq\jq-win64.exe
set NO_COLOR=Y
set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

:get_all_departments
%HR_YELLOW%
powershell -Command Write-Host "GET ALL DEPARTMENTS" -foreground "Green"
%CURL% -X GET %SITE%/departments | %JQ% .
echo.

:get_department_by_id
%HR_YELLOW%
powershell -Command Write-Host "GET DEPARTMENT BY ID" -foreground "Green"
%CURL% -X GET %SITE%/departments/1 | %JQ% .
echo.

:finish
%HR_RED%
powershell -Command Write-Host "FINISH" -foreground "Red"