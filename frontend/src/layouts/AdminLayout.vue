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

          <el-sub-menu index="content">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>{{ t('adminContent') }}</span>
            </template>
            <el-menu-item index="/admin/carousels">
              <el-icon><Picture /></el-icon>
              <template #title>{{ t('adminCarousels') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/news">
              <el-icon><Reading /></el-icon>
              <template #title>{{ t('adminNews') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/notices">
              <el-icon><Bell /></el-icon>
              <template #title>{{ t('adminNotices') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/technologies">
              <el-icon><Cpu /></el-icon>
              <template #title>{{ t('adminTech') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/honors">
              <el-icon><Trophy /></el-icon>
              <template #title>{{ t('adminHonors') }}</template>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="business">
            <template #title>
              <el-icon><Briefcase /></el-icon>
              <span>{{ t('adminBusiness') }}</span>
            </template>
            <el-menu-item index="/admin/products">
              <el-icon><Box /></el-icon>
              <template #title>{{ t('adminProducts') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/cases">
              <el-icon><Collection /></el-icon>
              <template #title>{{ t('adminCases') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/consultations">
              <el-icon><ChatDotRound /></el-icon>
              <template #title>{{ t('adminConsultations') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/feedbacks">
              <el-icon><Comment /></el-icon>
              <template #title>{{ t('adminFeedbacks') }}</template>
            </el-menu-item>
            <el-menu-item index="/admin/favorites">
              <el-icon><Star /></el-icon>
              <template #title>{{ t('adminFavorites') }}</template>
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
  DataAnalysis, Document, Picture, Reading, Bell, Cpu, Trophy,
  Briefcase, Box, Collection, ChatDotRound, Comment, Star,
  Setting, User, Tools, List, Fold, Expand, SwitchButton
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
  return key ? t(key) : (route.meta.title || '')
})
const pageKey = computed(() => `${route.fullPath}#${refreshTick.value}`)

function tagLabel(tag) {
  const key = adminTitleKeys[tag.path]
  return key ? t(key) : tag.title
}

watch(
  () => route.path,
  () => tags.addView(route),
  { immediate: true }
)

function closeTag(path) {
  tags.removeView(path)
  if (route.path === path) {
    const last = tags.visited[tags.visited.length - 1]
    router.push(last?.path || '/admin/dashboard')
  }
}

function ensureCurrentVisible(tagPath) {
  if (!tags.visited.some((v) => v.path === route.path)) {
    router.push(tagPath || '/admin/dashboard')
  }
}

function onTagCommand(cmd, tag) {
  if (cmd === 'refresh') {
    if (route.path !== tag.path) {
      router.push(tag.path).then(() => {
        refreshTick.value += 1
      })
    } else {
      refreshTick.value += 1
    }
    return
  }
  if (cmd === 'closeLeft') {
    tags.removeLeft(tag.path)
    ensureCurrentVisible(tag.path)
  }
  if (cmd === 'closeRight') {
    tags.removeRight(tag.path)
    ensureCurrentVisible(tag.path)
  }
  if (cmd === 'closeAll') {
    tags.closeAll()
    router.push('/admin/dashboard')
  }
}

function handleLogout() {
  auth.logout()
  router.push('/admin/login')
}

function handleCommand(cmd) {
  if (cmd === 'portal') router.push('/portal/profile')
  if (cmd === 'logout') handleLogout()
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}
.admin-aside {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  background: #0a1628;
  transition: width 0.2s;
  z-index: 200;
  overflow: hidden;
}
.aside-logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 700;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.logo-text {
  color: #0B5ED7;
  letter-spacing: 1px;
}
.logo-mini {
  color: #0B5ED7;
  font-size: 20px;
}
.aside-scroll {
  flex: 1;
  min-height: 0;
}
.aside-logout {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 52px;
  width: 100%;
  border: none;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  background: transparent;
  color: #bfcbd9;
  cursor: pointer;
  font-size: 14px;
  font-family: inherit;
}
.aside-logout:hover {
  color: #f56c6c;
  background: rgba(245, 108, 108, 0.12);
}
.aside-logout .el-icon {
  font-size: 18px;
}
.admin-main-wrap {
  transition: margin-left 0.2s;
  min-height: 100vh;
}
.admin-header {
  position: fixed;
  top: 0;
  right: 0;
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  z-index: 100;
  transition: left 0.2s;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
.tags-bar {
  position: fixed;
  top: 56px;
  right: 0;
  height: 40px;
  background: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  padding: 0 12px;
  gap: 8px;
  overflow-x: auto;
  z-index: 99;
  transition: left 0.2s;
}
.tag-item {
  cursor: pointer;
}
.tags-bar :deep(.el-dropdown) {
  display: inline-flex;
  align-items: center;
}
.admin-content {
  margin-top: 96px;
  padding: 16px;
  background: var(--liquicool-bg);
  min-height: calc(100vh - 96px);
}
</style>
