import { defineStore } from 'pinia'
import { authApi } from '@/api'
import { defaultAvatar } from '@/utils/media'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('liquicool_token') || '',
    user: JSON.parse(localStorage.getItem('liquicool_user') || 'null')
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ADMIN',
    avatar: (state) => state.user?.avatar || defaultAvatar(),
    displayName: (state) => state.user?.nickname || state.user?.username || '用户'
  },
  actions: {
    persist() {
      if (this.token) {
        localStorage.setItem('liquicool_token', this.token)
      } else {
        localStorage.removeItem('liquicool_token')
      }
      if (this.user) {
        localStorage.setItem('liquicool_user', JSON.stringify(this.user))
      } else {
        localStorage.removeItem('liquicool_user')
      }
    },
    async login(credentials) {
      const res = await authApi.login(credentials)
      const data = res.data || res
      this.token = data.token || ''
      this.user = data
      this.persist()
      return this.user
    },
    async register(payload) {
      const res = await authApi.register(payload)
      const data = res.data || res
      this.token = data.token || ''
      this.user = data
      this.persist()
      return this.user
    },
    async fetchMe() {
      if (!this.token) return null
      try {
        const res = await authApi.me()
        this.user = res.data || res
        this.persist()
        return this.user
      } catch {
        this.logout()
        return null
      }
    },
    async updateProfile(payload) {
      const res = await authApi.updateProfile(payload)
      this.user = { ...this.user, ...(res.data || res) }
      this.persist()
      return this.user
    },
    logout() {
      this.token = ''
      this.user = null
      this.persist()
    }
  }
})
