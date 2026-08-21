import * as OpenCC from 'opencc-js'

const toTw = OpenCC.Converter({ from: 'cn', to: 'tw' })

/** 简体 → 繁体（台湾） */
export function toTraditional(text) {
  if (!text || !String(text).trim()) return ''
  return toTw(String(text))
}

/**
 * 中文 → 英文（MyMemory 免费接口，失败时返回空串）
 * 长文本分段翻译后拼接
 */
export async function toEnglish(text) {
  const src = String(text || '').trim()
  if (!src) return ''

  const chunks = splitText(src, 450)
  const parts = []
  for (const chunk of chunks) {
    const translated = await translateChunk(chunk)
    if (!translated) return ''
    parts.push(translated)
  }
  return parts.join('\n').trim()
}

function splitText(text, maxLen) {
  if (text.length <= maxLen) return [text]
  const chunks = []
  let rest = text
  while (rest.length > maxLen) {
    let cut = rest.lastIndexOf('\n', maxLen)
    if (cut < maxLen * 0.4) cut = rest.lastIndexOf('。', maxLen)
    if (cut < maxLen * 0.4) cut = rest.lastIndexOf('，', maxLen)
    if (cut < maxLen * 0.4) cut = maxLen
    chunks.push(rest.slice(0, cut + 1).trim())
    rest = rest.slice(cut + 1).trim()
  }
  if (rest) chunks.push(rest)
  return chunks
}

async function translateChunk(text) {
  const url =
    'https://api.mymemory.translated.net/get?q=' +
    encodeURIComponent(text) +
    '&langpair=zh-CN|en'
  try {
    const res = await fetch(url)
    if (!res.ok) return ''
    const data = await res.json()
    const out = data?.responseData?.translatedText
    if (!out || /MYMEMORY WARNING/i.test(out)) return ''
    return String(out).trim()
  } catch {
    return ''
  }
}
