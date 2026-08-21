<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('honorTitle') }}</h1>
    <div class="honor-grid">
      <component
        :is="item.linkUrl ? 'a' : 'article'"
        v-for="item in list"
        :key="item.id"
        class="honor-item"
        v-bind="item.linkUrl ? { href: normalizeHref(item.linkUrl), target: '_blank', rel: 'noopener noreferrer' } : {}"
      >
        <img :src="resolveMediaUrl(item.coverUrl || item.imageUrl) || defaultCover('honor')" class="honor-img" alt="" />
        <h3>{{ localizedText(item, 'title', locale) }}</h3>
        <p>{{ localizedText(item, 'summary', locale) }}</p>
        <span class="year">{{ item.awardYear || item.year }}</span>
      </component>
    </div>
    <el-empty v-if="!loading && !list.length" :description="t('noData')" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { honorApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const list = ref([])
const loading = ref(false)

function normalizeHref(url) {
  const raw = String(url || '').trim()
  if (!raw) return '#'
  if (/^(https?:|mailto:|tel:)/i.test(raw) || raw.startsWith('/')) return raw
  return `https://${raw}`
}

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
.honor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0;
  border-top: 1px solid #d5dae0;
  border-left: 1px solid #d5dae0;
}
.honor-item {
  text-align: left;
  background: #fff;
  border-right: 1px solid #d5dae0;
  border-bottom: 1px solid #d5dae0;
  padding: 18px 16px 20px;
  text-decoration: none;
  color: inherit;
  display: block;
  transition: background 0.15s ease;
}
a.honor-item:hover {
  background: #f7f9fc;
}
a.honor-item:hover h3 {
  color: #0a4fb8;
}
.honor-img {
  width: 100%;
  height: 120px;
  object-fit: contain;
  display: block;
  margin-bottom: 12px;
  background: #f7f8fa;
  border: 1px solid #e8ebef;
}
.honor-item h3 {
  color: #101820;
  margin: 0 0 8px;
  font-size: 15px;
}
.honor-item p {
  color: #5c6570;
  font-size: 13px;
  margin: 0 0 10px;
  line-height: 1.5;
}
.year {
  color: #0a4fb8;
  font-weight: 600;
  font-size: 13px;
  font-family: 'IBM Plex Sans', sans-serif;
}
@media (max-width: 900px) {
  .honor-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 480px) {
  .honor-grid {
    grid-template-columns: 1fr;
  }
}
</style>
