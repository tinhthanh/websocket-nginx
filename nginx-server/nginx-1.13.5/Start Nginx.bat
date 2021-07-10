@echo off

taskkill /IM nginx.exe /F > NUL 2>&1
START /B nginx.exe
FOR /F %%x IN ('tasklist /NH /FI "IMAGENAME eq nginx.exe"') DO IF %%x == nginx.exe goto FOUND
echo Nginx cannot be started. Please check your nginx.conf setting
goto FIN
:FOUND
echo Nginx is running now...
:FIN

pause
exit