@echo off
chcp 65001 >nul
echo ============================================
echo  立冷科技 LIQUICOOL 数据库导入
echo  库名: liquicool   账号: root / 123456
echo ============================================
cd /d "%~dp0"

where mysql >nul 2>nul
if errorlevel 1 (
  echo [错误] 未找到 mysql 命令，请把 MySQL bin 目录加入 PATH
  echo 例如: C:\Program Files\MySQL\MySQL Server 8.0\bin
  pause
  exit /b 1
)

mysql -uroot -p123456 -e "SELECT VERSION();" 2>nul
if errorlevel 1 (
  echo [错误] 无法连接 MySQL，请确认服务已启动且密码为 123456
  pause
  exit /b 1
)

echo 正在导入 database\init.sql ...
mysql -uroot -p123456 --default-character-set=utf8mb4 < "%~dp0database\init.sql"
if errorlevel 1 (
  echo [失败] 导入出错
  pause
  exit /b 1
)

echo [成功] 数据库 liquicool 已导入完成
echo 默认账号: admin / 123456   用户: user1 / 123456
pause
