@echo off
chcp 65001 >nul
mysql -uroot -p123456 liquicool -e "ALTER TABLE carousel ADD COLUMN subtitle VARCHAR(500) NULL AFTER title;" 2>nul
mysql -uroot -p123456 liquicool -e "ALTER TABLE notice ADD COLUMN type VARCHAR(50) NULL AFTER title;" 2>nul
mysql -uroot -p123456 liquicool -e "ALTER TABLE operate_log ADD COLUMN module VARCHAR(50) NULL AFTER action;" 2>nul
mysql -uroot -p123456 liquicool -e "ALTER TABLE sys_config ADD COLUMN updated_at DATETIME NULL;" 2>nul
mysql -uroot -p123456 liquicool -e "ALTER TABLE feedback ADD COLUMN contact_name VARCHAR(50) NULL AFTER user_id;" 2>nul

mysql -uroot -p123456 --default-character-set=utf8mb4 liquicool -e ^
"UPDATE carousel SET subtitle='专业热管理技术，高效节能冷却系统', status=1, created_at='2026-08-19 09:00:00' WHERE id=1;^
UPDATE carousel SET subtitle='模块化 CDU，智能流量与温度控制', status=1, created_at='2026-08-19 09:10:00' WHERE id=2;^
UPDATE carousel SET subtitle='助力绿色数据中心建设，显著降低机房能耗', status=1, created_at='2026-08-19 09:20:00' WHERE id=3;^
UPDATE notice SET type='活动通知', status=1, created_at='2026-08-19 10:30:00' WHERE id=3;^
UPDATE notice SET type='维护通知', status=1, created_at='2026-08-19 10:20:00' WHERE id=2;^
UPDATE notice SET type='系统公告', status=1, created_at='2026-08-19 10:10:00' WHERE id=1;^
UPDATE technology SET created_at='2026-08-19 11:00:00' WHERE id=1;^
UPDATE technology SET created_at='2026-08-19 11:10:00' WHERE id=2;^
UPDATE technology SET created_at='2026-08-19 11:20:00' WHERE id=3;^
UPDATE technology SET created_at='2026-08-19 11:30:00' WHERE id=4;^
UPDATE honor SET created_at='2026-08-19 12:00:00' WHERE id=1;^
UPDATE honor SET created_at='2026-08-19 12:10:00' WHERE id=2;^
UPDATE honor SET created_at='2026-08-19 12:20:00' WHERE id=3;"

mysql -uroot -p123456 --default-character-set=utf8mb4 liquicool -e ^
"UPDATE consultation SET created_at='2026-08-19 16:00:00', updated_at='2026-08-19 16:00:00' WHERE id=5;^
UPDATE consultation SET created_at='2026-08-19 15:30:00', updated_at='2026-08-19 15:40:00' WHERE id=4;^
UPDATE consultation SET created_at='2026-08-19 14:00:00', updated_at='2026-08-19 14:00:00' WHERE id=3;^
UPDATE consultation SET created_at='2026-08-19 11:20:00', updated_at='2026-08-19 13:00:00' WHERE id=2;^
UPDATE consultation SET created_at='2026-08-19 13:00:00', updated_at='2026-08-19 15:00:00' WHERE id=7;^
UPDATE consultation SET created_at='2026-08-19 10:00:00', updated_at='2026-08-19 12:00:00' WHERE id=1;^
UPDATE consultation SET created_at='2026-08-19 08:00:00', updated_at='2026-08-19 09:00:00' WHERE id=6;^
UPDATE feedback SET contact_name='王采购', created_at='2026-08-19 09:10:00' WHERE id=3;^
UPDATE feedback SET contact_name='李运维', created_at='2026-08-19 14:20:00' WHERE id=2;^
UPDATE feedback SET contact_name='张工程师', created_at='2026-08-19 10:00:00' WHERE id=1;"

mysql -uroot -p123456 --default-character-set=utf8mb4 liquicool -e ^
"UPDATE favorite SET created_at='2026-08-19 16:10:00' WHERE id=8;^
UPDATE favorite SET created_at='2026-08-19 16:00:00' WHERE id=7;^
UPDATE favorite SET created_at='2026-08-19 15:00:00' WHERE id=6;^
UPDATE favorite SET created_at='2026-08-19 11:10:00' WHERE id=5;^
UPDATE favorite SET created_at='2026-08-19 11:00:00' WHERE id=4;^
UPDATE favorite SET created_at='2026-08-19 09:00:00' WHERE id=3;^
UPDATE favorite SET created_at='2026-08-19 10:05:00' WHERE id=2;^
UPDATE favorite SET created_at='2026-08-19 10:00:00' WHERE id=1;^
UPDATE sys_config SET remark='网站名称', updated_at='2026-08-19 09:00:00' WHERE id=1;^
UPDATE sys_config SET remark='客服电话', updated_at='2026-08-19 09:05:00' WHERE id=2;^
UPDATE sys_config SET remark='联系邮箱', updated_at='2026-08-19 09:10:00' WHERE id=3;^
UPDATE sys_config SET remark='公司地址', updated_at='2026-08-19 09:15:00' WHERE id=4;^
UPDATE sys_config SET remark='宣传语', updated_at='2026-08-19 09:20:00' WHERE id=5;^
UPDATE operate_log SET module='咨询工单', created_at='2026-08-19 10:00:00' WHERE id=3;^
UPDATE operate_log SET module='解决方案', created_at='2026-08-19 09:20:00' WHERE id=2;^
UPDATE operate_log SET module='系统管理', created_at='2026-08-19 09:00:00' WHERE id=1;^
UPDATE operate_log SET module='文件上传', created_at='2026-08-19 16:00:00' WHERE id=6;^
UPDATE operate_log SET module='咨询工单', created_at='2026-08-19 11:20:00' WHERE id=4;^
UPDATE operate_log SET module='收藏记录', created_at='2026-08-19 10:00:00' WHERE id=5;"

mysql -uroot -p123456 --default-character-set=utf8mb4 liquicool -e ^
"UPDATE product SET created_at='2026-08-19 10:00:00' WHERE id=1;^
UPDATE product SET created_at='2026-08-19 10:10:00' WHERE id=2;^
UPDATE product SET created_at='2026-08-19 10:20:00' WHERE id=3;^
UPDATE product SET created_at='2026-08-19 10:30:00' WHERE id=4;^
UPDATE product SET created_at='2026-08-19 10:40:00' WHERE id=5;^
UPDATE product SET created_at='2026-08-19 10:50:00' WHERE id=6;^
UPDATE news SET publish_time='2026-08-19 09:00:00', created_at='2026-08-19 09:00:00' WHERE id=1;^
UPDATE news SET publish_time='2026-08-19 09:30:00', created_at='2026-08-19 09:30:00' WHERE id=2;^
UPDATE news SET publish_time='2026-08-19 10:00:00', created_at='2026-08-19 10:00:00' WHERE id=3;^
UPDATE news SET publish_time='2026-08-19 11:00:00', created_at='2026-08-19 11:00:00' WHERE id=4;^
UPDATE news SET publish_time='2026-08-19 11:30:00', created_at='2026-08-19 11:30:00' WHERE id=5;^
UPDATE case_study SET created_at='2026-08-19 13:00:00' WHERE id=1;^
UPDATE case_study SET created_at='2026-08-19 13:10:00' WHERE id=2;^
UPDATE case_study SET created_at='2026-08-19 13:20:00' WHERE id=3;^
UPDATE case_study SET created_at='2026-08-19 13:30:00' WHERE id=4;"

echo FILL_OK
mysql -uroot -p123456 liquicool -e "SELECT id,subtitle,status FROM carousel; SELECT id,type,status FROM notice; SELECT id,module FROM operate_log; SELECT id,remark,updated_at FROM sys_config; SELECT id,contact_name FROM feedback;"
