-- 已有库补丁：创建导航表并写入一/二/三级菜单
-- 用法: mysql -uroot -p123456 liquicool < database/patch_nav_menu.sql

USE liquicool;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS nav_menu (
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

DELETE FROM nav_menu;

INSERT INTO nav_menu (id, parent_id, module_code, level_no, name_zh, name_tw, name_en, code, link_path, sort_order, status, created_at, updated_at) VALUES
(1, NULL, 'PRODUCTS', 1, '产品与服务中心', '產品與服務中心', 'Products & Services', 'products', '/portal/products', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(2, NULL, 'SOLUTIONS', 1, '解决方案', '解決方案', 'Solutions', 'solutions', '/portal/solutions', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(3, NULL, 'NEWS', 1, '新闻', '新聞', 'News', 'news', '/portal/news', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(4, NULL, 'ABOUT', 1, '关于', '關於', 'About', 'about', '/portal/about', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(5, NULL, 'CONTACT', 1, '联系我们', '聯絡我們', 'Contact', 'contact', '/portal/contact', 5, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(10, 1, 'PRODUCTS', 2, '液冷产品', '液冷產品', 'Liquid Cooling Products', 'lc-products', '/portal/products', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(11, 1, 'PRODUCTS', 2, '热管理技术', '熱管理技術', 'Thermal Management', 'thermal', '/portal/technologies', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(12, 1, 'PRODUCTS', 2, '工程与服务', '工程與服務', 'Engineering & Services', 'services', '/portal/contact', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(101, 10, 'PRODUCTS', 3, '液冷服务器', '液冷伺服器', 'Liquid Cooled Servers', '液冷服务器', '/portal/products?category=液冷服务器', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(102, 10, 'PRODUCTS', 3, 'CDU冷量分配单元', 'CDU冷量分配單元', 'CDU Units', 'CDU冷量分配单元', '/portal/products?category=CDU冷量分配单元', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(103, 10, 'PRODUCTS', 3, '冷却液', '冷卻液', 'Coolants', '冷却液', '/portal/products?category=冷却液', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(104, 10, 'PRODUCTS', 3, '机柜系统', '機櫃系統', 'Rack Systems', '机柜系统', '/portal/products?category=机柜系统', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(105, 10, 'PRODUCTS', 3, '散热模组', '散熱模組', 'Cold Plates', '散热模组', '/portal/products?category=散热模组', 5, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(111, 11, 'PRODUCTS', 3, '浸没式液冷', '浸沒式液冷', 'Immersion Cooling', 'immersion', '/portal/technologies', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(112, 11, 'PRODUCTS', 3, '冷板式液冷', '冷板式液冷', 'Cold Plate Cooling', 'cold-plate', '/portal/technologies', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(113, 11, 'PRODUCTS', 3, '智能温控平台', '智慧溫控平台', 'Smart Thermal Control', 'aiot', '/portal/technologies', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(121, 12, 'PRODUCTS', 3, '方案设计', '方案設計', 'Solution Design', 'design', '/portal/contact', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(122, 12, 'PRODUCTS', 3, '交付实施', '交付實施', 'Delivery', 'delivery', '/portal/contact', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(123, 12, 'PRODUCTS', 3, '运维优化', '運維優化', 'O&M Optimization', 'om', '/portal/contact', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(20, 2, 'SOLUTIONS', 2, '按场景', '按場景', 'By Scenario', 'by-scenario', '/portal/solutions', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(21, 2, 'SOLUTIONS', 2, '按行业', '按行業', 'By Industry', 'by-industry', '/portal/solutions', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(22, 2, 'SOLUTIONS', 2, '了解更多', '瞭解更多', 'Learn More', 'learn-more', '/portal/solutions', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(201, 20, 'SOLUTIONS', 3, '高密度算力机房', '高密度算力機房', 'High-Density Computing', '高密度算力', '/portal/cases', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(202, 20, 'SOLUTIONS', 3, '绿色数据中心', '綠色資料中心', 'Green Data Center', '绿色数据中心', '/portal/cases', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(203, 20, 'SOLUTIONS', 3, '边缘计算节点', '邊緣計算節點', 'Edge Computing', '边缘计算', '/portal/cases', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(211, 21, 'SOLUTIONS', 3, '人工智能', '人工智慧', 'AI', '人工智能', '/portal/cases?industry=人工智能', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(212, 21, 'SOLUTIONS', 3, '金融', '金融', 'Finance', '金融', '/portal/cases?industry=金融', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(213, 21, 'SOLUTIONS', 3, '运营商', '電信運營商', 'Telecom', '运营商', '/portal/cases?industry=运营商', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(214, 21, 'SOLUTIONS', 3, '教育科研', '教育科研', 'Education & Research', '教育科研', '/portal/cases?industry=教育科研', 4, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(221, 22, 'SOLUTIONS', 3, '数据中心液冷技术', '資料中心液冷技術', 'DC Liquid Cooling', 'tech-lc', '/portal/technologies', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(222, 22, 'SOLUTIONS', 3, '成功案例总览', '成功案例總覽', 'Case Studies', 'cases-all', '/portal/cases', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(40, 4, 'ABOUT', 2, '公司简介', '公司簡介', 'Company Profile', 'about-company', '/portal/about', 1, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(41, 4, 'ABOUT', 2, '加入我们', '加入我們', 'Join Us', 'about-join', '/portal/join', 2, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00'),
(42, 4, 'ABOUT', 2, '获得奖项', '獲得獎項', 'Awards', 'about-awards', '/portal/honors', 3, 1, '2026-08-20 10:00:00', '2026-08-20 10:00:00');
