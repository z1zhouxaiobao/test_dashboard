/**
 * 获取当前页面源码的 vscode:// 链接
 */
export function getPageSourcePath(componentName) {
  const map = {
    AdminLogin: 'src/views/AdminLogin.vue',
    Register: 'src/views/Register.vue',
    PortalHome: 'src/views/portal/Home.vue',
    AdminDashboard: 'src/views/admin/Dashboard.vue'
  }
  const rel = map[componentName] || 'src/App.vue'
  const cwd = 'E:/new_code/20260819/frontend'
  const fullPath = `${cwd}/${rel}`.replace(/\//g, '/')
  return `vscode://file/${fullPath}`
}

export function openInVscode(componentName) {
  window.open(getPageSourcePath(componentName), '_blank')
}
