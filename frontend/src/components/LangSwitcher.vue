<template>
  <el-dropdown trigger="click" @command="onLangChange">
    <button class="lang-btn" :class="variant" type="button">
      <span class="lang-icon">🌐</span>
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
  gap: 6px;
  cursor: pointer;
  font-size: 13px;
}
.lang-icon {
  font-size: 14px;
  line-height: 1;
}
.portal {
  height: 34px;
  padding: 0 14px;
  border: none;
  border-radius: 999px;
  background: #0B5ED7;
  color: #fff;
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
  color: #0B5ED7;
  outline: none;
  box-shadow: none;
}
.admin .lang-icon {
  font-size: 14px;
  filter: grayscale(1);
  opacity: 0.85;
}
.admin:hover .lang-icon,
.admin:focus .lang-icon {
  filter: none;
  opacity: 1;
}
.login {
  height: 34px;
  padding: 0 14px;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
  backdrop-filter: blur(8px);
}
</style>
