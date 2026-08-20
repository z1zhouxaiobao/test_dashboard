<template>
  <el-dropdown trigger="click" @command="onLangChange">
    <button class="lang-btn" :class="variant" type="button">
      <svg v-if="variant === 'portal'" class="globe" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true">
        <circle cx="12" cy="12" r="9" fill="none" stroke="currentColor" stroke-width="1.6" />
        <path d="M3 12h18M12 3c2.5 2.8 3.8 5.8 3.8 9s-1.3 6.2-3.8 9c-2.5-2.8-3.8-5.8-3.8-9S9.5 5.8 12 3z" fill="none" stroke="currentColor" stroke-width="1.6" />
      </svg>
      <span>{{ localeStore.langButtonText }}</span>
      <el-icon><ArrowDown /></el-icon>
    </button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="zh-CN" :disabled="locale === 'zh-CN'">
          {{ t('langZhCN') }}
        </el-dropdown-item>
        <el-dropdown-item command="zh-TW" :disabled="locale === 'zh-TW'">
          {{ t('langZhTW') }}
        </el-dropdown-item>
        <el-dropdown-item command="en" :disabled="locale === 'en'">
          {{ t('langEn') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useI18n } from '@/composables/useI18n'

defineProps({
  variant: { type: String, default: 'portal' }
})

const { t, locale, setLocale, localeStore } = useI18n()

function onLangChange(lang) {
  setLocale(lang)
  ElMessage.success(t('switchOk'))
}
</script>

<style scoped>
.lang-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  font-size: 12px;
  font-family: inherit;
}
.globe {
  flex-shrink: 0;
  opacity: 0.8;
}
.portal {
  height: auto;
  padding: 0;
  border: none;
  border-radius: 0;
  background: transparent;
  color: #333;
  font-weight: 400;
}
.portal:hover {
  color: #0a4fb8;
}
.admin {
  height: auto;
  padding: 0;
  border: none;
  border-radius: 0;
  background: transparent;
  color: #4e5969;
  font-family: inherit;
  box-shadow: none;
  appearance: none;
}
.admin:hover,
.admin:focus,
.admin:focus-visible {
  color: #0a4fb8;
  outline: none;
  box-shadow: none;
}
.login {
  height: 34px;
  padding: 0 12px;
  border: 1px solid rgba(255, 255, 255, 0.45);
  border-radius: 0;
  background: transparent;
  color: #fff;
}
</style>
