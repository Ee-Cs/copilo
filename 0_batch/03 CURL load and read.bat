@echo on
@set SITE=http://localhost:8080/company
@set CURL=c:\tools\curl-7.58.0\bin\curl.exe
@set CURL=%CURL% -g -i -H "Accept: application/json" -H "Content-Type: application/json"
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

%HR_YELLOW%
@powershell -Command Write-Host "Load sample dataset" -foreground "Green"
%CURL% "%SITE%/loadSampleDataset"
@echo.

:get-one-department
%HR_YELLOW%
@powershell -Command Write-Host "GET one department" -foreground "Green"
%CURL% "%SITE%/departments/1"
@echo.

:get-one-employee
%HR_YELLOW%
@powershell -Command Write-Host "GET one employee" -foreground "Green"
%CURL% "%SITE%/employees/101"
@echo.&pause

:get-all-departments
%HR_YELLOW%
@powershell -Command Write-Host "GET all departments" -foreground "Green"
%CURL% "%SITE%/departments?page=0&size=20&sort=name,asc"
@echo.

:get-all-employees
%HR_YELLOW%
@powershell -Command Write-Host "GET all employees" -foreground "Green"
%CURL% "%SITE%/employees?page=0&size=20&sort=lastName,asc&sort=firstName,asc"
@echo.

:finish
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause