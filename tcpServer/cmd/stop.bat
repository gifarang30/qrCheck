@echo off
Set port=9981
for /f "tokens=5" %%p in ('netstat -ano ^| find "LISTENING" ^| FIND "0:%port%"') do taskkill /F /PID %%p