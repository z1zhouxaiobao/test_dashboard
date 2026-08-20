import http from '@/utils/http'

const pageParams = (params = {}) => ({
  page: params.page ?? 1,
  size: params.size ?? 10,
  keyword: params.keyword ?? ''
})

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  register: (data) => http.post('/auth/register', data),
  me: () => http.get('/auth/me'),
  updateProfile: (data) => http.put('/user/profile', data),
  updateAvatar: (avatar) => http.put('/user/avatar', { avatar })
}

export const fileApi = {
  upload: (file) => {
    const form = new FormData()
    form.append('file', file)
    return http.post('/file/upload', form, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}

export const userApi = {
  page: (params) => http.get('/admin/users', { params: pageParams(params) }),
  create: (data) => http.post('/admin/users', data),
  update: (id, data) => http.put(`/admin/users/${id}`, data),
  remove: (id) => http.delete(`/admin/users/${id}`)
}

export const carouselApi = {
  page: (params) => http.get('/admin/carousels', { params: pageParams(params) }),
  create: (data) => http.post('/admin/carousels', data),
  update: (id, data) => http.put(`/admin/carousels/${id}`, data),
  remove: (id) => http.delete(`/admin/carousels/${id}`),
  portalList: () => http.get('/portal/carousels', { params: { page: 1, size: 10 } })
}

export const newsApi = {
  page: (params) => http.get('/admin/news', { params: pageParams(params) }),
  create: (data) => http.post('/admin/news', data),
  update: (id, data) => http.put(`/admin/news/${id}`, data),
  remove: (id) => http.delete(`/admin/news/${id}`),
  portalPage: (params) => http.get('/portal/news', { params: pageParams(params) }),
  portalDetail: (id) => http.get(`/portal/news/${id}`)
}

export const noticeApi = {
  page: (params) => http.get('/admin/notices', { params: pageParams(params) }),
  create: (data) => http.post('/admin/notices', data),
  update: (id, data) => http.put(`/admin/notices/${id}`, data),
  remove: (id) => http.delete(`/admin/notices/${id}`)
}

export const productApi = {
  page: (params) => http.get('/admin/products', { params: pageParams(params) }),
  create: (data) => http.post('/admin/products', data),
  update: (id, data) => http.put(`/admin/products/${id}`, data),
  remove: (id) => http.delete(`/admin/products/${id}`),
  portalPage: (params) =>
    http.get('/portal/products', {
      params: {
        ...pageParams(params),
        category: params.category || undefined
      }
    }),
  portalDetail: (id) => http.get(`/portal/products/${id}`)
}

export const technologyApi = {
  page: (params) => http.get('/admin/technologies', { params: pageParams(params) }),
  create: (data) => http.post('/admin/technologies', data),
  update: (id, data) => http.put(`/admin/technologies/${id}`, data),
  remove: (id) => http.delete(`/admin/technologies/${id}`),
  portalList: () => http.get('/portal/technologies', { params: { page: 1, size: 50 } })
}

export const honorApi = {
  page: (params) => http.get('/admin/honors', { params: pageParams(params) }),
  create: (data) => http.post('/admin/honors', data),
  update: (id, data) => http.put(`/admin/honors/${id}`, data),
  remove: (id) => http.delete(`/admin/honors/${id}`),
  portalList: () => http.get('/portal/honors', { params: { page: 1, size: 50 } })
}

export const caseApi = {
  page: (params) => http.get('/admin/cases', { params: pageParams(params) }),
  create: (data) => http.post('/admin/cases', data),
  update: (id, data) => http.put(`/admin/cases/${id}`, data),
  remove: (id) => http.delete(`/admin/cases/${id}`),
  portalPage: (params) =>
    http.get('/portal/cases', {
      params: {
        ...pageParams(params),
        industry: params.industry || undefined
      }
    }),
  portalDetail: (id) => http.get(`/portal/cases/${id}`)
}

export const consultationApi = {
  page: (params) => http.get('/admin/consultations', { params: pageParams(params) }),
  create: (data) => http.post('/portal/consultations', data),
  update: (id, data) => http.put(`/admin/consultations/${id}`, data),
  remove: (id) => http.delete(`/admin/consultations/${id}`),
  myPage: (params) => http.get('/user/consultations', { params: pageParams(params) })
}

export const feedbackApi = {
  page: (params) => http.get('/admin/feedbacks', { params: pageParams(params) }),
  create: (data) => http.post('/portal/feedbacks', data),
  update: (id, data) => http.put(`/admin/feedbacks/${id}`, data),
  remove: (id) => http.delete(`/admin/feedbacks/${id}`)
}

export const configApi = {
  page: (params) => http.get('/admin/configs', { params: pageParams(params) }),
  create: (data) => http.post('/admin/configs', data),
  update: (id, data) => http.put(`/admin/configs/${id}`, data),
  remove: (id) => http.delete(`/admin/configs/${id}`)
}

export const contactSettingsApi = {
  get: () => http.get('/admin/contact-settings'),
  save: (data) => http.put('/admin/contact-settings', data),
  portal: () => http.get('/portal/contact-settings')
}

export const logApi = {
  page: (params) => http.get('/admin/operate-logs', { params: pageParams(params) }),
  remove: (id) => http.delete(`/admin/operate-logs/${id}`)
}

export const dashboardApi = {
  stats: () => http.get('/admin/dashboard/stats')
}

export const navMenuApi = {
  portalTree: (moduleCode) =>
    http.get('/portal/nav-menus', { params: moduleCode ? { moduleCode } : {} }),
  page: (params) => http.get('/admin/nav-menus', { params: pageParams({ ...params, size: params.size ?? 100 }) }),
  all: () => http.get('/admin/nav-menus/all'),
  tree: () => http.get('/admin/nav-menus/tree'),
  create: (data) => http.post('/admin/nav-menus', data),
  update: (id, data) => http.put(`/admin/nav-menus/${id}`, data),
  remove: (id) => http.delete(`/admin/nav-menus/${id}`)
}
