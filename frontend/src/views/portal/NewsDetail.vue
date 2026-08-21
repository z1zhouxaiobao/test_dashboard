<template>
  <div class="portal-section page-card">
    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else-if="detail">
      <h1 class="page-title">{{ localizedText(detail, 'title', locale) }}</h1>
      <div class="meta">
        <span>{{ formatDateTime(detail.publishTime || detail.createdAt) }}</span>
        <span>阅读 {{ detail.viewCount || 0 }}</span>
      </div>
      <img v-if="detail.coverUrl" :src="resolveMediaUrl(detail.coverUrl)" class="cover" alt="" />
      <div class="content rich-content" v-html="localizedText(detail, 'content', locale)"></div>
    </template>
    <el-empty v-else description="新闻不存在" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { newsApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'
import { setDocumentPageTitle } from '@/router'

const { locale } = useI18n()
const route = useRoute()
const detail = ref(null)
const loading = ref(true)

function syncTitle() {
  if (!detail.value) return
  const name = localizedText(detail.value, 'title', locale.value)
  if (name) setDocumentPageTitle(name)
}

watch(locale, syncTitle)

onMounted(async () => {
  try {
    const res = await newsApi.portalDetail(route.params.id)
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
.meta {
  color: #999;
  font-size: 13px;
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.cover {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 20px;
}
.content {
  line-height: 1.8;
  color: #333;
  overflow-wrap: anywhere;
}
.content :deep(img) {
  max-width: 100%;
  height: auto;
}
@media (max-width: 768px) {
  .cover {
    max-height: 220px;
  }
}
</style>
