<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('newsTitle') }}</h1>
    <div v-for="item in list" :key="item.id" class="news-item" @click="openNews(item)">
      <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('news')" class="thumb" alt="" />
      <div class="news-body">
        <h3>{{ localizedText(item, 'title', locale) }}</h3>
        <p>{{ localizedText(item, 'summary', locale) }}</p>
        <span class="date">{{ formatDateTime(item.publishTime || item.createdAt) }}</span>
      </div>
    </div>
    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { newsApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

function normalizeHref(url) {
  const raw = String(url || '').trim()
  if (!raw) return ''
  if (/^(https?:|mailto:|tel:)/i.test(raw) || raw.startsWith('/')) return raw
  return `https://${raw}`
}

function openNews(item) {
  const href = normalizeHref(item?.linkUrl)
  if (!href) {
    ElMessage.warning(t('newsNoLink'))
    return
  }
  window.open(href, '_blank', 'noopener,noreferrer')
}

async function loadData() {
  try {
    const res = await newsApi.portalPage({ page: page.value, size: size.value })
    list.value = res.data?.records || res.records || res.data || []
    total.value = res.data?.total || res.total || 0
  } catch {
    list.value = []
  }
}

onMounted(loadData)
</script>

<style scoped>
.news-item {
  display: flex;
  gap: 20px;
  padding: 20px 0;
  background: transparent;
  border-radius: 0;
  margin-bottom: 0;
  border-bottom: 1px solid #d5dae0;
  cursor: pointer;
}
.news-item:first-child {
  border-top: 2px solid #101820;
}
.news-item:hover {
  box-shadow: none;
}
.news-item:hover h3 {
  color: #0a4fb8;
}
.thumb {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 0;
  flex-shrink: 0;
  border: 1px solid #d5dae0;
  background: #fff;
}
.news-body h3 {
  margin: 0 0 8px;
  color: #101820;
  transition: color 0.15s;
}
.news-body p {
  color: #5c6570;
  font-size: 14px;
  margin: 0 0 8px;
}
.date {
  color: #8a929b;
  font-size: 13px;
  font-family: 'IBM Plex Sans', sans-serif;
}
@media (max-width: 768px) {
  .news-item {
    flex-direction: column;
    padding: 16px 0;
    gap: 12px;
  }
  .thumb {
    width: 100%;
    height: 160px;
  }
}
</style>
