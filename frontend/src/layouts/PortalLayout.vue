<template>
  <div class="portal-layout" :key="locale">
    <header class="portal-header" @mouseleave="onHeaderLeave">
      <div class="header-inner">
        <router-link to="/portal/home" class="brand" @click="closeAll">
          <img :src="siteLogo" alt="LIQUICOOL" class="brand-logo" />
        </router-link>

        <nav class="nav-menu desktop-nav">
          <div class="nav-item-wrap" @mouseenter="openModule = ''">
            <router-link
              to="/portal/home"
              class="nav-item"
              :class="{ active: isHomeActive }"
              @click="closeAll"
            >
              {{ t('navHome') }}
            </router-link>
          </div>
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

        <div v-if="headerPhone || headerEmail" class="header-contact desktop-contact">
          <a
            v-if="headerPhone"
            class="header-contact-row"
            :href="`tel:${headerPhoneTel}`"
            @click="onHeaderTelClick"
          >
            <span class="header-contact-k">{{ t('headerHotline') }}</span>
            <strong>{{ headerPhone }}</strong>
          </a>
          <a
            v-if="headerEmail"
            class="header-contact-row"
            :href="`mailto:${headerEmail}`"
            @click="onHeaderMailClick"
          >
            <span class="header-contact-k">{{ t('floatEmail') }}</span>
            <strong>{{ headerEmail }}</strong>
          </a>
        </div>

        <div class="header-actions">
          <LangSwitcher variant="portal" />
          <el-dropdown v-if="auth.isLoggedIn" trigger="click" @command="onUserCommand">
            <button class="user-btn" type="button">
              <span class="user-btn-name">{{ auth.displayName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="auth.isAdmin" command="admin">{{ t('adminBack') }}</el-dropdown-item>
                <el-dropdown-item command="logout" :divided="auth.isAdmin">{{ t('logout') }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <div v-else class="auth-links">
            <router-link class="user-btn guest" to="/login">{{ t('login') }}</router-link>
            <router-link class="user-btn guest" to="/register">{{ t('registerTitle') }}</router-link>
          </div>
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
      <div class="mobile-block">
        <router-link class="mobile-l1 mobile-home" to="/portal/home" @click="closeAll">
          {{ t('navHome') }}
        </router-link>
      </div>
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
      <div class="mobile-auth">
        <template v-if="auth.isLoggedIn">
          <span>{{ auth.displayName }}</span>
          <button type="button" @click="handleLogout">{{ t('logout') }}</button>
        </template>
        <template v-else>
          <router-link to="/login" @click="closeAll">{{ t('login') }}</router-link>
          <router-link to="/register" @click="closeAll">{{ t('registerTitle') }}</router-link>
        </template>
      </div>
      <div v-if="headerPhone || headerEmail" class="mobile-contact">
        <a v-if="headerPhone" :href="`tel:${headerPhoneTel}`">{{ t('headerHotline') }} {{ headerPhone }}</a>
        <a v-if="headerEmail" :href="`mailto:${headerEmail}`">{{ headerEmail }}</a>
      </div>
    </nav>

    <main class="portal-main">
      <router-view />
    </main>

    <PortalFloatDock :settings="contactSettings" />

    <footer class="portal-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <img :src="siteLogo" alt="LIQUICOOL" class="footer-logo" />
          <p>{{ t('footerSlogan') }}</p>
          <div v-if="socialAccounts.length" class="footer-social">
            <h3 class="footer-social-title">{{ t('followUs') }}</h3>
            <div class="footer-social-grid">
              <div v-for="(item, idx) in socialAccounts" :key="idx" class="footer-social-item">
                <el-image
                  :src="resolveMediaUrl(item.qrUrl)"
                  :alt="socialName(item)"
                  class="footer-qr"
                  :preview-src-list="socialQrPreviewList"
                  :initial-index="idx"
                  preview-teleported
                  fit="contain"
                />
                <span>{{ socialName(item) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="footer-col">
          <h3 class="footer-col-title">{{ t('footerNav') }}</h3>
          <div class="footer-links">
            <router-link to="/portal/products">{{ t('navProducts') }}</router-link>
            <router-link to="/portal/solutions">{{ t('navSolutions') }}</router-link>
            <router-link to="/portal/news">{{ t('navNews') }}</router-link>
            <router-link to="/portal/about">{{ t('navAbout') }}</router-link>
            <router-link to="/portal/contact">{{ t('navContact') }}</router-link>
          </div>
        </div>

        <div class="footer-col">
          <h3 class="footer-col-title">{{ t('contactMethods') }}</h3>
          <div class="footer-contact">
            <p v-if="headerPhone">
              <span>{{ t('headerHotline') }}</span>
              <a :href="`tel:${headerPhoneTel}`">{{ headerPhone }}</a>
            </p>
            <p v-if="headerEmail">
              <span>{{ t('floatEmail') }}</span>
              <a :href="`mailto:${headerEmail}`">{{ headerEmail }}</a>
            </p>
            <p v-for="(addr, idx) in footerAddresses" :key="idx">
              <span>{{ t('footerAddress') }}</span>
              <em>{{ addr }}</em>
            </p>
            <router-link class="footer-contact-btn" to="/portal/contact">{{ t('navContact') }}</router-link>
          </div>
        </div>

        <p class="copyright">{{ t('copyright') }}</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { useI18n } from '@/composables/useI18n'
import { useAuthStore } from '@/stores/auth'
import { navMenuApi, contactSettingsApi } from '@/api'
import LangSwitcher from '@/components/LangSwitcher.vue'
import PortalFloatDock from '@/components/PortalFloatDock.vue'
import { ElMessage } from 'element-plus'
import { resolveMediaUrl } from '@/utils/media'
import { localizedText } from '@/utils/localized'

const { t, locale } = useI18n()
const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const topMenus = ref([])
const openModule = ref('')
const mobileOpen = ref(false)
const mobileExpand = ref('')
const contactSettings = ref({})
const socialAccounts = computed(() => {
  const list = Array.isArray(contactSettings.value.socialAccounts)
    ? contactSettings.value.socialAccounts
    : []
  return list.filter((s) => s && String(s.qrUrl || '').trim())
})
const socialQrPreviewList = computed(() =>
  socialAccounts.value.map((s) => resolveMediaUrl(s.qrUrl)).filter(Boolean)
)
let closeTimer = null

function socialName(item) {
  return localizedText(item, 'name', locale.value) || item?.name || ''
}

async function loadContactSettings() {
  try {
    const res = await contactSettingsApi.portal()
    contactSettings.value = res.data || res || {}
  } catch {
    contactSettings.value = {}
  }
}

const headerPhone = computed(() => {
  const s = contactSettings.value || {}
  return String(s.companyPhone || s.presalesPhone || '').trim()
})
const headerPhoneTel = computed(() => headerPhone.value.replace(/\s/g, ''))
const headerEmail = computed(() => String(contactSettings.value?.email || '').trim())
const siteLogo = computed(() => {
  const url = resolveMediaUrl(contactSettings.value?.logoUrl)
  return url || '/logo.svg'
})
const footerAddresses = computed(() => {
  const list = Array.isArray(contactSettings.value?.addresses)
    ? contactSettings.value.addresses
    : []
  const mapped = list
    .map((item) => localizedText(item, 'text', locale.value))
    .filter(Boolean)
  if (mapped.length) return mapped
  const legacy = localizedText(contactSettings.value || {}, 'address', locale.value)
  return legacy ? [legacy] : []
})

function onHeaderTelClick() {
  const phone = headerPhoneTel.value
  if (!phone || /Mobi|Android|iPhone|iPad/i.test(navigator.userAgent)) return
  navigator.clipboard?.writeText(phone).then(
    () => ElMessage.success(t('telCopied', { phone })),
    () => ElMessage.info(t('telHint', { phone }))
  )
}

function onHeaderMailClick() {
  const email = headerEmail.value
  if (!email) return
  navigator.clipboard?.writeText(email).then(
    () => ElMessage.success(t('mailtoCopied', { email })),
    () => ElMessage.info(t('mailtoHint', { email }))
  )
}

function handleLogout() {
  auth.logout()
  ElMessage.success(t('logout'))
  closeAll()
  if (route.path.startsWith('/admin')) {
    router.push('/portal/home')
  }
}

function onUserCommand(cmd) {
  if (cmd === 'logout') {
    handleLogout()
    return
  }
  if (cmd === 'admin') {
    closeAll()
    router.push('/admin/dashboard')
  }
}

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

const isHomeActive = computed(() => {
  const path = route.path
  return path === '/portal/home' || path === '/portal' || path === '/'
})

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
    },
    {
      id: 5,
      moduleCode: 'CONTACT',
      levelNo: 1,
      nameZh: '联系我们',
      nameTw: '聯絡我們',
      nameEn: 'Contact',
      linkPath: '/portal/contact',
      children: []
    }
  ]
}

onMounted(() => {
  loadNav()
  loadContactSettings()
})
watch(() => route.fullPath, closeAll)
</script>

<style scoped>
.portal-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.portal-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(16, 24, 32, 0.06);
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8);
}
.header-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 88px;
  display: flex;
  align-items: center;
  gap: 18px;
  position: relative;
}
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
  text-decoration: none;
}
.brand-logo {
  display: block;
  height: 58px;
  width: auto;
  max-width: 260px;
  object-fit: contain;
}
.desktop-nav {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 2px;
  min-width: 0;
}
.nav-item {
  display: inline-block;
  padding: 30px 12px;
  color: #1a1f26;
  font-size: 14px;
  font-weight: 500;
  border-bottom: 2px solid transparent;
  text-decoration: none;
  white-space: nowrap;
  transition: color 0.15s, border-color 0.15s;
}
.nav-item:hover,
.nav-item.active,
.nav-item.open {
  color: #0a4fb8;
  border-bottom-color: #0a4fb8;
}
.header-contact {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
  flex-shrink: 0;
  padding: 0 4px 0 10px;
  border-left: 1px solid #e8ebef;
  margin-left: 4px;
}
.header-contact-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  text-decoration: none;
  line-height: 1.25;
  white-space: nowrap;
}
.header-contact-k {
  font-size: 11px;
  color: #8a929b;
  min-width: 2em;
}
.header-contact-row strong {
  font-size: 13px;
  font-weight: 700;
  color: #0a4fb8;
  font-family: 'IBM Plex Sans', 'Segoe UI', sans-serif;
  letter-spacing: 0.02em;
}
.header-contact-row:hover strong {
  color: #083a8a;
}
.header-actions {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.user-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  height: 34px;
  padding: 0 12px;
  border: 1px solid #e5e8ec;
  border-radius: 999px;
  background: #fff;
  color: #1a1f26;
  font-size: 13px;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  max-width: 160px;
}
.user-btn:hover {
  border-color: #0a4fb8;
  color: #0a4fb8;
}
.user-btn-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.auth-links {
  display: flex;
  align-items: center;
  gap: 8px;
}
.user-btn.guest {
  border-color: transparent;
  background: #f2f5f9;
  text-decoration: none;
  color: #0a4fb8;
}
.menu-toggle {
  display: none;
  width: 40px;
  height: 40px;
  border: 1px solid #e5e8ec;
  border-radius: 8px;
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
  border-radius: 1px;
}
.mega-panel {
  position: absolute;
  left: 0;
  right: 0;
  top: 100%;
  background: #fff;
  border-bottom: 1px solid #e8ebef;
  box-shadow: 0 16px 40px rgba(16, 24, 32, 0.1);
}
.mega-inner {
  max-width: 1200px;
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
  color: #555;
  line-height: 1.9;
  text-decoration: none;
}
.mega-l3:hover {
  color: #0a4fb8;
}
.portal-main {
  flex: 1;
  background: #fff;
}
.portal-footer {
  background: linear-gradient(180deg, #0d1520 0%, #101820 100%);
  color: rgba(255, 255, 255, 0.72);
  padding: 48px 24px 28px;
}
.footer-inner {
  max-width: 1280px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1.4fr 0.9fr 1.1fr;
  gap: 28px 40px;
  align-items: start;
}
.footer-brand .footer-logo {
  display: block;
  height: 32px;
  width: auto;
  max-width: 180px;
  margin-bottom: 12px;
  filter: brightness(0) invert(1);
}
.footer-brand p {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
}
.footer-col-title,
.footer-social-title {
  margin: 0 0 14px;
  font-size: 14px;
  font-weight: 650;
  color: rgba(255, 255, 255, 0.92);
  letter-spacing: 0.04em;
}
.footer-links {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}
.footer-links a {
  color: rgba(255, 255, 255, 0.72);
  font-size: 14px;
  line-height: 1.4;
  text-decoration: none;
}
.footer-links a:hover {
  color: #fff;
}
.footer-contact {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.footer-contact p {
  margin: 0;
  display: grid;
  grid-template-columns: 2.5em 1fr;
  gap: 8px;
  align-items: start;
  font-size: 14px;
  line-height: 1.5;
  color: rgba(255, 255, 255, 0.72);
}
.footer-contact span {
  color: rgba(255, 255, 255, 0.45);
  font-size: 12px;
  padding-top: 2px;
}
.footer-contact a,
.footer-contact em {
  color: rgba(255, 255, 255, 0.88);
  font-style: normal;
  text-decoration: none;
  word-break: break-all;
}
.footer-contact a:hover {
  color: #fff;
}
.footer-contact-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-top: 6px;
  width: fit-content;
  min-width: 108px;
  height: 34px;
  padding: 0 14px;
  border: 1px solid rgba(255, 255, 255, 0.28);
  border-radius: 6px;
  color: #fff;
  font-size: 13px;
  text-decoration: none;
}
.footer-contact-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.5);
}
.footer-social {
  margin-top: 20px;
}
.footer-social-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16px 20px;
}
.footer-social-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  min-width: 88px;
}
.footer-qr {
  width: 88px;
  height: 88px;
  object-fit: contain;
  background: #fff;
  border-radius: 6px;
  padding: 4px;
  cursor: zoom-in;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  transform-origin: center bottom;
  will-change: transform;
}
.footer-social-item:hover .footer-qr {
  transform: scale(2);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.35);
  z-index: 2;
  position: relative;
}
.footer-qr :deep(img) {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
.footer-social-item span {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.72);
  text-align: center;
}
.copyright {
  grid-column: 1 / -1;
  margin: 4px 0 0;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  font-size: 12px;
  opacity: 0.65;
}
.nav-backdrop {
  display: none;
}
.mobile-nav {
  display: none;
}
.mobile-auth {
  display: flex;
  gap: 16px;
  padding: 14px 16px 18px;
  border-top: 1px solid #eef1f4;
  font-size: 14px;
}
.mobile-auth a,
.mobile-auth button {
  color: #0a4fb8;
  background: none;
  border: none;
  font: inherit;
  cursor: pointer;
  text-decoration: none;
}
.mobile-contact {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px 16px 18px;
  border-top: 1px solid #eef1f4;
  font-size: 13px;
}
.mobile-contact a {
  color: #0a4fb8;
  text-decoration: none;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .desktop-contact {
    display: none;
  }
}

@media (max-width: 980px) {
  .brand-logo {
    height: 48px;
    max-width: 200px;
  }
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
    inset: 88px 0 0;
    background: rgba(16, 24, 32, 0.35);
    z-index: 90;
  }
  .mobile-nav {
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    top: 88px;
    background: #fff;
    border-bottom: 1px solid #e8ebef;
    z-index: 120;
    max-height: calc(100vh - 88px);
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
  a.mobile-home {
    color: inherit;
    text-decoration: none;
    box-sizing: border-box;
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
    flex-direction: row;
    flex-wrap: wrap;
    gap: 10px 18px;
  }
}
</style>
