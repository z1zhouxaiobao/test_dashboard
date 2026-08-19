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
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        const auth = useAuthStore()
        auth.logout()
        const path = router.currentRoute.value.path
        router.push(path.startsWith('/admin') ? '/admin/login' : '/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '网络错误'
    ElMessage.error(message)
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
