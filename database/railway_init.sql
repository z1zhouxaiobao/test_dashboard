-- Railway MySQL: import ALL demo data
-- File: database/railway_init.sql

-- 立冷科技 LIQUICOOL 数据库初始化
-- MySQL 8 / root / 123456
-- 密码均为 MD5(123456) = e10adc3949ba59abbe56e057f20f883e


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS operate_log;
DROP TABLE IF EXISTS sys_config;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS consultation;
DROP TABLE IF EXISTS case_study;
DROP TABLE IF EXISTS honor;
DROP TABLE IF EXISTS technology;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS carousel;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(64) NOT NULL,
  nickname VARCHAR(50),
  avatar VARCHAR(255),
  phone VARCHAR(20),
  email VARCHAR(100),
  role VARCHAR(20) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE carousel (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  subtitle VARCHAR(500),
  image_url VARCHAR(500),
  link_url VARCHAR(500),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE news (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  cover_url VARCHAR(500),
  summary VARCHAR(500),
  content TEXT,
  view_count INT DEFAULT 0,
  status INT DEFAULT 1,
  publish_time DATETIME,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE notice (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  type VARCHAR(50),
  content TEXT,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200),
  cover_url VARCHAR(500),
  category VARCHAR(50),
  summary VARCHAR(500),
  content TEXT,
  specs TEXT,
  price_hint VARCHAR(100),
  status INT DEFAULT 1,
  sort_order INT DEFAULT 0,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE technology (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  cover_url VARCHAR(500),
  icon_url VARCHAR(500),
  summary VARCHAR(500),
  content TEXT,
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE honor (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  cover_url VARCHAR(500),
  award_year VARCHAR(20),
  issuer VARCHAR(200),
  summary VARCHAR(500),
  sort_order INT DEFAULT 0,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE case_study (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  cover_url VARCHAR(500),
  client_name VARCHAR(100),
  industry VARCHAR(100),
  summary VARCHAR(500),
  content TEXT,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE consultation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  product_id BIGINT,
  contact_name VARCHAR(50),
  phone VARCHAR(20),
  company VARCHAR(100),
  email VARCHAR(100),
  need_type VARCHAR(50),
  content TEXT,
  status VARCHAR(20),
  reply TEXT,
  created_at DATETIME,
  updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE favorite (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  target_type VARCHAR(20),
  target_id BIGINT,
  created_at DATETIME,
  UNIQUE KEY uk_fav (user_id, target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE feedback (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  contact_name VARCHAR(50),
  content TEXT,
  contact VARCHAR(100),
  status VARCHAR(20),
  reply TEXT,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  config_key VARCHAR(100) NOT NULL UNIQUE,
  config_value TEXT,
  remark VARCHAR(200),
  updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE operate_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  username VARCHAR(50),
  action VARCHAR(100),
  module VARCHAR(50),
  detail TEXT,
  ip VARCHAR(50),
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==================== 用户 ====================
INSERT INTO sys_user (id, username, password, nickname, avatar, phone, email, role, status, created_at, updated_at) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '/uploads/avatar-admin.png', '13800000001', 'admin@liquicool.com', 'ADMIN', 1, '2026-01-10 09:00:00', '2026-01-10 09:00:00'),
(2, 'user1', 'e10adc3949ba59abbe56e057f20f883e', '张工程师', '/uploads/avatar-user1.png', '13800000002', 'zhang@demo.com', 'USER', 1, '2026-02-01 10:00:00', '2026-02-01 10:00:00'),
(3, 'user2', 'e10adc3949ba59abbe56e057f20f883e', '李运维', '/uploads/avatar-default.png', '13800000003', 'li@demo.com', 'USER', 1, '2026-02-15 11:00:00', '2026-02-15 11:00:00'),
(4, 'user3', 'e10adc3949ba59abbe56e057f20f883e', '王采购', '/uploads/avatar-default.png', '13800000004', 'wang@demo.com', 'USER', 1, '2026-03-01 14:00:00', '2026-03-01 14:00:00'),
(5, 'demo', 'e10adc3949ba59abbe56e057f20f883e', '演示账号', '/uploads/avatar-default.png', '13800000005', 'demo@liquicool.com', 'USER', 1, '2026-03-20 16:00:00', '2026-03-20 16:00:00');

-- ==================== 轮播 ====================
INSERT INTO carousel (id, title, subtitle, image_url, link_url, sort_order, status, created_at) VALUES
(1, '数据中心液冷解决方案专家', '专业热管理技术，高效节能冷却系统', '/uploads/carousel-1.jpg', '/portal/products', 1, 1, '2026-08-19 09:00:00'),
(2, '高效节能 CDU 冷量分配系统', '模块化 CDU，智能流量与温度控制', '/uploads/carousel-2.jpg', '/portal/technologies', 2, 1, '2026-08-19 09:10:00'),
(3, '助力绿色算力 PUE 低至 1.06', '助力绿色数据中心建设，显著降低机房能耗', '/uploads/carousel-3.jpg', '/portal/cases', 3, 1, '2026-08-19 09:20:00');

-- ==================== 产品 ====================
INSERT INTO product (id, name, cover_url, category, summary, content, specs, price_hint, status, sort_order, created_at) VALUES
(1, 'LC-8000 浸没式液冷服务器', '/uploads/product-1.jpg', '液冷服务器', '高密度算力浸没式液冷服务器，单机柜可达 100kW+',
 '立冷科技 LC-8000 采用单相浸没式液冷技术，服务器完全浸没于绝缘冷却液中，散热效率远超传统风冷，适用于 AI 训练集群与高性能计算场景。',
 '功率密度:100kW+/柜；冷却方式:单相浸没；噪音:<45dB；PUE:<1.08', '面议', 1, 1, '2026-02-10 10:00:00'),
(2, 'CDU-500 冷量分配单元', '/uploads/product-2.jpg', 'CDU冷量分配单元', '模块化 CDU，智能流量与温度控制，适配冷板式液冷机房。',
 'CDU-500 提供稳定冷量分配，支持冗余泵组与远程监控，可无缝对接楼宇冷却水系统。',
 '制冷量:500kW；接口:DN80；控制:PLC+物联网；冗余:N+1', '面议', 1, 2, '2026-02-12 10:00:00'),
(3, 'CoolX 高效介电冷却液', '/uploads/product-3.jpg', '冷却液', '低粘度、高绝缘、长寿命介电冷却液，兼容主流浸没方案。',
 'CoolX 冷却液具备优异热传导与材料相容性，维护周期长，可显著降低运维成本。',
 '闪点:>150C；电导率:极低；寿命:5年+；环保:可回收', '咨询报价', 1, 3, '2026-02-15 10:00:00'),
(4, 'RackCool 液冷整机柜', '/uploads/product-4.jpg', '机柜系统', '标准化液冷整机柜，支持盲插快接，交付即用。',
 '面向新建与改造机房，提供整机柜液冷解决方案，缩短部署周期。',
 'U位:42U；快接:盲插；漏液检测:标配；监控:SNMP/MQTT', '面议', 1, 4, '2026-02-18 10:00:00'),
(5, 'CP-Pro 铜冷板散热模组', '/uploads/product-5.jpg', '散热模组', 'CPU/GPU 专用铜冷板，低热阻高可靠。',
 '针对主流加速卡与 CPU 定制冷板，配合 CDU 实现精准温控。',
 '热阻:<0.05C/W；材质:紫铜；接口:快插；压力:耐压10bar', '咨询报价', 1, 5, '2026-02-20 10:00:00'),
(6, 'LC-6000 边缘液冷节点', '/uploads/product-1.jpg', '液冷服务器', '面向边缘智算的紧凑型液冷节点。',
 '适用于边缘机房与工业现场，体积小、噪音低、部署灵活。',
 '功率:15-30kW；部署:机柜式；运维:远程', '面议', 1, 6, '2026-03-01 10:00:00');

-- ==================== 技术 ====================
INSERT INTO technology (id, title, cover_url, icon_url, summary, content, sort_order, status, created_at) VALUES
(1, '浸没式液冷技术', '/uploads/tech-1.jpg', '/uploads/tech-1.jpg', '服务器浸没于介电冷却液，实现极致散热与静音。',
 '立冷科技浸没式液冷方案覆盖单相与两相技术路线，PUE 可低至 1.06，WUE 低于 0.5，适合超高密度算力场景。', 1, 1, '2026-01-20 10:00:00'),
(2, '冷板式液冷技术', '/uploads/tech-2.jpg', '/uploads/tech-2.jpg', '冷板贴合发热芯片，改造成本低、兼容风冷机房。',
 '冷板式方案可渐进改造现有机房，投资回报快，是企业上液冷的首选路径。', 2, 1, '2026-01-22 10:00:00'),
(3, 'AIoT 智能温控平台', '/uploads/tech-3.jpg', '/uploads/tech-3.jpg', '基于 AIoT 的流量/温度/能耗一体化调控。',
 '平台采集 CDU、泵组、机柜传感器数据，自动优化供冷策略，降低能耗并预警异常。', 3, 1, '2026-01-25 10:00:00'),
(4, 'PUE 持续优化算法', '/uploads/tech-4.jpg', '/uploads/tech-4.jpg', '数据驱动的能效优化模型，持续压降 PUE。',
 '结合历史负载与气象数据，动态调整冷却设定点，保障可靠性的同时节约运营成本。', 4, 1, '2026-01-28 10:00:00');

-- ==================== 荣誉 ====================
INSERT INTO honor (id, title, cover_url, award_year, issuer, summary, sort_order, created_at) VALUES
(1, '国家高新技术企业', '/uploads/honor-1.jpg', '2024', '科技部火炬中心', '认定立冷科技为高新技术企业，认可液冷核心技术能力。', 1, '2024-06-01 10:00:00'),
(2, '绿色数据中心优秀解决方案', '/uploads/honor-2.jpg', '2025', '中国电子节能技术协会', '液冷方案入选绿色数据中心优秀案例。', 2, '2025-05-18 10:00:00'),
(3, '算力基础设施创新奖', '/uploads/honor-3.jpg', '2025', '中国通服科技创新大会', '表彰立冷科技在液冷算力基础设施的创新贡献。', 3, '2025-11-20 10:00:00');

-- ==================== 案例 ====================
INSERT INTO case_study (id, title, cover_url, client_name, industry, summary, content, status, created_at) VALUES
(1, '某智算中心 8MW 液冷改造', '/uploads/case-1.jpg', '华东智算中心', '人工智能', '完成 8MW 冷板+浸没混合液冷改造，PUE 从 1.45 降至 1.12。',
 '项目涵盖方案设计、设备供货、施工交付与运维培训。上线后单机柜功率密度提升 3 倍，年度电费节省超过 1200 万元。', 1, '2025-08-01 10:00:00'),
(2, '商业银行同城双活数据中心', '/uploads/case-2.jpg', '某股份制银行', '金融', '同城双活机房部署 CDU 液冷系统，保障核心交易高可用。',
 '采用冷板式液冷+冗余 CDU，满足金融监管对可用性与可审计性的要求。', 1, '2025-09-15 10:00:00'),
(3, '运营商省级枢纽节能项目', '/uploads/case-3.jpg', '某省运营商', '运营商', '省级枢纽节点引入液冷，综合节能率达 28%。',
 '立冷科技提供整机柜与冷却液全包服务，3 个月完成试点并推广全省。', 1, '2025-12-01 10:00:00'),
(4, '高校超算中心扩容', '/uploads/case-4.jpg', '某重点高校', '教育科研', '超算集群扩容采用浸没式液冷，支撑科研算力翻倍。',
 '新建浸没舱 2 套，配套冷却液循环与过滤系统，噪音显著降低。', 1, '2026-01-10 10:00:00');

-- ==================== 新闻 ====================
INSERT INTO news (id, title, cover_url, summary, content, view_count, status, publish_time, created_at) VALUES
(1, '立冷科技发布《数据中心液冷技术白皮书》', '/uploads/news-1.jpg', '系统阐述浸没/冷板技术路径与落地实践。',
 '白皮书覆盖技术选型、机房改造、运维体系与能效评估，面向政企与运营商客户免费开放下载。', 1280, 1, '2026-03-01 09:00:00', '2026-03-01 09:00:00'),
(2, 'LIQUICOOL 新品发布会：CDU-500 正式上市', '/uploads/news-2.jpg', '新一代模块化 CDU 亮相，制冷量提升 20%。',
 '发布会现场演示智能温控联动与远程运维能力，吸引多家智算与金融客户到场洽谈。', 960, 1, '2026-03-10 10:00:00', '2026-03-10 10:00:00'),
(3, '立冷科技亮相绿色算力高峰论坛', '/uploads/news-3.jpg', '分享 PUE 1.06 落地经验，推动绿色算力发展。',
 '公司技术负责人作主题演讲，介绍液冷在 AI 集群中的规模化应用路径。', 750, 1, '2026-03-18 14:00:00', '2026-03-18 14:00:00'),
(4, '与某云厂商达成液冷战略合作', '/uploads/news-1.jpg', '双方将在智算园区联合推广液冷整机柜方案。',
 '合作覆盖联合方案、联合交付与联合运维，加速液冷规模化落地。', 620, 1, '2026-04-01 11:00:00', '2026-04-01 11:00:00'),
(5, '立冷科技通过 ISO27001 信息安全管理认证', '/uploads/news-2.jpg', '强化交付与运维过程的信息安全能力。',
 '认证覆盖研发、交付、运维全流程，增强政企客户信任。', 410, 1, '2026-04-12 09:30:00', '2026-04-12 09:30:00');

-- ==================== 公告 ====================
INSERT INTO notice (id, title, type, content, status, created_at) VALUES
(1, '2026 年五一假期服务安排', '系统公告', '五一期间技术支持热线保持 7x24 值班，紧急故障请拨打 400-800-6600。', 1, '2026-04-20 09:00:00'),
(2, '官网升级维护通知', '维护通知', '本周六 02:00-04:00 进行系统维护，期间门户短暂不可访问，敬请谅解。', 1, '2026-04-25 18:00:00'),
(3, '欢迎参观立冷科技液冷实验室', '活动通知', '预约参观请发送邮件至 visit@liquicool.com，我们提供现场演示与方案交流。', 1, '2026-05-01 10:00:00');

-- ==================== 咨询工单 ====================
INSERT INTO consultation (id, user_id, product_id, contact_name, phone, company, email, need_type, content, status, reply, created_at, updated_at) VALUES
(1, 2, 1, '张工程师', '13800000002', '华东智算', 'zhang@demo.com', '方案咨询', '希望了解 2MW 浸没式液冷改造可行性。', '已完成', '已安排售前工程师对接，方案已发送。', '2026-04-01 10:00:00', '2026-04-03 15:00:00'),
(2, 2, 2, '张工程师', '13800000002', '华东智算', 'zhang@demo.com', '产品询价', 'CDU-500 需要 4 台，请报价。', '处理中', '正在核算交付周期。', '2026-04-10 11:20:00', '2026-04-11 09:00:00'),
(3, 3, 4, '李运维', '13800000003', '城商行信息中心', 'li@demo.com', '机房改造', '同城双活机房计划上液冷整机柜。', '待处理', NULL, '2026-04-18 14:00:00', '2026-04-18 14:00:00'),
(4, 4, 3, '王采购', '13800000004', '省运营商', 'wang@demo.com', '采购意向', '冷却液年用量约 20 吨，请提供框架协议。', '处理中', '商务合同草稿已发。', '2026-04-20 09:30:00', '2026-04-21 10:00:00'),
(5, 5, 5, '演示账号', '13800000005', '演示公司', 'demo@liquicool.com', '技术交流', '想预约实验室参观与冷板测试。', '待处理', NULL, '2026-04-22 16:00:00', '2026-04-22 16:00:00'),
(6, 3, 1, '李运维', '13800000003', '城商行信息中心', 'li@demo.com', '售后咨询', '现有试点机柜漏液检测告警，请协助排查。', '已关闭', '现场已排查为传感器误报，固件已升级。', '2026-03-28 08:00:00', '2026-03-29 17:00:00'),
(7, 4, 2, '王采购', '13800000004', '省运营商', 'wang@demo.com', '方案咨询', '省级枢纽节点节能改造咨询。', '已完成', '已完成现场勘察与节能测算。', '2026-04-05 13:00:00', '2026-04-08 11:00:00');

-- ==================== 收藏 ====================
INSERT INTO favorite (id, user_id, target_type, target_id, created_at) VALUES
(1, 2, 'PRODUCT', 1, '2026-04-02 10:00:00'),
(2, 2, 'PRODUCT', 2, '2026-04-02 10:05:00'),
(3, 2, 'NEWS', 1, '2026-04-03 09:00:00'),
(4, 3, 'CASE', 2, '2026-04-12 11:00:00'),
(5, 3, 'PRODUCT', 4, '2026-04-12 11:10:00'),
(6, 4, 'PRODUCT', 3, '2026-04-15 15:00:00'),
(7, 5, 'NEWS', 2, '2026-04-16 16:00:00'),
(8, 5, 'CASE', 1, '2026-04-16 16:10:00');

-- ==================== 反馈 ====================
INSERT INTO feedback (id, user_id, contact_name, content, contact, status, reply, created_at) VALUES
(1, 2, '张工程师', '希望门户增加方案对比功能。', '13800000002', '已回复', '感谢建议，已纳入产品迭代计划。', '2026-04-08 10:00:00'),
(2, 3, '李运维', '后台导出咨询工单时希望支持 Excel。', '13800000003', '待处理', NULL, '2026-04-15 14:20:00'),
(3, 4, '王采购', '产品详情页规格参数建议做成表格。', '13800000004', '处理中', '正在改版中。', '2026-04-19 09:10:00');

-- ==================== 配置 ====================
INSERT INTO sys_config (id, config_key, config_value, remark) VALUES
(1, 'site.name', '立冷科技 LIQUICOOL', '网站名称'),
(2, 'site.phone', '400-800-6600', '客服电话'),
(3, 'site.email', 'contact@liquicool.com', '联系邮箱'),
(4, 'site.address', '深圳市南山区科技园立冷大厦', '公司地址'),
(5, 'site.slogan', '数据中心液冷解决方案专家', '宣传语');

-- ==================== 日志 ====================
INSERT INTO operate_log (id, user_id, username, action, module, detail, ip, created_at) VALUES
(1, 1, 'admin', '登录系统', '系统', '管理员登录成功', '127.0.0.1', '2026-04-22 09:00:00'),
(2, 1, 'admin', '更新产品', '产品', '编辑产品 LC-8000', '127.0.0.1', '2026-04-22 09:20:00'),
(3, 1, 'admin', '处理咨询', '咨询', '将工单#2 设为处理中', '127.0.0.1', '2026-04-22 10:00:00'),
(4, 2, 'user1', '提交咨询', '咨询', '提交 CDU 询价', '127.0.0.1', '2026-04-10 11:20:00'),
(5, 2, 'user1', '收藏产品', '收藏', '收藏液冷服务器', '127.0.0.1', '2026-04-02 10:00:00'),
(6, 1, 'admin', '上传文件', '文件', '上传轮播图 carousel-3.jpg', '127.0.0.1', '2026-04-21 16:00:00');

SET FOREIGN_KEY_CHECKS = 1;
