<template>
  <el-config-provider :locale="epLocale">
    <router-view />
  </el-config-provider>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import zhTw from 'element-plus/dist/locale/zh-tw.mjs'
import en from 'element-plus/dist/locale/en.mjs'
import { useLocaleStore } from '@/stores/locale'

const localeStore = useLocaleStore()
const { locale } = storeToRefs(localeStore)

const epLocale = computed(() => {
  if (locale.value === 'en') return en
  if (locale.value === 'zh-TW') return zhTw
  return zhCn
})

onMounted(() => {
  localeStore.setLocale(localeStore.locale)
})
</script>
<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
/* 清除全局滚动条样式 */
* {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 和 Edge */
}

/* Chrome, Safari 和 Opera */
*::-webkit-scrollbar {
  display: none;
}

/* 确保页面主体也应用滚动条隐藏 */
html, body {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 和 Edge */
  margin: 0;
  padding: 0;
}

html::-webkit-scrollbar,
body::-webkit-scrollbar {
  display: none;
}
</style>
