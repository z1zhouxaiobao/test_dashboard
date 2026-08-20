<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('techTitle') }}</h1>
    <div class="tech-list">
      <article v-for="item in list" :key="item.id" class="tech-row">
        <img :src="resolveMediaUrl(item.coverUrl || item.imageUrl) || defaultCover('product')" class="tech-img" alt="" />
        <div class="tech-body">
          <h3>{{ item.title || item.name }}</h3>
          <p>{{ item.summary || item.description }}</p>
        </div>
      </article>
    </div>
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
.tech-list {
  border-top: 2px solid #101820;
}
.tech-row {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 24px;
  align-items: center;
  padding: 22px 0;
  border-bottom: 1px solid #d5dae0;
}
.tech-img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  display: block;
  border: 1px solid #d5dae0;
  background: #fff;
}
.tech-body h3 {
  color: #101820;
  margin: 0 0 8px;
  font-size: 18px;
}
.tech-body p {
  color: #5c6570;
  font-size: 14px;
  line-height: 1.7;
  margin: 0;
}
@media (max-width: 768px) {
  .tech-row {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  .tech-img {
    height: 160px;
  }
}
</style>
