<template>
  <div class="portal-section">
    <h1 class="page-title">核心技术</h1>
    <el-row :gutter="24">
      <el-col v-for="item in list" :key="item.id" :xs="24" :md="12" :lg="8">
        <el-card class="tech-card" shadow="hover">
          <img :src="resolveMediaUrl(item.coverUrl || item.imageUrl) || defaultCover('product')" class="tech-img" alt="" />
          <h3>{{ item.title || item.name }}</h3>
          <p>{{ item.summary || item.description }}</p>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="!loading && !list.length" description="暂无数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { technologyApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const list = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await technologyApi.portalList()
    list.value = res.data?.records || res.data || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.tech-card {
  margin-bottom: 24px;
}
.tech-img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
}
.tech-card h3 {
  color: #0B5ED7;
  margin: 12px 0 8px;
}
.tech-card p {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}
</style>
