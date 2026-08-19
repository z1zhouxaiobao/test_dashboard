const API_BASE = '/api'

export function resolveMediaUrl(url) {
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://') || url.startsWith('data:')) {
    return url
  }
  if (url.startsWith('/uploads')) {
    return url
  }
  if (url.startsWith('/')) {
    return url
  }
  return `${API_BASE}/uploads/${url}`
}

export function defaultAvatar() {
  return '/images/avatar-default.png'
}

export function defaultCover(type = 'product') {
  const map = {
    product: '/images/product-default.jpg',
    news: '/images/news-default.jpg',
    case: '/images/case-default.jpg',
    honor: '/images/honor-default.jpg',
    carousel: '/images/carousel-1.jpg'
  }
  return map[type] || '/images/product-default.jpg'
}
