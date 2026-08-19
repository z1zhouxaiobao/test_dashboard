<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('honorTitle') }}</h1>
    <el-row :gutter="20">
      <el-col v-for="item in list" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="honor-card">
          <img :src="resolveMediaUrl(item.coverUrl || item.imageUrl) || defaultCover('honor')" class="honor-img" alt="" />
          <h3>{{ item.title || item.name }}</h3>
          <p>{{ item.summary || item.description }}</p>
          <span class="year">{{ item.awardYear || item.year }}</span>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="!loading && !list.length" description="暂无数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { honorApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const list = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await honorApi.portalList()
    list.value = res.data?.records || res.data || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.honor-card {
  margin-bottom: 20px;
  text-align: center;
}
.honor-img {
  width: 100%;
  height: 140px;
  object-fit: contain;
}
.honor-card h3 {
  color: #0B5ED7;
  margin: 12px 0 8px;
  font-size: 15px;
}
.honor-card p {
  color: #666;
  font-size: 13px;
}
.year {
  color: #DC3545;
  font-weight: 600;
}
</style>
