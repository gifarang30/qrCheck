@echo off
Set port=9981
for /f "tokens=5" %%p in ('netstat -ano ^| find "LISTENING" ^| FIND "0:%port%"') do taskkill /F /PID %%p
start javaw -jar -Dfile.encoding=UTF-8 %~dp0\kjb_netty_tcp.jar %port%