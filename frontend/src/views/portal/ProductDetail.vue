<template>
  <div class="portal-section page-card">
    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else-if="detail">
      <img :src="resolveMediaUrl(detail.coverUrl) || defaultCover('product')" class="hero-img" alt="" />
      <h1 class="page-title">{{ detail.name }}</h1>
      <el-tag>{{ detail.category }}</el-tag>
      <p class="summary">{{ detail.summary }}</p>
      <div class="content rich-content" v-html="detail.content || detail.description"></div>
    </template>
    <el-empty v-else description="方案不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { productApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'

const route = useRoute()
const detail = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await productApi.portalDetail(route.params.id)
    detail.value = res.data || res
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
  border-radius: 8px;
  margin-bottom: 20px;
}
.summary {
  color: #666;
  line-height: 1.8;
  margin: 16px 0;
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
  .hero-img {
    max-height: 240px;
  }
}
</style>
