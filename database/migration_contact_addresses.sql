-- 联系页多地址：以 JSON 数组存于 sys_config.contact.addresses
-- 若已有单地址 contact.address，启动时 ensureDefaults 会自动迁移；也可手动执行：

INSERT INTO sys_config (config_key, config_value, remark)
SELECT
  'contact.addresses',
  CONCAT(
    '[{"text":"',
    REPLACE(IFNULL((SELECT config_value FROM sys_config WHERE config_key = 'contact.address' LIMIT 1), '北京市海淀区科技园区'), '"', '\\"'),
    '","textTw":"',
    REPLACE(IFNULL((SELECT config_value FROM sys_config WHERE config_key = 'contact.address.tw' LIMIT 1), '北京市海淀區科技園區'), '"', '\\"'),
    '","textEn":"',
    REPLACE(IFNULL((SELECT config_value FROM sys_config WHERE config_key = 'contact.address.en' LIMIT 1), 'Haidian Science Park, Beijing'), '"', '\\"'),
    '"}]'
  ),
  '联系页-地址列表JSON'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_config WHERE config_key = 'contact.addresses'
);
