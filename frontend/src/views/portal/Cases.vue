<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('casesTitle') }}</h1>
    <div class="case-list">
      <article
        v-for="item in list"
        :key="item.id"
        class="case-row"
        @click="$router.push(`/portal/cases/${item.id}`)"
      >
        <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('case')" class="cover" alt="" />
        <div class="body">
          <span v-if="item.industry" class="tag">{{ localizedText(item, 'industry', locale) }}</span>
          <h3>{{ localizedText(item, 'title', locale) }}</h3>
          <p>{{ localizedText(item, 'summary', locale) }}</p>
        </div>
      </article>
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
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { caseApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const route = useRoute()
const list = ref([])
const page = ref(1)
const size = ref(9)
const total = ref(0)

async function loadData() {
  try {
    const res = await caseApi.portalPage({
      page: page.value,
      size: size.value,
      industry: route.query.industry || undefined
    })
    list.value = res.data?.records || res.records || res.data || []
    total.value = res.data?.total || res.total || 0
  } catch {
    list.value = []
  }
}

watch(
  () => route.query.industry,
  () => {
    page.value = 1
    loadData()
  }
)

onMounted(loadData)
</script>

<style scoped>
.case-list {
  border-top: 2px solid #101820;
}
.case-row {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 24px;
  padding: 22px 0;
  border-bottom: 1px solid #d5dae0;
  cursor: pointer;
}
.case-row:hover h3 {
  color: #0a4fb8;
}
.cover {
  width: 100%;
  height: 150px;
  object-fit: cover;
  display: block;
  border: 1px solid #d5dae0;
  background: #fff;
}
.body .tag {
  display: inline-block;
  font-size: 11px;
  letter-spacing: 0.06em;
  color: #0a4fb8;
  border: 1px solid #0a4fb8;
  padding: 2px 8px;
  margin-bottom: 8px;
}
.body h3 {
  margin: 0 0 8px;
  color: #101820;
  font-size: 18px;
  transition: color 0.15s;
}
.body p {
  color: #5c6570;
  font-size: 14px;
  margin: 0;
  line-height: 1.65;
}
@media (max-width: 768px) {
  .case-row {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  .cover {
    height: 160px;
  }
}
</style>
