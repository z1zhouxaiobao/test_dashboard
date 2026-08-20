<template>
  <el-container class="admin-layout">
    <el-aside :width="collapsed ? '64px' : '220px'" class="admin-aside">
      <div class="aside-logo" @click="$router.push('/admin/dashboard')">
        <span v-if="!collapsed" class="logo-text">LIQUICOOL</span>
        <span v-else class="logo-mini">L</span>
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
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <template #title>{{ t('adminUsers') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/configs">
              <el-icon><Tools /></el-icon>
              <template #title>{{ t('adminConfigs') }}</template>
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
                <el-dropdown-item command="portal">{{ t('profile') }}</el-dropdown-item>
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
          <el-tag
            :closable="tag.path !== '/admin/dashboard'"
            :effect="route.path === tag.path ? 'dark' : 'plain'"
            class="tag-item"
            @click="$router.push(tag.path)"
            @close.stop="closeTag(tag.path)"
          >
            {{ tagLabel(tag) }}
          </el-tag>
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
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  DataAnalysis, Picture, Reading, Trophy,
  Briefcase, Box, ChatDotRound,
  Setting, User, Tools, List, Fold, Expand, SwitchButton, Menu
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { useTagsStore } from '@/stores/tags'
import { resolveMediaUrl } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'
import { adminTitleKeys } from '@/i18n/admin'
import LangSwitcher from '@/components/LangSwitcher.vue'
import HeaderClock from '@/components/HeaderClock.vue'

const { t } = useI18n()
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
  if (cmd === 'portal') router.push('/portal/profile')
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
}
.logo-text {
  color: #fff;
  font-weight: 800;
  letter-spacing: 1px;
}
.logo-mini {
  color: #0B5ED7;
  font-weight: 800;
  font-size: 20px;
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
  background: #fff;
  border-bottom: 1px solid #eef1f4;
  padding: 8px 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  transition: left 0.2s;
}
.tag-item {
  cursor: pointer;
}
.admin-content {
  margin-top: 104px;
  padding: 16px;
  min-height: calc(100vh - 104px);
}
</style>
