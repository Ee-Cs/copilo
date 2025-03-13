@echo on
@set CURL=c:\tools\curl-7.58.0\bin\curl.exe -g -i
@set SITE=http://localhost:8983/solr/kp_study/select
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

%HR_YELLOW%
@powershell -Command Write-Host "SIMPLE QUERY" -foreground "Green"
%CURL% "%SITE%?indent=on&q=*:*&fl=*+score"
%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause