-- 先重启后端（ddl-auto=update 会自动增加 title_tw/title_en 等列），再执行本脚本填充演示英文/繁体
UPDATE honor SET
  title_tw = '國家高新技術企業',
  title_en = 'National High-tech Enterprise',
  issuer_tw = '科技部火炬中心',
  issuer_en = 'Torch Center, MOST',
  summary_tw = '認定立冷科技為高新技術企業，認可液冷核心技術能力。',
  summary_en = 'Recognized Liquicool as a high-tech enterprise for core liquid cooling capabilities.'
WHERE id = 1;

UPDATE honor SET
  title_tw = '綠色數據中心優秀解決方案',
  title_en = 'Outstanding Green Data Center Solution',
  issuer_tw = '中國電子節能技術協會',
  issuer_en = 'China Electronics Energy Conservation Association',
  summary_tw = '液冷方案入選綠色數據中心優秀案例。',
  summary_en = 'Liquid cooling solution selected as an outstanding green data center case.'
WHERE id = 2;

UPDATE honor SET
  title_tw = '算力基礎設施創新獎',
  title_en = 'Computing Infrastructure Innovation Award',
  issuer_tw = '中國通服科技創新大會',
  issuer_en = 'China Comservice Innovation Conference',
  summary_tw = '表彰立冷科技在液冷算力基礎設施的創新貢獻。',
  summary_en = 'Honoring Liquicool’s innovation in liquid-cooled computing infrastructure.'
WHERE id = 3;
