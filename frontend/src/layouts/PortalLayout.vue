<template>
  <div class="portal-layout" :key="locale">
    <div class="utility-bar">
      <div class="utility-inner">
        <LangSwitcher variant="portal" />
        <router-link to="/portal/contact" class="util-link" @click="closeAll">{{ t('navContact') }}</router-link>
        <el-dropdown v-if="auth.isLoggedIn" trigger="click" @command="onUserCommand">
          <button class="util-user" type="button">
            <el-icon><User /></el-icon>
            <span>{{ auth.displayName }}</span>
            <el-icon><ArrowDown /></el-icon>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">{{ t('profile') }}</el-dropdown-item>
              <el-dropdown-item v-if="auth.isAdmin" command="admin">{{ t('enterAdmin') }}</el-dropdown-item>
              <el-dropdown-item divided command="logout">{{ t('logout') }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <router-link v-else to="/login" class="util-link util-login">
          <el-icon><User /></el-icon>
          <span>{{ t('login') }}</span>
        </router-link>
      </div>
    </div>

    <header class="portal-header" @mouseleave="onHeaderLeave">
      <div class="header-inner">
        <router-link to="/portal/home" class="brand" @click="closeAll">
          <div class="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 40 40" width="36" height="36">
              <rect x="2" y="2" width="36" height="36" fill="#101820" />
              <path d="M10 26V14h6.2c3.4 0 5.4 1.7 5.4 4.4 0 2.8-2 4.5-5.4 4.5H14.6V26H10zm4.6-6.4h1.4c1.4 0 2.2-.7 2.2-1.7s-.8-1.6-2.2-1.6h-1.4v3.3z" fill="#fff" />
              <rect x="24" y="14" width="6" height="12" fill="#0a4fb8" />
            </svg>
          </div>
          <div class="brand-text">
            <div class="brand-en">LIQUICOOL</div>
            <div class="brand-cn">{{ t('brandCn') }}</div>
          </div>
        </router-link>

        <nav class="nav-menu desktop-nav">
          <div
            v-for="item in topMenus"
            :key="item.id"
            class="nav-item-wrap"
            @mouseenter="openMega(item)"
          >
            <a
              class="nav-item"
              :class="{ active: isActive(item), open: openModule === item.moduleCode }"
              href="javascript:;"
              @click.prevent="goTop(item)"
            >
              {{ displayName(item) }}
            </a>
          </div>
        </nav>

        <div class="header-actions">
          <button
            class="menu-toggle"
            type="button"
            :aria-expanded="mobileOpen"
            aria-label="menu"
            @click="mobileOpen = !mobileOpen"
          >
            <span></span><span></span><span></span>
          </button>
        </div>
      </div>

      <div
        v-show="openModule && activeMegaColumns.length"
        class="mega-panel"
        @mouseenter="keepMega"
      >
        <div class="mega-inner">
          <div
            v-for="col in activeMegaColumns"
            :key="col.id"
            class="mega-col"
          >
            <router-link
              v-if="col.linkPath"
              :to="normalizePath(col.linkPath)"
              class="mega-l2"
              @click="closeAll"
            >
              {{ displayName(col) }}
            </router-link>
            <div v-else class="mega-l2">{{ displayName(col) }}</div>
            <router-link
              v-for="child in col.children || []"
              :key="child.id"
              :to="normalizePath(child.linkPath || col.linkPath || '/portal/home')"
              class="mega-l3"
              @click="closeAll"
            >
              {{ displayName(child) }}
            </router-link>
          </div>
        </div>
      </div>
    </header>

    <div v-if="mobileOpen" class="nav-backdrop" @click="closeAll"></div>
    <nav v-if="mobileOpen" class="mobile-nav">
      <div v-for="item in topMenus" :key="'m-' + item.id" class="mobile-block">
        <button class="mobile-l1" type="button" @click="toggleMobile(item)">
          <span>{{ displayName(item) }}</span>
          <span v-if="(item.children || []).length">{{ mobileExpand === item.moduleCode ? '−' : '+' }}</span>
        </button>
        <div v-if="mobileExpand === item.moduleCode" class="mobile-subs">
          <template v-for="col in item.children || []" :key="'c-' + col.id">
            <div class="mobile-l2">{{ displayName(col) }}</div>
            <router-link
              v-for="child in (col.children && col.children.length ? col.children : [col])"
              :key="'l3-' + child.id"
              :to="normalizePath(child.linkPath || col.linkPath || item.linkPath)"
              class="mobile-l3"
              @click="closeAll"
            >
              {{ displayName(child) }}
            </router-link>
          </template>
          <router-link
            v-if="!(item.children || []).length"
            :to="normalizePath(item.linkPath)"
            class="mobile-l3"
            @click="closeAll"
          >
            {{ displayName(item) }}
          </router-link>
        </div>
      </div>
    </nav>

    <main class="portal-main">
      <router-view />
    </main>

    <footer class="portal-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="brand-en">LIQUICOOL</div>
          <p>{{ t('footerSlogan') }}</p>
        </div>
        <div class="footer-links">
          <router-link to="/portal/products">{{ t('navProducts') }}</router-link>
          <router-link to="/portal/solutions">{{ t('navSolutions') }}</router-link>
          <router-link to="/portal/news">{{ t('navNews') }}</router-link>
          <router-link to="/portal/contact">{{ t('navContact') }}</router-link>
        </div>
        <p class="copyright">{{ t('copyright') }}</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowDown, User } from '@element-plus/icons-vue'
import { useI18n } from '@/composables/useI18n'
import { useAuthStore } from '@/stores/auth'
import { navMenuApi } from '@/api'
import LangSwitcher from '@/components/LangSwitcher.vue'

const { t, locale } = useI18n()
const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const topMenus = ref([])
const openModule = ref('')
const mobileOpen = ref(false)
const mobileExpand = ref('')
let closeTimer = null

const activeMegaColumns = computed(() => {
  const item = topMenus.value.find((m) => m.moduleCode === openModule.value)
  return item?.children || []
})

function displayName(node) {
  if (!node) return ''
  if (locale.value === 'zh-TW') return node.nameTw || node.nameZh
  if (locale.value === 'en') return node.nameEn || node.nameZh
  return node.nameZh
}

function normalizePath(path) {
  if (!path) return '/portal/home'
  if (path.startsWith('http')) return path
  return path
}

function isActive(item) {
  const path = item.linkPath || ''
  const base = path.split('?')[0]
  if (!base) return false
  return route.path === base || route.path.startsWith(base + '/')
}

function openMega(item) {
  clearTimeout(closeTimer)
  if ((item.children || []).length) {
    openModule.value = item.moduleCode
  } else {
    openModule.value = ''
  }
}

function keepMega() {
  clearTimeout(closeTimer)
}

function onHeaderLeave() {
  clearTimeout(closeTimer)
  closeTimer = setTimeout(() => {
    openModule.value = ''
  }, 180)
}

function goTop(item) {
  closeAll()
  router.push(normalizePath(item.linkPath))
}

function closeAll() {
  openModule.value = ''
  mobileOpen.value = false
  mobileExpand.value = ''
}

function toggleMobile(item) {
  if (!(item.children || []).length) {
    goTop(item)
    return
  }
  mobileExpand.value = mobileExpand.value === item.moduleCode ? '' : item.moduleCode
}

function onUserCommand(cmd) {
  if (cmd === 'profile') router.push('/portal/profile')
  else if (cmd === 'admin') router.push('/admin/dashboard')
  else if (cmd === 'logout') {
    auth.logout()
    router.push('/portal/home')
  }
}

async function loadNav() {
  try {
    const res = await navMenuApi.portalTree()
    const tree = res.data || res || []
    topMenus.value = Array.isArray(tree) ? tree.filter((n) => n.levelNo === 1 || !n.parentId) : []
  } catch {
    topMenus.value = fallbackMenus()
  }
  if (!topMenus.value.length) topMenus.value = fallbackMenus()
}

function fallbackMenus() {
  return [
    {
      id: 1,
      moduleCode: 'PRODUCTS',
      levelNo: 1,
      nameZh: '产品与服务中心',
      nameTw: '產品與服務中心',
      nameEn: 'Products & Services',
      linkPath: '/portal/products',
      children: [
        {
          id: 10,
          nameZh: '液冷产品',
          nameTw: '液冷產品',
          nameEn: 'Products',
          linkPath: '/portal/products',
          children: [
            { id: 101, nameZh: '液冷服务器', nameTw: '液冷伺服器', nameEn: 'Servers', linkPath: '/portal/products?category=液冷服务器' },
            { id: 102, nameZh: 'CDU冷量分配单元', nameTw: 'CDU', nameEn: 'CDU', linkPath: '/portal/products?category=CDU冷量分配单元' },
            { id: 103, nameZh: '冷却液', nameTw: '冷卻液', nameEn: 'Coolant', linkPath: '/portal/products?category=冷却液' }
          ]
        },
        {
          id: 11,
          nameZh: '热管理技术',
          nameTw: '熱管理技術',
          nameEn: 'Thermal',
          linkPath: '/portal/technologies',
          children: [
            { id: 111, nameZh: '浸没式液冷', nameTw: '浸沒式液冷', nameEn: 'Immersion', linkPath: '/portal/technologies' },
            { id: 112, nameZh: '冷板式液冷', nameTw: '冷板式液冷', nameEn: 'Cold Plate', linkPath: '/portal/technologies' }
          ]
        }
      ]
    },
    {
      id: 2,
      moduleCode: 'SOLUTIONS',
      levelNo: 1,
      nameZh: '解决方案',
      nameTw: '解決方案',
      nameEn: 'Solutions',
      linkPath: '/portal/solutions',
      children: [
        {
          id: 21,
          nameZh: '按行业',
          nameTw: '按行業',
          nameEn: 'By Industry',
          linkPath: '/portal/solutions',
          children: [
            { id: 212, nameZh: '金融', nameTw: '金融', nameEn: 'Finance', linkPath: '/portal/cases?industry=金融' },
            { id: 213, nameZh: '运营商', nameTw: '運營商', nameEn: 'Telecom', linkPath: '/portal/cases?industry=运营商' }
          ]
        }
      ]
    },
    { id: 3, moduleCode: 'NEWS', levelNo: 1, nameZh: '新闻', nameTw: '新聞', nameEn: 'News', linkPath: '/portal/news', children: [] },
    {
      id: 4,
      moduleCode: 'ABOUT',
      levelNo: 1,
      nameZh: '关于',
      nameTw: '關於',
      nameEn: 'About',
      linkPath: '/portal/about',
      children: [
        { id: 40, nameZh: '公司简介', nameTw: '公司簡介', nameEn: 'Company', linkPath: '/portal/about', children: [] },
        { id: 41, nameZh: '加入我们', nameTw: '加入我們', nameEn: 'Join Us', linkPath: '/portal/join', children: [] },
        { id: 42, nameZh: '获得奖项', nameTw: '獲得獎項', nameEn: 'Awards', linkPath: '/portal/honors', children: [] }
      ]
    }
  ]
}

onMounted(loadNav)
watch(() => route.fullPath, closeAll)
</script>

<style scoped>
.portal-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.utility-bar {
  background: #fff;
  border-bottom: 1px solid #e8ebef;
}
.utility-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 0 24px;
  height: 34px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 20px;
}
.util-link,
.util-user {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #333;
  font-size: 12px;
  background: none;
  border: none;
  cursor: pointer;
  font-family: inherit;
  padding: 0;
  text-decoration: none;
}
.util-link:hover,
.util-user:hover {
  color: #0a4fb8;
}
.portal-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  border-bottom: 1px solid #d9dde3;
}
.header-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  gap: 28px;
  position: relative;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.brand-en {
  color: #101820;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.08em;
}
.brand-cn {
  color: #5c6570;
  font-size: 11px;
  margin-top: 3px;
}
.desktop-nav {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 4px;
}
.nav-item {
  display: inline-block;
  padding: 24px 14px 22px;
  color: #1a1f26;
  font-size: 14px;
  font-weight: 500;
  border-bottom: 3px solid transparent;
  text-decoration: none;
  white-space: nowrap;
}
.nav-item:hover,
.nav-item.active,
.nav-item.open {
  color: #101820;
  border-bottom-color: #101820;
}
.header-actions {
  margin-left: auto;
  display: flex;
  align-items: center;
}
.menu-toggle {
  display: none;
  width: 40px;
  height: 40px;
  border: 1px solid #d5dae0;
  background: #fff;
  padding: 10px 9px;
  cursor: pointer;
  flex-direction: column;
  justify-content: space-between;
}
.menu-toggle span {
  display: block;
  height: 2px;
  background: #101820;
}
.mega-panel {
  position: absolute;
  left: 0;
  right: 0;
  top: 100%;
  background: #fff;
  border-bottom: 1px solid #d9dde3;
  box-shadow: 0 12px 24px rgba(16, 24, 32, 0.08);
}
.mega-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 28px 24px 32px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 24px 32px;
}
.mega-l2 {
  display: block;
  font-size: 15px;
  font-weight: 700;
  color: #101820;
  margin-bottom: 12px;
  text-decoration: none;
}
.mega-l2:hover {
  color: #0a4fb8;
}
.mega-l3 {
  display: block;
  font-size: 13px;
  color: #333;
  line-height: 1.9;
  text-decoration: none;
}
.mega-l3:hover {
  color: #0a4fb8;
  text-decoration: underline;
}
.portal-main {
  flex: 1;
}
.portal-footer {
  background: #101820;
  color: rgba(255, 255, 255, 0.72);
  padding: 40px 24px 28px;
  border-top: 3px solid #0a4fb8;
}
.footer-inner {
  max-width: 1180px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 20px 40px;
}
.footer-brand .brand-en {
  color: #fff;
  margin-bottom: 8px;
}
.footer-brand p {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
}
.footer-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 24px;
  justify-content: flex-end;
  align-content: flex-start;
}
.footer-links a {
  color: rgba(255, 255, 255, 0.78);
  font-size: 14px;
}
.copyright {
  grid-column: 1 / -1;
  margin: 8px 0 0;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  font-size: 12px;
  opacity: 0.65;
}
.nav-backdrop {
  display: none;
}
.mobile-nav {
  display: none;
}

@media (max-width: 900px) {
  .desktop-nav {
    display: none;
  }
  .menu-toggle {
    display: inline-flex;
  }
  .mega-panel {
    display: none !important;
  }
  .nav-backdrop {
    display: block;
    position: fixed;
    inset: 108px 0 0;
    background: rgba(16, 24, 32, 0.35);
    z-index: 90;
  }
  .mobile-nav {
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    top: 108px;
    background: #fff;
    border-bottom: 1px solid #d9dde3;
    z-index: 120;
    max-height: calc(100vh - 108px);
    overflow: auto;
  }
  .mobile-l1 {
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: 14px 16px;
    border: none;
    border-bottom: 1px solid #eef1f4;
    background: #fff;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
    font-family: inherit;
  }
  .mobile-l2 {
    padding: 8px 16px 4px;
    font-size: 13px;
    font-weight: 700;
    color: #101820;
  }
  .mobile-l3 {
    display: block;
    padding: 8px 24px;
    font-size: 13px;
    color: #444;
    text-decoration: none;
  }
  .footer-inner {
    grid-template-columns: 1fr;
  }
  .footer-links {
    justify-content: flex-start;
  }
}
@media (max-width: 480px) {
  .brand-cn {
    display: none;
  }
  .utility-inner {
    gap: 12px;
    padding: 0 14px;
  }
}
</style>
