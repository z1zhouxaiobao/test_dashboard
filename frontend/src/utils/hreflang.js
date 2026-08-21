/**
 * 门户多语言 hreflang / html lang
 * 使用 ?lang=zh-CN|zh-TW|en 作为可被搜索引擎区分的语言 URL
 */

export const HREFLANG_LOCALES = [
  { locale: 'zh-CN', hreflang: 'zh-CN' },
  { locale: 'zh-TW', hreflang: 'zh-TW' },
  { locale: 'en', hreflang: 'en' }
]

export const DEFAULT_LOCALE = 'zh-CN'

export function isValidLocale(lang) {
  return HREFLANG_LOCALES.some((item) => item.locale === lang)
}

/** 从当前地址生成带 lang 的绝对 URL */
export function buildLangUrl(pathWithQuery, lang) {
  if (typeof window === 'undefined') return ''
  const origin = window.location.origin
  const url = new URL(pathWithQuery || '/', origin)
  // 仅保留 path + 非 lang 的 query，再写入目标语言
  const params = new URLSearchParams(url.search)
  params.set('lang', lang)
  const qs = params.toString()
  return `${origin}${url.pathname}${qs ? `?${qs}` : ''}`
}

export function syncHtmlLang(locale) {
  if (typeof document === 'undefined') return
  const map = { 'zh-CN': 'zh-CN', 'zh-TW': 'zh-TW', en: 'en' }
  document.documentElement.lang = map[locale] || 'zh-CN'
}

/**
 * 在 <head> 注入/更新 alternate hreflang 与 canonical
 * @param {string} fullPath 当前路由 fullPath（含 query）
 * @param {boolean} enabled 是否门户页
 */
export function updateHreflangLinks(fullPath, enabled) {
  if (typeof document === 'undefined') return
  const head = document.head
  head.querySelectorAll('link[data-hreflang]').forEach((el) => el.remove())
  if (!enabled) return

  const path = fullPath || window.location.pathname + window.location.search

  HREFLANG_LOCALES.forEach(({ locale, hreflang }) => {
    const link = document.createElement('link')
    link.setAttribute('rel', 'alternate')
    link.setAttribute('hreflang', hreflang)
    link.setAttribute('href', buildLangUrl(path, locale))
    link.setAttribute('data-hreflang', hreflang)
    head.appendChild(link)
  })

  const xDefault = document.createElement('link')
  xDefault.setAttribute('rel', 'alternate')
  xDefault.setAttribute('hreflang', 'x-default')
  xDefault.setAttribute('href', buildLangUrl(path, DEFAULT_LOCALE))
  xDefault.setAttribute('data-hreflang', 'x-default')
  head.appendChild(xDefault)

  // canonical 指向当前语言版本
  let canonical = head.querySelector('link[data-canonical="hreflang"]')
  if (!canonical) {
    canonical = document.createElement('link')
    canonical.setAttribute('rel', 'canonical')
    canonical.setAttribute('data-canonical', 'hreflang')
    head.appendChild(canonical)
  }
  const currentLang = new URL(path, window.location.origin).searchParams.get('lang')
  const lang = isValidLocale(currentLang) ? currentLang : DEFAULT_LOCALE
  canonical.setAttribute('href', buildLangUrl(path, lang))
}
