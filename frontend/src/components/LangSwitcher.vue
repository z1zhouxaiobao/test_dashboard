<template>
  <el-dropdown trigger="click" @command="onLangChange">
    <button class="lang-btn" :class="variant" type="button">
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
  font-family: inherit;
}
.portal {
  height: 34px;
  padding: 0 12px;
  border: 1px solid #101820;
  border-radius: 0;
  background: #fff;
  color: #101820;
  font-weight: 500;
}
.portal:hover {
  border-color: #0a4fb8;
  color: #0a4fb8;
}
@media (max-width: 480px) {
  .portal {
    padding: 0 8px;
    font-size: 12px;
  }
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
