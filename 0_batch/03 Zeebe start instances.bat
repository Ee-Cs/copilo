@echo off

:: it uses Zeebe CLI client
set DATA_1="{\"impact\": \"medium\", \"urgency\": \"high\"}"
set DATA_2="{\"impact\": \"medium\", \"urgency\": \"medium\"}"
set DATA_3="{\"impact\": \"low\",    \"urgency\": \"medium\"}"
set ZB_APP=d:\tools\camunda-zeebe-cli\zbctl.exe
set ZEEBE_ADDRESS=127.0.0.1:26500
set ZEEBE_INSECURE_CONNECTION=true

%ZB_APP% status
echo --------------------------------------------------
%ZB_APP% create instance TriageProcess_01 --variables %DATA_1%
echo --------------------------------------------------
%ZB_APP% create instance TriageProcess_01 --variables %DATA_2%
echo --------------------------------------------------
%ZB_APP% create instance TriageProcess_01 --variables %DATA_3%
pause