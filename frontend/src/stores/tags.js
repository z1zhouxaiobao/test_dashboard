import { defineStore } from 'pinia'

const DASHBOARD = '/admin/dashboard'
const MAX_TAGS = 10

function canKeep(tag) {
  return tag?.path && tag.path.startsWith('/admin') && tag.path !== '/admin/login'
}

/** 超出上限时优先关掉最早的非看板标签；keepPath 当前页尽量保留 */
function trimVisited(list, keepPath) {
  const tags = (list || []).filter(canKeep)
  while (tags.length > MAX_TAGS) {
    const idx = tags.findIndex(
      (v) => v.path !== DASHBOARD && v.path !== keepPath
    )
    if (idx < 0) break
    tags.splice(idx, 1)
  }
  return tags
}

export const useTagsStore = defineStore('tags', {
  state: () => ({
    visited: trimVisited(JSON.parse(sessionStorage.getItem('liquicool_tags') || '[]') || [])
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
      }
      this.visited = trimVisited(this.visited, route.path)
      this.persist()
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
