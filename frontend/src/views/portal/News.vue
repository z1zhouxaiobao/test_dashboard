<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('newsTitle') }}</h1>
    <div v-for="item in list" :key="item.id" class="news-item" @click="$router.push(`/portal/news/${item.id}`)">
      <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('news')" class="thumb" alt="" />
      <div class="news-body">
        <h3>{{ item.title }}</h3>
        <p>{{ item.summary }}</p>
        <span class="date">{{ formatDateTime(item.publishTime || item.createdAt) }}</span>
      </div>
    </div>
    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { newsApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

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
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.news-item:hover {
  box-shadow: 0 4px 16px rgba(11, 94, 215, 0.12);
}
.thumb {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}
.news-body h3 {
  margin: 0 0 8px;
  color: #0a1628;
}
.news-body p {
  color: #666;
  font-size: 14px;
  margin: 0 0 8px;
}
.date {
  color: #999;
  font-size: 13px;
}
@media (max-width: 768px) {
  .news-item {
    flex-direction: column;
  }
  .thumb {
    width: 100%;
  }
}
</style>
