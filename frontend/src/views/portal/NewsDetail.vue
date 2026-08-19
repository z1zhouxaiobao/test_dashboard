<template>
  <div class="portal-section page-card">
    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else-if="detail">
      <h1 class="page-title">{{ detail.title }}</h1>
      <div class="meta">
        <span>{{ formatDateTime(detail.publishTime || detail.createdAt) }}</span>
        <span>阅读 {{ detail.viewCount || 0 }}</span>
      </div>
      <img v-if="detail.coverUrl" :src="resolveMediaUrl(detail.coverUrl)" class="cover" alt="" />
      <div class="content" v-html="detail.content"></div>
    </template>
    <el-empty v-else description="新闻不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { newsApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl } from '@/utils/media'

const route = useRoute()
const detail = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await newsApi.portalDetail(route.params.id)
    detail.value = res.data || res
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
}
</style>
