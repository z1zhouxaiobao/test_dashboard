-- 立冷科技 LIQUICOOL 数据库初始化
-- MySQL 8 / root / 123456
-- 密码均为 MD5(123456) = e10adc3949ba59abbe56e057f20f883e

CREATE DATABASE IF NOT EXISTS liquicool DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE liquicool;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS portal_visit_log;
DROP TABLE IF EXISTS operate_log;
DROP TABLE IF EXISTS sys_config;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS consultation;
DROP TABLE IF EXISTS case_study;
DROP TABLE IF EXISTS honor;
DROP TABLE IF EXISTS technology;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS carousel;
DROP TABLE IF EXISTS nav_menu;
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
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  subtitle VARCHAR(500),
  subtitle_tw VARCHAR(500),
  subtitle_en VARCHAR(500),
  image_url VARCHAR(500),
  link_url VARCHAR(500),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE news (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  cover_url VARCHAR(500),
  summary VARCHAR(500),
  summary_tw VARCHAR(500),
  summary_en VARCHAR(500),
  content TEXT,
  content_tw TEXT,
  content_en TEXT,
  view_count INT DEFAULT 0,
  status INT DEFAULT 1,
  publish_time DATETIME,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE notice (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  type VARCHAR(50),
  content TEXT,
  content_tw TEXT,
  content_en TEXT,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200),
  name_tw VARCHAR(200),
  name_en VARCHAR(200),
  cover_url VARCHAR(500),
  category VARCHAR(80),
  summary VARCHAR(500),
  summary_tw VARCHAR(500),
  summary_en VARCHAR(500),
  content TEXT,
  content_tw TEXT,
  content_en TEXT,
  specs TEXT,
  price_hint VARCHAR(100),
  status INT DEFAULT 1,
  sort_order INT DEFAULT 0,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE nav_menu (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT NULL,
  module_code VARCHAR(40) NOT NULL,
  level_no INT NOT NULL DEFAULT 1,
  name_zh VARCHAR(100) NOT NULL,
  name_tw VARCHAR(100),
  name_en VARCHAR(100),
  code VARCHAR(80),
  link_path VARCHAR(255),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME,
  INDEX idx_nav_parent (parent_id),
  INDEX idx_nav_module (module_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE technology (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  cover_url VARCHAR(500),
  icon_url VARCHAR(500),
  summary VARCHAR(500),
  summary_tw VARCHAR(500),
  summary_en VARCHAR(500),
  content TEXT,
  content_tw TEXT,
  content_en TEXT,
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE honor (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  cover_url VARCHAR(500),
  award_year VARCHAR(20),
  issuer VARCHAR(200),
  issuer_tw VARCHAR(200),
  issuer_en VARCHAR(200),
  summary VARCHAR(500),
  summary_tw VARCHAR(500),
  summary_en VARCHAR(500),
  sort_order INT DEFAULT 0,
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE case_study (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200),
  title_tw VARCHAR(200),
  title_en VARCHAR(200),
  cover_url VARCHAR(500),
  client_name VARCHAR(100),
  industry VARCHAR(100),
  industry_tw VARCHAR(100),
  industry_en VARCHAR(100),
  summary VARCHAR(500),
  summary_tw VARCHAR(500),
  summary_en VARCHAR(500),
  content TEXT,
  content_tw TEXT,
  content_en TEXT,
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

CREATE TABLE portal_visit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  path VARCHAR(500),
  page_title VARCHAR(200),
  referer VARCHAR(500),
  ip VARCHAR(64),
  user_agent VARCHAR(500),
  locale VARCHAR(20),
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==================== 用户 ====================
INSERT INTO sys_user (id, username, password, nickname, avatar, phone, email, role, status, created_at, updated_at) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '/uploads/avatar-admin.png', '13800000001', 'admin@liquicool.com', 'ADMIN', 1, '2026-01-10 09:00:00', '2026-01-10 09:00:00'),
(2, 'user1', 'e10adc3949ba59abbe56e057f20f883e', '张工程师', '/uploads/avatar-user1.png', '13800000002', 'zhang@demo.com', 'USER', 1, '2026-02-01 10:00:00', '2026-02-01 10:00:00'),
(3, 'user2', 'e10adc3949ba59abbe56e057f20f883e', '李运维', '/uploads/avatar-default.png', '13800000003', 'li@demo.com', 'USER', 1, '2026-02-15 11:00:00', '2026-02-15 11:00:00'),
(4, 'user3', 'e10adc3949ba59abbe56e057f20f883e', '王采购', '/uploads/avatar-default.png', '13800000004', 'wang@demo.com', 'USER', 1, '2026-03-01 14:00:00', '2026-03-01 14:00:00'),
(5, 'demo', 'e10adc3949ba59abbe56e057f20f883e', '演示账号', '/uploads/avatar-default.png', '13800000005', 'demo@liquicool.com', 'USER', 1, '2026-03-20 16:00:00', '2026-03-20 16:00:00');

-- ==================== 导航菜单（一级/二级/三级） ====================
INSERT INTO nav_menu (id, parent_id, module_code, level_no, name_zh, name_tw, name_en, code, link_path, sort_order, status, created_at, updated_at) VALUES
-- 一级
(1, NULL, 'PRODUCTS', 1, '产品与服务中心', '產品與服務中心', 'Products & Services', 'products', '/portal/products', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(2, NULL, 'SOLUTIONS', 1, '解决方案', '解決方案', 'Solutions', 'solutions', '/portal/solutions', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(3, NULL, 'NEWS', 1, '新闻', '新聞', 'News', 'news', '/portal/news', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(4, NULL, 'ABOUT', 1, '关于', '關於', 'About', 'about', '/portal/about', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(5, NULL, 'CONTACT', 1, '联系我们', '聯絡我們', 'Contact', 'contact', '/portal/contact', 5, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
-- 产品二级
(10, 1, 'PRODUCTS', 2, '液冷产品', '液冷產品', 'Liquid Cooling Products', 'lc-products', '/portal/products', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(11, 1, 'PRODUCTS', 2, '热管理技术', '熱管理技術', 'Thermal Management', 'thermal', '/portal/technologies', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(12, 1, 'PRODUCTS', 2, '工程与服务', '工程與服務', 'Engineering & Services', 'services', '/portal/contact', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
-- 产品三级
(101, 10, 'PRODUCTS', 3, '液冷服务器', '液冷伺服器', 'Liquid Cooled Servers', '液冷服务器', '/portal/products?category=%E6%B6%B2%E5%86%B7%E6%9C%8D%E5%8A%A1%E5%99%A8', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(102, 10, 'PRODUCTS', 3, 'CDU冷量分配单元', 'CDU冷量分配單元', 'CDU Units', 'CDU冷量分配单元', '/portal/products?category=CDU%E5%86%B7%E9%87%8F%E5%88%86%E9%85%8D%E5%8D%95%E5%85%83', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(103, 10, 'PRODUCTS', 3, '冷却液', '冷卻液', 'Coolants', '冷却液', '/portal/products?category=%E5%86%B7%E5%8D%B4%E6%B6%B2', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(104, 10, 'PRODUCTS', 3, '机柜系统', '機櫃系統', 'Rack Systems', '机柜系统', '/portal/products?category=%E6%9C%BA%E6%9F%9C%E7%B3%BB%E7%BB%9F', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(105, 10, 'PRODUCTS', 3, '散热模组', '散熱模組', 'Cold Plates', '散热模组', '/portal/products?category=%E6%95%A3%E7%83%AD%E6%A8%A1%E7%BB%84', 5, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(111, 11, 'PRODUCTS', 3, '浸没式液冷', '浸沒式液冷', 'Immersion Cooling', 'immersion', '/portal/technologies', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(112, 11, 'PRODUCTS', 3, '冷板式液冷', '冷板式液冷', 'Cold Plate Cooling', 'cold-plate', '/portal/technologies', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(113, 11, 'PRODUCTS', 3, '智能温控平台', '智慧溫控平台', 'Smart Thermal Control', 'aiot', '/portal/technologies', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(121, 12, 'PRODUCTS', 3, '方案设计', '方案設計', 'Solution Design', 'design', '/portal/contact', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(122, 12, 'PRODUCTS', 3, '交付实施', '交付實施', 'Delivery', 'delivery', '/portal/contact', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(123, 12, 'PRODUCTS', 3, '运维优化', '運維優化', 'O&M Optimization', 'om', '/portal/contact', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
-- 解决方案二级
(20, 2, 'SOLUTIONS', 2, '按场景', '按場景', 'By Scenario', 'by-scenario', '/portal/solutions', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(21, 2, 'SOLUTIONS', 2, '按行业', '按行業', 'By Industry', 'by-industry', '/portal/solutions', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(22, 2, 'SOLUTIONS', 2, '了解更多', '瞭解更多', 'Learn More', 'learn-more', '/portal/solutions', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
-- 解决方案三级
(201, 20, 'SOLUTIONS', 3, '高密度算力机房', '高密度算力機房', 'High-Density Computing', '高密度算力', '/portal/cases', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(202, 20, 'SOLUTIONS', 3, '绿色数据中心', '綠色資料中心', 'Green Data Center', '绿色数据中心', '/portal/cases', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(203, 20, 'SOLUTIONS', 3, '边缘计算节点', '邊緣計算節點', 'Edge Computing', '边缘计算', '/portal/cases', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(211, 21, 'SOLUTIONS', 3, '人工智能', '人工智慧', 'AI', '人工智能', '/portal/cases?industry=%E4%BA%BA%E5%B7%A5%E6%99%BA%E8%83%BD', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(212, 21, 'SOLUTIONS', 3, '金融', '金融', 'Finance', '金融', '/portal/cases?industry=%E9%87%91%E8%9E%8D', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(213, 21, 'SOLUTIONS', 3, '运营商', '電信運營商', 'Telecom', '运营商', '/portal/cases?industry=%E8%BF%90%E8%90%A5%E5%95%86', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(214, 21, 'SOLUTIONS', 3, '教育科研', '教育科研', 'Education & Research', '教育科研', '/portal/cases?industry=%E6%95%99%E8%82%B2%E7%A7%91%E7%A0%94', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(221, 22, 'SOLUTIONS', 3, '数据中心液冷技术', '資料中心液冷技術', 'DC Liquid Cooling', 'tech-lc', '/portal/technologies', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(222, 22, 'SOLUTIONS', 3, '成功案例总览', '成功案例總覽', 'Case Studies', 'cases-all', '/portal/cases', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
-- 关于二级（也作为三级入口）
(40, 4, 'ABOUT', 2, '公司简介', '公司簡介', 'Company Profile', 'about-company', '/portal/about', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(41, 4, 'ABOUT', 2, '加入我们', '加入我們', 'Join Us', 'about-join', '/portal/join', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(42, 4, 'ABOUT', 2, '获得奖项', '獲得獎項', 'Awards', 'about-awards', '/portal/honors', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00');

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
INSERT INTO honor (id, title, title_tw, title_en, cover_url, award_year, issuer, issuer_tw, issuer_en, summary, summary_tw, summary_en, sort_order, created_at) VALUES
(1, '国家高新技术企业', '國家高新技術企業', 'National High-tech Enterprise', '/uploads/honor-1.jpg', '2024',
 '科技部火炬中心', '科技部火炬中心', 'Torch Center, MOST',
 '认定立冷科技为高新技术企业，认可液冷核心技术能力。', '認定立冷科技為高新技術企業，認可液冷核心技術能力。', 'Recognized Liquicool as a high-tech enterprise for core liquid cooling capabilities.',
 1, '2024-06-01 10:00:00'),
(2, '绿色数据中心优秀解决方案', '綠色數據中心優秀解決方案', 'Outstanding Green Data Center Solution', '/uploads/honor-2.jpg', '2025',
 '中国电子节能技术协会', '中國電子節能技術協會', 'China Electronics Energy Conservation Association',
 '液冷方案入选绿色数据中心优秀案例。', '液冷方案入選綠色數據中心優秀案例。', 'Liquid cooling solution selected as an outstanding green data center case.',
 2, '2025-05-18 10:00:00'),
(3, '算力基础设施创新奖', '算力基礎設施創新獎', 'Computing Infrastructure Innovation Award', '/uploads/honor-3.jpg', '2025',
 '中国通服科技创新大会', '中國通服科技創新大會', 'China Comservice Innovation Conference',
 '表彰立冷科技在液冷算力基础设施的创新贡献。', '表彰立冷科技在液冷算力基礎設施的創新貢獻。', 'Honoring Liquicool’s innovation in liquid-cooled computing infrastructure.',
 3, '2025-11-20 10:00:00');

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
(5, 'site.slogan', '数据中心液冷解决方案专家', '宣传语'),
(6, 'contact.talk_now', '即刻对话', '联系页-即刻对话标题'),
(7, 'contact.presales.title', '售前人工客服', '联系页-售前标题'),
(8, 'contact.presales.desc', '售前咨询、方案选型与商务对接，工作日人工客服在线响应。', '联系页-售前说明'),
(9, 'contact.presales.phone', '400-888-0000', '联系页-售前电话'),
(10, 'contact.presales.btn', '售前人工客服', '联系页-售前按钮'),
(11, 'contact.aftersales.title', '售后技术支持', '联系页-售后标题'),
(12, 'contact.aftersales.desc', '已购产品的技术支持、故障排查与运维协助，智能与人工协同服务。', '联系页-售后说明'),
(13, 'contact.aftersales.phone', '400-888-0001', '联系页-售后电话'),
(14, 'contact.aftersales.btn', '售后技术支持', '联系页-售后按钮'),
(15, 'contact.support_heading', '获取产品和服务支持', '联系页-支持区标题'),
(16, 'contact.email', 'contact@liquicool.com', '联系页-邮箱'),
(17, 'contact.address', '北京市海淀区科技园区', '联系页-地址'),
(18, 'contact.company_phone', '400-888-0000', '联系页-公司电话');

-- ==================== 日志 ====================
INSERT INTO operate_log (id, user_id, username, action, module, detail, ip, created_at) VALUES
(1, 1, 'admin', '登录系统', '系统', '管理员登录成功', '127.0.0.1', '2026-04-22 09:00:00'),
(2, 1, 'admin', '更新产品', '产品', '编辑产品 LC-8000', '127.0.0.1', '2026-04-22 09:20:00'),
(3, 1, 'admin', '处理咨询', '咨询', '将工单#2 设为处理中', '127.0.0.1', '2026-04-22 10:00:00'),
(4, 2, 'user1', '提交咨询', '咨询', '提交 CDU 询价', '127.0.0.1', '2026-04-10 11:20:00'),
(5, 2, 'user1', '提交反馈', '反馈', '提交门户功能建议', '127.0.0.1', '2026-04-08 10:00:00'),
(6, 1, 'admin', '上传文件', '文件', '上传轮播图 carousel-3.jpg', '127.0.0.1', '2026-04-21 16:00:00');

SET FOREIGN_KEY_CHECKS = 1;
