import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000
})

http.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res && typeof res.code !== 'undefined' && res.code !== 200) {
      const message = res.message || '请求失败'
      if (!response.config?.skipErrorToast) {
        ElMessage.error(message)
      }
      if (res.code === 401) {
        const auth = useAuthStore()
        auth.logout()
        const path = router.currentRoute.value.path
        router.push(path.startsWith('/admin') ? '/admin/login' : '/login')
      }
      return Promise.reject(new Error(message))
    }
    return res
  },
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '网络错误'
    if (!shouldSkipErrorToast(message) && !error.config?.skipErrorToast) {
      ElMessage.error(message)
    }
    if (status === 401) {
      const auth = useAuthStore()
      auth.logout()
      const path = router.currentRoute.value.path
      router.push(path.startsWith('/admin') ? '/admin/login' : '/login')
    }
    return Promise.reject(error)
  }
)

export default http
