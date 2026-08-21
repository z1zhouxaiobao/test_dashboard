<template>
  <el-container class="admin-layout">
    <el-aside :width="collapsed ? '64px' : '220px'" class="admin-aside">
      <div class="aside-logo" @click="$router.push('/admin/dashboard')">
        <img
          :src="siteLogo"
          alt="LIQUICOOL"
          class="aside-logo-img"
          :class="{ 'is-mini': collapsed }"
        />
      </div>
      <el-scrollbar class="aside-scroll">
        <el-menu
          :default-active="activeMenu"
          :collapse="collapsed"
          :collapse-transition="false"
          unique-opened
          background-color="#0a1628"
          text-color="#bfcbd9"
          active-text-color="#0B5ED7"
          router
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <template #title>{{ t('adminDashboard') }}</template>
          </el-menu-item>

          <el-menu-item index="/admin/nav-menus">
            <el-icon><Menu /></el-icon>
            <template #title>{{ t('adminNavMenus') }}</template>
          </el-menu-item>

          <el-menu-item index="/admin/carousels">
            <el-icon><Picture /></el-icon>
            <template #title>{{ t('adminCarousels') }}</template>
          </el-menu-item>

          <el-sub-menu index="portal-products">
            <template #title>
              <el-icon><Box /></el-icon>
              <span>{{ t('adminGroupProducts') }}</span>
            </template>
            <el-menu-item index="/admin/products">
              <template #title>{{ t('adminProducts') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/technologies">
              <template #title>{{ t('adminTech') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="portal-solutions">
            <template #title>
              <el-icon><Briefcase /></el-icon>
              <span>{{ t('adminGroupSolutions') }}</span>
            </template>
            <el-menu-item index="/admin/cases">
              <template #title>{{ t('adminCases') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="portal-news">
            <template #title>
              <el-icon><Reading /></el-icon>
              <span>{{ t('adminGroupNews') }}</span>
            </template>
            <el-menu-item index="/admin/news">
              <template #title>{{ t('adminNews') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/notices">
              <template #title>{{ t('adminNotices') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="portal-about">
            <template #title>
              <el-icon><Trophy /></el-icon>
              <span>{{ t('adminGroupAbout') }}</span>
            </template>
            <el-menu-item index="/admin/honors">
              <template #title>{{ t('adminHonors') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/jobs">
              <template #title>{{ t('adminJobs') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="portal-contact">
            <template #title>
              <el-icon><ChatDotRound /></el-icon>
              <span>{{ t('adminGroupContact') }}</span>
            </template>
            <el-menu-item index="/admin/contact-settings">
              <template #title>{{ t('adminContactSettings') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/consultations">
              <template #title>{{ t('adminConsultations') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/feedbacks">
              <template #title>{{ t('adminFeedbacks') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>{{ t('adminSystem') }}</span>
            </template>
            <el-menu-item index="/admin/profile">
              <el-icon><UserFilled /></el-icon>
              <template #title>{{ t('adminProfile') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <template #title>{{ t('adminUsers') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/configs">
              <el-icon><Tools /></el-icon>
              <template #title>{{ t('adminConfigs') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/visits">
              <el-icon><View /></el-icon>
              <template #title>{{ t('adminVisits') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/logs">
              <el-icon><List /></el-icon>
              <template #title>{{ t('adminLogs') }}</template>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
      <button class="aside-logout" type="button" :title="t('logout')" @click="handleLogout">
        <el-icon><SwitchButton /></el-icon>
        <span v-if="!collapsed">{{ t('logout') }}</span>
      </button>
    </el-aside>

    <el-container class="admin-main-wrap" :style="{ marginLeft: asideWidth }">
      <el-header class="admin-header" :style="{ left: asideWidth }">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="collapsed = !collapsed">
            <Fold v-if="!collapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">{{ t('adminBack') }}</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <HeaderClock />
          <LangSwitcher variant="admin" />
          <el-button type="primary" link @click="$router.push('/portal/home')">{{ t('backToPortal') }}</el-button>
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="resolveMediaUrl(auth.avatar)" />
              <span>{{ auth.displayName }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">{{ t('adminProfile') }}</el-dropdown-item>
                <el-dropdown-item divided command="logout">{{ t('logout') }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <div class="tags-bar" :style="{ left: asideWidth }">
        <el-dropdown
          v-for="tag in tags.visited"
          :key="tag.path"
          trigger="contextmenu"
          @command="(cmd) => onTagCommand(cmd, tag)"
        >
          <button
            type="button"
            class="nav-tag"
            :class="{ 'is-active': route.path === tag.path }"
            @click="$router.push(tag.path)"
          >
            <span class="nav-tag-dot" aria-hidden="true" />
            <span class="nav-tag-label">{{ tagLabel(tag) }}</span>
            <span
              v-if="tag.path !== '/admin/dashboard'"
              class="nav-tag-close"
              role="button"
              :title="t('tagClose')"
              @click.stop="closeTag(tag.path)"
            >×</span>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="refresh">{{ t('tagRefresh') }}</el-dropdown-item>
              <el-dropdown-item command="closeLeft">{{ t('tagCloseLeft') }}</el-dropdown-item>
              <el-dropdown-item command="closeRight">{{ t('tagCloseRight') }}</el-dropdown-item>
              <el-dropdown-item command="closeAll" divided>{{ t('tagCloseAll') }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <el-main class="admin-content">
        <router-view :key="pageKey" />
      </el-main>
      <el-backtop :right="40" :bottom="48" :visibility-height="240" :title="t('backToTop')" />
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  DataAnalysis, Picture, Reading, Trophy,
  Briefcase, Box, ChatDotRound,
  Setting, User, UserFilled, Tools, List, Fold, Expand, SwitchButton, Menu, View
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { useTagsStore } from '@/stores/tags'
import { resolveMediaUrl } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'
import { useSiteLogo } from '@/composables/useSiteLogo'
import { adminTitleKeys } from '@/i18n/admin'
import LangSwitcher from '@/components/LangSwitcher.vue'
import HeaderClock from '@/components/HeaderClock.vue'

const { t } = useI18n()
const { siteLogo, loadSiteLogo } = useSiteLogo()
onMounted(loadSiteLogo)
const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const tags = useTagsStore()

const collapsed = ref(false)
const refreshTick = ref(0)
const asideWidth = computed(() => (collapsed.value ? '64px' : '220px'))
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => {
  const key = adminTitleKeys[route.path]
  return key ? t(key) : route.meta?.title || ''
})
const pageKey = computed(() => `${route.fullPath}-${refreshTick.value}`)

function tagLabel(tag) {
  const key = adminTitleKeys[tag.path]
  return key ? t(key) : tag.title || tag.path
}

function closeTag(path) {
  tags.removeView(path)
  if (route.path === path) {
    const last = tags.visited[tags.visited.length - 1]
    router.push(last?.path || '/admin/dashboard')
  }
}

function onTagCommand(cmd, tag) {
  if (cmd === 'refresh') {
    if (route.path === tag.path) refreshTick.value += 1
    else router.push(tag.path).then(() => { refreshTick.value += 1 })
    return
  }
  if (cmd === 'closeLeft') tags.removeLeft(tag.path)
  if (cmd === 'closeRight') tags.removeRight(tag.path)
  if (cmd === 'closeAll') {
    tags.closeAll()
    router.push('/admin/dashboard')
  }
}

function handleCommand(cmd) {
  if (cmd === 'profile') router.push('/admin/profile')
  if (cmd === 'logout') handleLogout()
}

function handleLogout() {
  auth.logout()
  router.push('/admin/login')
}

watch(
  () => route.path,
  () => {
    if (route.path.startsWith('/admin')) tags.addView(route)
  },
  { immediate: true }
)
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: #f0f2f5;
}
.admin-aside {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 100;
  background: #0a1628;
  display: flex;
  flex-direction: column;
  transition: width 0.2s;
}
.aside-logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  flex-shrink: 0;
  padding: 0 12px;
  overflow: hidden;
}
.aside-logo-img {
  display: block;
  height: 28px;
  width: auto;
  max-width: 168px;
  filter: brightness(0) invert(1);
}
.aside-logo-img.is-mini {
  height: 22px;
  width: 36px;
  max-width: none;
  object-fit: cover;
  object-position: left center;
}
.aside-scroll {
  flex: 1;
  overflow: hidden;
}
.aside-logout {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 48px;
  border: none;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  background: transparent;
  color: #bfcbd9;
  cursor: pointer;
  flex-shrink: 0;
}
.aside-logout:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.06);
}
.admin-main-wrap {
  min-height: 100vh;
  transition: margin-left 0.2s;
}
.admin-header {
  position: fixed;
  top: 0;
  right: 0;
  height: 56px;
  z-index: 90;
  background: #fff;
  border-bottom: 1px solid #e8ebef;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  transition: left 0.2s;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.collapse-btn {
  cursor: pointer;
  font-size: 18px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-info {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
.tags-bar {
  position: fixed;
  top: 56px;
  right: 0;
  z-index: 80;
  height: 44px;
  background: linear-gradient(180deg, #f7f9fc 0%, #eef2f7 100%);
  border-bottom: 1px solid #e2e8f0;
  padding: 0 14px;
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: 8px;
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  transition: left 0.2s;
  scrollbar-width: thin;
  scrollbar-color: #c5ccd4 transparent;
}
.tags-bar::-webkit-scrollbar {
  height: 4px;
}
.tags-bar::-webkit-scrollbar-thumb {
  background: #c5ccd4;
  border-radius: 4px;
}
.tags-bar :deep(.el-dropdown) {
  flex-shrink: 0;
}
.nav-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  max-width: 180px;
  height: 30px;
  padding: 0 10px;
  border: 1px solid transparent;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 1px 2px rgba(16, 24, 32, 0.04);
  color: #4a5560;
  font-size: 13px;
  line-height: 1;
  cursor: pointer;
  flex-shrink: 0;
  transition: background 0.15s, color 0.15s, border-color 0.15s, box-shadow 0.15s;
}
.nav-tag:hover {
  color: var(--liquicool-primary);
  border-color: #c9daf0;
  box-shadow: 0 2px 6px rgba(10, 79, 184, 0.08);
}
.nav-tag.is-active {
  background: linear-gradient(135deg, #0a4fb8 0%, #1565d8 100%);
  border-color: transparent;
  color: #fff;
  box-shadow: 0 4px 10px rgba(10, 79, 184, 0.28);
}
.nav-tag-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #c5ccd4;
  flex-shrink: 0;
}
.nav-tag.is-active .nav-tag-dot {
  background: #fff;
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.28);
}
.nav-tag-label {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.nav-tag-close {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  margin-left: 2px;
  margin-right: -2px;
  border-radius: 4px;
  color: inherit;
  opacity: 0.55;
  font-size: 14px;
  line-height: 1;
  flex-shrink: 0;
  transition: opacity 0.15s, background 0.15s;
}
.nav-tag-close:hover {
  opacity: 1;
  background: rgba(0, 0, 0, 0.08);
}
.nav-tag.is-active .nav-tag-close:hover {
  background: rgba(255, 255, 255, 0.22);
}
.admin-content {
  margin-top: 104px;
  padding: 16px;
  min-height: calc(100vh - 104px);
}
</style>
