-- CMS 内容三语字段：重启后端后 ddl-auto=update 也会自动加列；也可手动执行本脚本
-- MySQL 8

ALTER TABLE news
  ADD COLUMN title_tw VARCHAR(200) NULL AFTER title,
  ADD COLUMN title_en VARCHAR(200) NULL AFTER title_tw,
  ADD COLUMN summary_tw VARCHAR(500) NULL AFTER summary,
  ADD COLUMN summary_en VARCHAR(500) NULL AFTER summary_tw,
  ADD COLUMN content_tw TEXT NULL AFTER content,
  ADD COLUMN content_en TEXT NULL AFTER content_tw;

ALTER TABLE notice
  ADD COLUMN title_tw VARCHAR(200) NULL AFTER title,
  ADD COLUMN title_en VARCHAR(200) NULL AFTER title_tw,
  ADD COLUMN content_tw TEXT NULL AFTER content,
  ADD COLUMN content_en TEXT NULL AFTER content_tw;

ALTER TABLE product
  ADD COLUMN name_tw VARCHAR(200) NULL AFTER name,
  ADD COLUMN name_en VARCHAR(200) NULL AFTER name_tw,
  ADD COLUMN summary_tw VARCHAR(500) NULL AFTER summary,
  ADD COLUMN summary_en VARCHAR(500) NULL AFTER summary_tw,
  ADD COLUMN content_tw TEXT NULL AFTER content,
  ADD COLUMN content_en TEXT NULL AFTER content_tw;

ALTER TABLE technology
  ADD COLUMN title_tw VARCHAR(200) NULL AFTER title,
  ADD COLUMN title_en VARCHAR(200) NULL AFTER title_tw,
  ADD COLUMN summary_tw VARCHAR(500) NULL AFTER summary,
  ADD COLUMN summary_en VARCHAR(500) NULL AFTER summary_tw,
  ADD COLUMN content_tw TEXT NULL AFTER content,
  ADD COLUMN content_en TEXT NULL AFTER content_tw;

ALTER TABLE case_study
  ADD COLUMN title_tw VARCHAR(200) NULL AFTER title,
  ADD COLUMN title_en VARCHAR(200) NULL AFTER title_tw,
  ADD COLUMN industry_tw VARCHAR(100) NULL AFTER industry,
  ADD COLUMN industry_en VARCHAR(100) NULL AFTER industry_tw,
  ADD COLUMN summary_tw VARCHAR(500) NULL AFTER summary,
  ADD COLUMN summary_en VARCHAR(500) NULL AFTER summary_tw,
  ADD COLUMN content_tw TEXT NULL AFTER content,
  ADD COLUMN content_en TEXT NULL AFTER content_tw;

ALTER TABLE carousel
  ADD COLUMN title_tw VARCHAR(200) NULL AFTER title,
  ADD COLUMN title_en VARCHAR(200) NULL AFTER title_tw,
  ADD COLUMN subtitle_tw VARCHAR(500) NULL AFTER subtitle,
  ADD COLUMN subtitle_en VARCHAR(500) NULL AFTER subtitle_tw;
