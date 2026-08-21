/**
 * 按当前语言取后台配置的多语言字段。
 * 约定：field 为中文默认字段名，对应 fieldTw / fieldEn。
 * 例：localizedText(item, 'title', 'en') → item.titleEn || item.title
 */
export function localizedText(item, field, locale) {
  if (!item || !field) return ''
  const zh = item[field]
  if (locale === 'en') {
    return item[`${field}En`] || zh || ''
  }
  if (locale === 'zh-TW') {
    return item[`${field}Tw`] || zh || ''
  }
  return zh || ''
}
