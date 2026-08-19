@echo off
chcp 65001 >nul
echo ========================================
echo  Import liquicool demo data to Railway
echo ========================================
echo.
echo 1) Open Railway MySQL -^> Settings -^> Networking
echo 2) Enable TCP Proxy / Public Networking
echo 3) Open MySQL -^> Variables, copy:
echo    MYSQLHOST / MYSQLPORT / MYSQLUSER / MYSQLPASSWORD / MYSQLDATABASE
echo.
set /p HOST=MYSQLHOST: 
set /p PORT=MYSQLPORT: 
set /p USER=MYSQLUSER: 
set /p DB=MYSQLDATABASE: 
echo.
echo Password will be prompted by mysql client...
echo.
mysql -h %HOST% -P %PORT% -u %USER% -p --default-character-set=utf8mb4 %DB% < "%~dp0railway_init.sql"
if errorlevel 1 (
  echo.
  echo FAILED. Make sure mysql client is installed and Public Networking is ON.
  pause
  exit /b 1
)
echo.
echo SUCCESS. Now login with admin / 123456
pause
