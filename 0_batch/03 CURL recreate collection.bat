@echo on
@set SITE=http://localhost:8983/api/collections
@set CURL=c:\tools\curl-7.58.0\bin\curl.exe -g -i
@set CURL_POST=%CURL% -H "Content-Type: application/json" -X POST
@set COLLECTION=kp_study
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

:delete-collection
%HR_YELLOW%
@powershell -Command Write-Host "DELETE COLLECTION" -foreground "Green"
%CURL% -X DELETE %SITE%/%COLLECTION%
@echo.

:create-collection
%HR_YELLOW%
@powershell -Command Write-Host "CREATE COLLECTION" -foreground "Green"
%CURL_POST% -d @../solr-requests/create-collection.json --url %SITE%
@echo.

:create-schema
%HR_YELLOW%
@powershell -Command Write-Host "CREATE SCHEMA" -foreground "Green"
%CURL_POST% -d @../solr-requests/create-schema.json --url %SITE%/%COLLECTION%/schema
@echo.

:add-dataset
%HR_YELLOW%
@powershell -Command Write-Host "ADD DATASET" -foreground "Green"
%CURL_POST% -d @../solr-requests/dataset.json --url %SITE%/%COLLECTION%/update?commit=true
@echo.

:collection-status
%HR_YELLOW%
@powershell -Command Write-Host "COLLECTION STATUS" -foreground "Green"
%CURL% -X GET %SITE%/%COLLECTION%
@echo.

:finish
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause