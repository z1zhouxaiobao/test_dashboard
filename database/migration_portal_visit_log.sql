-- 门户访问留痕表（已有库可单独执行；ddl-auto=update 也会自动建表）
CREATE TABLE IF NOT EXISTS portal_visit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  path VARCHAR(500),
  page_title VARCHAR(200),
  referer VARCHAR(500),
  ip VARCHAR(64),
  user_agent VARCHAR(500),
  locale VARCHAR(20),
  created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
