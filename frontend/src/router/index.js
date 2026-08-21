import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useTagsStore } from '@/stores/tags'
import { useLocaleStore } from '@/stores/locale'
import { adminTitleKeys } from '@/i18n/admin'
import { visitApi } from '@/api'
import { ElMessage } from 'element-plus'

const Login = () => import('@/views/Login.vue')
const Register = () => import('@/views/Register.vue')

const routes = [
  { path: '/', redirect: '/portal/home' },
  { path: '/portal/profile', redirect: '/portal/home' },
  { path: '/login', name: 'Login', component: Login, meta: { public: true, title: '用户登录', titleKey: 'login' } },
  { path: '/register', name: 'Register', component: Register, meta: { public: true, title: '注册', titleKey: 'registerTitle' } },
  { path: '/admin/login', name: 'AdminLogin', component: () => import('@/views/AdminLogin.vue'), meta: { public: true, title: '管理员登录', titleKey: 'adminLoginTitle' } },
  {
    path: '/portal',
    component: () => import('@/layouts/PortalLayout.vue'),
    meta: { public: true },
    children: [
      { path: '', redirect: '/portal/home' },
      { path: 'home', name: 'PortalHome', component: () => import('@/views/portal/Home.vue'), meta: { public: true, title: '首页', titleKey: 'navHome' } },
      { path: 'about', name: 'PortalAbout', component: () => import('@/views/portal/About.vue'), meta: { public: true, title: '公司简介', titleKey: 'aboutPageTitle' } },
      { path: 'join', name: 'PortalJoin', component: () => import('@/views/portal/Join.vue'), meta: { public: true, title: '加入我们', titleKey: 'joinUs' } },
      { path: 'technologies', name: 'PortalTechnologies', component: () => import('@/views/portal/Technologies.vue'), meta: { public: true, title: '核心技术', titleKey: 'techTitle' } },
      { path: 'products', name: 'PortalProducts', component: () => import('@/views/portal/Products.vue'), meta: { public: true, title: '产品与服务', titleKey: 'productsTitle' } },
      { path: 'products/:id', name: 'PortalProductDetail', component: () => import('@/views/portal/ProductDetail.vue'), meta: { public: true, title: '产品详情', titleKey: 'productDetailTitle' } },
      { path: 'solutions', name: 'PortalSolutions', component: () => import('@/views/portal/Solutions.vue'), meta: { public: true, title: '解决方案', titleKey: 'solutionsTitle' } },
      { path: 'honors', name: 'PortalHonors', component: () => import('@/views/portal/Honors.vue'), meta: { public: true, title: '获得奖项', titleKey: 'honorTitle' } },
      { path: 'cases', name: 'PortalCases', component: () => import('@/views/portal/Cases.vue'), meta: { public: true, title: '成功案例', titleKey: 'casesTitle' } },
      { path: 'cases/:id', name: 'PortalCaseDetail', component: () => import('@/views/portal/CaseDetail.vue'), meta: { public: true, title: '案例详情', titleKey: 'caseDetailTitle' } },
      { path: 'news', name: 'PortalNews', component: () => import('@/views/portal/News.vue'), meta: { public: true, title: '新闻', titleKey: 'newsTitle' } },
      { path: 'news/:id', name: 'PortalNewsDetail', component: () => import('@/views/portal/NewsDetail.vue'), meta: { public: true, title: '新闻详情', titleKey: 'newsDetailTitle' } },
      { path: 'contact', name: 'PortalContact', component: () => import('@/views/portal/Contact.vue'), meta: { public: true, title: '联系我们', titleKey: 'contactTitle' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { requiresAdmin: true, title: '数据看板' } },
      { path: 'nav-menus', name: 'AdminNavMenus', component: () => import('@/views/admin/NavMenus.vue'), meta: { requiresAdmin: true, title: '前台菜单' } },
      { path: 'carousels', name: 'AdminCarousels', component: () => import('@/views/admin/Carousels.vue'), meta: { requiresAdmin: true, title: '首页轮播' } },
      { path: 'news', name: 'AdminNews', component: () => import('@/views/admin/News.vue'), meta: { requiresAdmin: true, title: '新闻列表' } },
      { path: 'notices', name: 'AdminNotices', component: () => import('@/views/admin/Notices.vue'), meta: { requiresAdmin: true, title: '公告通知' } },
      { path: 'technologies', name: 'AdminTechnologies', component: () => import('@/views/admin/Technologies.vue'), meta: { requiresAdmin: true, title: '热管理技术' } },
      { path: 'honors', name: 'AdminHonors', component: () => import('@/views/admin/Honors.vue'), meta: { requiresAdmin: true, title: '获得奖项' } },
      { path: 'products', name: 'AdminProducts', component: () => import('@/views/admin/Products.vue'), meta: { requiresAdmin: true, title: '产品列表' } },
      { path: 'cases', name: 'AdminCases', component: () => import('@/views/admin/Cases.vue'), meta: { requiresAdmin: true, title: '成功案例' } },
      { path: 'consultations', name: 'AdminConsultations', component: () => import('@/views/admin/Consultations.vue'), meta: { requiresAdmin: true, title: '咨询工单' } },
      { path: 'feedbacks', name: 'AdminFeedbacks', component: () => import('@/views/admin/Feedbacks.vue'), meta: { requiresAdmin: true, title: '用户留言' } },
      { path: 'contact-settings', name: 'AdminContactSettings', component: () => import('@/views/admin/ContactSettings.vue'), meta: { requiresAdmin: true, title: '联系配置' } },
      { path: 'profile', name: 'AdminProfile', component: () => import('@/views/admin/Profile.vue'), meta: { requiresAdmin: true, title: '个人中心' } },
      { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/Users.vue'), meta: { requiresAdmin: true, title: '用户管理' } },
      { path: 'configs', name: 'AdminConfigs', component: () => import('@/views/admin/Configs.vue'), meta: { requiresAdmin: true, title: '系统配置' } },
      { path: 'logs', name: 'AdminLogs', component: () => import('@/views/admin/Logs.vue'), meta: { requiresAdmin: true, title: '操作日志' } },
      { path: 'visits', name: 'AdminVisits', component: () => import('@/views/admin/Visits.vue'), meta: { requiresAdmin: true, title: '访问记录' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/portal/home' }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

let customPageTitle = null

export function applyDocumentTitle(to = router.currentRoute.value) {
  customPageTitle = null
  const locale = useLocaleStore()
  let page = ''

  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    const key = adminTitleKeys[to.path]
    page = key ? locale.t(key) : ''
  }

  if (!page) {
    const matched = [...to.matched].reverse()
    for (const record of matched) {
      if (record.meta?.titleKey) {
        page = locale.t(record.meta.titleKey)
        break
      }
      if (record.meta?.title) {
        page = record.meta.title
        break
      }
    }
  }

  const site = locale.t('siteTitle')
  document.title = page ? `${page} - ${site}` : site
}

/** 详情页等：用业务名称覆盖浏览器标题，如「LC-8000 xxx - 立冷科技 LIQUICOOL」 */
export function setDocumentPageTitle(pageName) {
  customPageTitle = String(pageName || '').trim() || null
  const locale = useLocaleStore()
  const site = locale.t('siteTitle')
  document.title = customPageTitle ? `${customPageTitle} - ${site}` : site
}

/** 切换语言时刷新标题（保留详情页自定义名称） */
export function refreshDocumentTitle() {
  if (customPageTitle) {
    const locale = useLocaleStore()
    document.title = `${customPageTitle} - ${locale.t('siteTitle')}`
    return
  }
  applyDocumentTitle()
}

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore()
  if (auth.token && !auth.user) {
    await auth.fetchMe()
  }

  if (to.path === '/admin/login') {
    if (auth.isAdmin) return next('/admin/dashboard')
    return next()
  }

  if (to.meta.requiresAdmin || to.path.startsWith('/admin')) {
    const i18n = useLocaleStore()
    if (!auth.isLoggedIn) {
      ElMessage.warning(i18n.t('needAdminLogin'))
      return next('/admin/login')
    }
    if (!auth.isAdmin) {
      ElMessage.error(i18n.t('noAdminAccess'))
      return next('/portal/home')
    }
  }

  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    const i18n = useLocaleStore()
    ElMessage.warning(i18n.t('pleaseLogin'))
    return next('/login')
  }

  next()
})

router.afterEach((to) => {
  applyDocumentTitle(to)

  const tags = useTagsStore()
  if (to.path.startsWith('/admin')) {
    tags.addView(to)
  }
  if (to.path.startsWith('/portal')) {
    const locale = useLocaleStore()
    visitApi
      .report({
        path: to.fullPath,
        pageTitle: to.meta?.title || '',
        referer: typeof document !== 'undefined' ? document.referrer || '' : '',
        locale: locale.locale || ''
      })
      .catch(() => {})
  }
})

export default router
