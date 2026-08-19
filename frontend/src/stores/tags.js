import { defineStore } from 'pinia'

const DASHBOARD = '/admin/dashboard'

function canKeep(tag) {
  return tag?.path && tag.path.startsWith('/admin') && tag.path !== '/admin/login'
}

export const useTagsStore = defineStore('tags', {
  state: () => ({
    visited: (JSON.parse(sessionStorage.getItem('liquicool_tags') || '[]') || []).filter(canKeep)
  }),
  actions: {
    persist() {
      sessionStorage.setItem('liquicool_tags', JSON.stringify(this.visited))
    },
    addView(route) {
      if (!canKeep({ path: route.path }) || !route.meta?.title) return
      const exists = this.visited.some((v) => v.path === route.path)
      if (!exists) {
        this.visited.push({
          path: route.path,
          title: route.meta.title,
          name: route.name
        })
        this.persist()
      }
    },
    removeView(path) {
      this.visited = this.visited.filter((v) => v.path !== path)
      this.persist()
    },
    removeOthers(path) {
      this.visited = this.visited.filter((v) => v.path === path || v.path === DASHBOARD)
      this.persist()
    },
    removeLeft(path) {
      const idx = this.visited.findIndex((v) => v.path === path)
      if (idx <= 0) return
      this.visited = this.visited.filter((v, i) => i >= idx || v.path === DASHBOARD)
      this.persist()
    },
    removeRight(path) {
      const idx = this.visited.findIndex((v) => v.path === path)
      if (idx < 0) return
      this.visited = this.visited.filter((v, i) => i <= idx || v.path === DASHBOARD)
      this.persist()
    },
    closeAll() {
      this.visited = this.visited.filter((v) => v.path === DASHBOARD)
      if (!this.visited.some((v) => v.path === DASHBOARD)) {
        this.visited = [{ path: DASHBOARD, title: 'Dashboard', name: 'AdminDashboard' }]
      }
      this.persist()
    }
  }
})
