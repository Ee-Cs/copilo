@echo on
@set SITE=http://localhost:8080/company
@set CURL=c:\tools\curl-7.58.0\bin\curl.exe
@set CURL=%CURL% -g -i -H "Accept: application/hal+json" -H "Content-Type: application/hal+json"
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

@set DEP_ID=12345
@set EMP_ID=67890

:create
%HR_YELLOW%
@powershell -Command Write-Host "CREATE" -foreground "Green"
%CURL% -d {\"id\":%DEP_ID%,\"name\":\"D-Name-CREATE\"} -X POST "%SITE%/departments"
@echo.
%CURL% -d {\"id\":%EMP_ID%,\"firstName\":\"EF-Name-CREATE\",\"lastName\":\"EL-Name-CREATE\",\"department\":{\"id\":%DEP_ID%}} -X POST "%SITE%/employees"
@echo.

:read-created
%HR_YELLOW%
@powershell -Command Write-Host "READ by id - after CREATE" -foreground "Green"
%CURL% "%SITE%/departments/%DEP_ID%"
@echo.
%CURL% "%SITE%/employees/%EMP_ID%"
@echo.&pause

:update
%HR_YELLOW%
@powershell -Command Write-Host "UPDATE by id" -foreground "Green"
%CURL% -d {\"name\":\"D-Name-UPDATE\"} -X PATCH "%SITE%/departments/%DEP_ID%"
@echo.
%CURL% -d {\"firstName\":\"EF-Name-UPDATE\",\"lastName\":\"EL-Name-UPDATE\",\"department\":{\"id\":%DEP_ID%}} -X PATCH "%SITE%/employees/%EMP_ID%"
@echo.

:read-updated
%HR_YELLOW%
@powershell -Command Write-Host "READ by id - after UPDATE" -foreground "Green"
%CURL% "%SITE%/departments/%DEP_ID%"
@echo.
%CURL% "%SITE%/employees/%EMP_ID%"
@echo.&pause

::goto :read-deleted
:delete
%HR_YELLOW%
@powershell -Command Write-Host "DELETE by id" -foreground "Green"
%CURL% -X DELETE "%SITE%/departments/%DEP_ID%"
@echo.
%CURL% -X DELETE "%SITE%/employees/%EMP_ID%"
@echo.

:read-deleted
%HR_YELLOW%
@powershell -Command Write-Host "READ by id - after DELETE" -foreground "Green"
%CURL% "%SITE%/departments/%DEP_ID%"
@echo.
%CURL% "%SITE%/employees/%EMP_ID%"
@echo.&pause

:finish
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause