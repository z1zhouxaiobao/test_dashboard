USE liquicool;
SET NAMES utf8mb4;

-- 补字段
ALTER TABLE carousel ADD COLUMN IF NOT EXISTS subtitle VARCHAR(500) NULL AFTER title;
ALTER TABLE notice ADD COLUMN IF NOT EXISTS type VARCHAR(50) NULL AFTER title;
ALTER TABLE operate_log ADD COLUMN IF NOT EXISTS module VARCHAR(50) NULL AFTER action;
ALTER TABLE sys_config ADD COLUMN IF NOT EXISTS updated_at DATETIME NULL;
ALTER TABLE feedback ADD COLUMN IF NOT EXISTS contact_name VARCHAR(50) NULL AFTER user_id;

-- 轮播
UPDATE carousel SET
  subtitle = '专业热管理技术，高效节能冷却系统',
  status = 1,
  created_at = '2026-08-19 09:00:00'
WHERE id = 1;
UPDATE carousel SET
  subtitle = '模块化 CDU，智能流量与温度控制',
  status = 1,
  created_at = '2026-08-19 09:10:00'
WHERE id = 2;
UPDATE carousel SET
  subtitle = '助力绿色数据中心建设，显著降低机房能耗',
  status = 1,
  created_at = '2026-08-19 09:20:00'
WHERE id = 3;

-- 公告
UPDATE notice SET type = '活动通知', status = 1, created_at = '2026-08-19 10:30:00' WHERE id = 3;
UPDATE notice SET type = '维护通知', status = 1, created_at = '2026-08-19 10:20:00' WHERE id = 2;
UPDATE notice SET type = '系统公告', status = 1, created_at = '2026-08-19 10:10:00' WHERE id = 1;

-- 核心技术
UPDATE technology SET created_at = '2026-08-19 11:00:00' WHERE id = 1;
UPDATE technology SET created_at = '2026-08-19 11:10:00' WHERE id = 2;
UPDATE technology SET created_at = '2026-08-19 11:20:00' WHERE id = 3;
UPDATE technology SET created_at = '2026-08-19 11:30:00' WHERE id = 4;

-- 荣誉
UPDATE honor SET created_at = '2026-08-19 12:00:00' WHERE id = 1;
UPDATE honor SET created_at = '2026-08-19 12:10:00' WHERE id = 2;
UPDATE honor SET created_at = '2026-08-19 12:20:00' WHERE id = 3;

-- 咨询
UPDATE consultation SET created_at = '2026-08-19 16:00:00', updated_at = '2026-08-19 16:00:00' WHERE id = 5;
UPDATE consultation SET created_at = '2026-08-19 15:30:00', updated_at = '2026-08-19 15:40:00' WHERE id = 4;
UPDATE consultation SET created_at = '2026-08-19 14:00:00', updated_at = '2026-08-19 14:00:00' WHERE id = 3;
UPDATE consultation SET created_at = '2026-08-19 11:20:00', updated_at = '2026-08-19 13:00:00' WHERE id = 2;
UPDATE consultation SET created_at = '2026-08-19 13:00:00', updated_at = '2026-08-19 15:00:00' WHERE id = 7;
UPDATE consultation SET created_at = '2026-08-19 10:00:00', updated_at = '2026-08-19 12:00:00' WHERE id = 1;
UPDATE consultation SET created_at = '2026-08-19 08:00:00', updated_at = '2026-08-19 09:00:00' WHERE id = 6;

-- 反馈
UPDATE feedback SET contact_name = '王采购', created_at = '2026-08-19 09:10:00' WHERE id = 3;
UPDATE feedback SET contact_name = '李运维', created_at = '2026-08-19 14:20:00' WHERE id = 2;
UPDATE feedback SET contact_name = '张工程师', created_at = '2026-08-19 10:00:00' WHERE id = 1;

-- 配置
UPDATE sys_config SET remark = '网站名称', updated_at = '2026-08-19 09:00:00' WHERE id = 1;
UPDATE sys_config SET remark = '客服电话', updated_at = '2026-08-19 09:05:00' WHERE id = 2;
UPDATE sys_config SET remark = '联系邮箱', updated_at = '2026-08-19 09:10:00' WHERE id = 3;
UPDATE sys_config SET remark = '公司地址', updated_at = '2026-08-19 09:15:00' WHERE id = 4;
UPDATE sys_config SET remark = '宣传语', updated_at = '2026-08-19 09:20:00' WHERE id = 5;

-- 日志
UPDATE operate_log SET module = '咨询工单', created_at = '2026-08-19 10:00:00' WHERE id = 3;
UPDATE operate_log SET module = '解决方案', created_at = '2026-08-19 09:20:00' WHERE id = 2;
UPDATE operate_log SET module = '系统管理', created_at = '2026-08-19 09:00:00' WHERE id = 1;
UPDATE operate_log SET module = '文件上传', created_at = '2026-08-19 16:00:00' WHERE id = 6;
UPDATE operate_log SET module = '咨询工单', created_at = '2026-08-19 11:20:00' WHERE id = 4;
UPDATE operate_log SET module = '用户留言', created_at = '2026-08-19 10:00:00' WHERE id = 5;

-- 其他业务时间
UPDATE product SET created_at = '2026-08-19 10:00:00' WHERE id = 1;
UPDATE product SET created_at = '2026-08-19 10:10:00' WHERE id = 2;
UPDATE product SET created_at = '2026-08-19 10:20:00' WHERE id = 3;
UPDATE product SET created_at = '2026-08-19 10:30:00' WHERE id = 4;
UPDATE product SET created_at = '2026-08-19 10:40:00' WHERE id = 5;
UPDATE product SET created_at = '2026-08-19 10:50:00' WHERE id = 6;

UPDATE news SET publish_time = '2026-08-19 09:00:00', created_at = '2026-08-19 09:00:00' WHERE id = 1;
UPDATE news SET publish_time = '2026-08-19 09:30:00', created_at = '2026-08-19 09:30:00' WHERE id = 2;
UPDATE news SET publish_time = '2026-08-19 10:00:00', created_at = '2026-08-19 10:00:00' WHERE id = 3;
UPDATE news SET publish_time = '2026-08-19 11:00:00', created_at = '2026-08-19 11:00:00' WHERE id = 4;
UPDATE news SET publish_time = '2026-08-19 11:30:00', created_at = '2026-08-19 11:30:00' WHERE id = 5;

UPDATE case_study SET created_at = '2026-08-19 13:00:00' WHERE id = 1;
UPDATE case_study SET created_at = '2026-08-19 13:10:00' WHERE id = 2;
UPDATE case_study SET created_at = '2026-08-19 13:20:00' WHERE id = 3;
UPDATE case_study SET created_at = '2026-08-19 13:30:00' WHERE id = 4;

UPDATE sys_user SET created_at = '2026-08-19 08:00:00', updated_at = '2026-08-19 08:00:00' WHERE id >= 1;
