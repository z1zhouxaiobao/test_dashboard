<template>
  <div class="portal-section page-card">
    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else-if="detail">
      <img :src="resolveMediaUrl(detail.coverUrl) || defaultCover('product')" class="hero-img" alt="" />
      <h1 class="page-title">{{ localizedText(detail, 'name', locale) }}</h1>
      <el-tag>{{ detail.category }}</el-tag>
      <p class="summary">{{ localizedText(detail, 'summary', locale) }}</p>
      <div class="content rich-content" v-html="localizedText(detail, 'content', locale) || detail.description"></div>
    </template>
    <el-empty v-else description="方案不存在" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { productApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'
import { setDocumentPageTitle } from '@/router'

const { locale } = useI18n()
const route = useRoute()
const detail = ref(null)
const loading = ref(true)

function syncTitle() {
  if (!detail.value) return
  const name = localizedText(detail.value, 'name', locale.value)
  if (name) setDocumentPageTitle(name)
}

watch(locale, syncTitle)

onMounted(async () => {
  try {
    const res = await productApi.portalDetail(route.params.id)
    detail.value = res.data || res
    syncTitle()
  } catch {
    detail.value = null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.hero-img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 0;
  border: 1px solid #d5dae0;
  margin-bottom: 20px;
}
.summary {
  color: #5c6570;
  line-height: 1.8;
  margin: 16px 0;
}
.content {
  line-height: 1.8;
  color: #1a1f26;
  overflow-wrap: anywhere;
}
.content :deep(img) {
  max-width: 100%;
  height: auto;
}
@media (max-width: 768px) {
  .hero-img {
    max-height: 240px;
  }
}
</style>
