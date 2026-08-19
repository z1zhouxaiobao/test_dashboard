/**
 * 格式化日期时间
 * @param {string|number|Date} value
 * @param {string} pattern 默认 YYYY-MM-DD HH:mm:ss
 */
export function formatDateTime(value, pattern = 'YYYY-MM-DD HH:mm:ss') {
  if (!value) return '-'
  const date = value instanceof Date ? value : new Date(value)
  if (Number.isNaN(date.getTime())) return '-'

  const pad = (n) => String(n).padStart(2, '0')
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
