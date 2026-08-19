<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('casesTitle') }}</h1>
    <el-row :gutter="20">
      <el-col v-for="item in list" :key="item.id" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="case-card" @click="$router.push(`/portal/cases/${item.id}`)">
          <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('case')" class="cover" alt="" />
          <h3>{{ item.title }}</h3>
          <p>{{ item.summary }}</p>
          <el-tag size="small">{{ item.industry }}</el-tag>
        </el-card>
      </el-col>
    </el-row>
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
import { caseApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const list = ref([])
const page = ref(1)
const size = ref(9)
const total = ref(0)

async function loadData() {
  try {
    const res = await caseApi.portalPage({ page: page.value, size: size.value })
    list.value = res.data?.records || res.records || res.data || []
    total.value = res.data?.total || res.total || 0
  } catch {
    list.value = []
  }
}

onMounted(loadData)
</script>

<style scoped>
.case-card {
  cursor: pointer;
  margin-bottom: 20px;
}
.cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 4px;
}
.case-card h3 {
  margin: 12px 0 8px;
}
.case-card p {
  color: #666;
  font-size: 14px;
}
</style>
