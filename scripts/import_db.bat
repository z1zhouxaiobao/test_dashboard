@echo off
chcp 65001 >nul
mysql -uroot -p123456 -e "SELECT VERSION();"
if errorlevel 1 (
  echo MYSQL_CONNECT_FAIL
  exit /b 1
)
mysql -uroot -p123456 --default-character-set=utf8mb4 < "%~dp0database\init.sql"
if errorlevel 1 (
  echo IMPORT_FAIL
  exit /b 1
)
mysql -uroot -p123456 -e "USE liquicool; SELECT COUNT(*) users FROM sys_user; SELECT COUNT(*) products FROM product; SELECT username,role FROM sys_user;"
echo IMPORT_OK
