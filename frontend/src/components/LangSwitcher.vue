<template>
  <el-dropdown trigger="click" @command="onLangChange">
    <button class="lang-btn" :class="variant" type="button">
      <span>{{ currentLangLabel }}</span>
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
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useI18n } from '@/composables/useI18n'
import { isValidLocale } from '@/utils/hreflang'

const props = defineProps({
  variant: { type: String, default: 'portal' }
})

const { t, locale, setLocale, localeStore } = useI18n()
const router = useRouter()
const route = useRoute()

const currentLangLabel = computed(() => {
  if (props.variant === 'portal') {
    if (locale.value === 'zh-TW') return t('langZhTW')
    if (locale.value === 'en') return t('langEn')
    return t('langZhCN')
  }
  return localeStore.langButtonText
})

function onLangChange(lang) {
  if (!isValidLocale(lang)) return
  setLocale(lang)
  if (route.path.startsWith('/portal')) {
    router.replace({
      path: route.path,
      query: { ...route.query, lang },
      hash: route.hash
    })
  }
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
.portal {
  height: 34px;
  padding: 0 14px;
  border: none;
  border-radius: 999px;
  background: #0a4fb8;
  color: #fff;
  font-weight: 600;
  font-size: 13px;
  box-shadow: 0 6px 16px rgba(10, 79, 184, 0.28);
}
.portal:hover {
  background: #0860d8;
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
