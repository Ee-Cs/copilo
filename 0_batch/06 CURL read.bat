@echo on
@set CURL=curl -g -i -H "Accept: application/json" -H "Content-Type: application/json"
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

%HR_YELLOW%
@powershell -Command Write-Host "Content from producer service - port 8081" -foreground "Green"
%CURL% "http://localhost:8081/content"

@echo.
%HR_YELLOW%
@powershell -Command Write-Host "Content from producer service - port 9091" -foreground "Green"
%CURL% "http://localhost:9091/content"

@echo.
%HR_YELLOW%
@powershell -Command Write-Host "Content from consumer service - port 8082" -foreground "Green"
%CURL% "http://localhost:8082/content"

@echo.
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause