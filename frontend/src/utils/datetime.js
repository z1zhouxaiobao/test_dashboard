/**
 * 格式化日期时间（后端已按 Asia/Shanghai 返回墙钟时间，字符串不再经 Date 做时区换算）
 * @param {string|number|Date} value
 * @param {string} pattern 默认 YYYY-MM-DD HH:mm:ss
 */
export function formatDateTime(value, pattern = 'YYYY-MM-DD HH:mm:ss') {
  if (!value) return '-'

  const pad = (n) => String(n).padStart(2, '0')

  if (typeof value === 'string') {
    const m = value.trim().match(/^(\d{4})-(\d{2})-(\d{2})[ T](\d{2}):(\d{2})(?::(\d{2}))?/)
    if (m) {
      const map = {
        YYYY: m[1],
        MM: m[2],
        DD: m[3],
        HH: m[4],
        mm: m[5],
        ss: pad(m[6] || '00')
      }
      return pattern.replace(/YYYY|MM|DD|HH|mm|ss/g, (token) => map[token])
    }
  }

  const date = value instanceof Date ? value : new Date(value)
  if (Number.isNaN(date.getTime())) return '-'

  const map = {
    YYYY: date.getFullYear(),
    MM: pad(date.getMonth() + 1),
    DD: pad(date.getDate()),
    HH: pad(date.getHours()),
    mm: pad(date.getMinutes()),
    ss: pad(date.getSeconds())
  }

  return pattern.replace(/YYYY|MM|DD|HH|mm|ss/g, (token) => map[token])
}

export function formatDate(value) {
  return formatDateTime(value, 'YYYY-MM-DD')
}
