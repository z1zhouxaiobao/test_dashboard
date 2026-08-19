<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('productsTitle') }}</h1>
    <el-row :gutter="20">
      <el-col v-for="item in list" :key="item.id" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="product-item" @click="$router.push(`/portal/products/${item.id}`)">
          <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('product')" class="cover" alt="" />
          <div class="info">
            <el-tag size="small">{{ item.category || '液冷方案' }}</el-tag>
            <h3>{{ item.name }}</h3>
            <p>{{ item.summary }}</p>
          </div>
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
import { productApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const list = ref([])
const page = ref(1)
const size = ref(9)
const total = ref(0)

async function loadData() {
  try {
    const res = await productApi.portalPage({ page: page.value, size: size.value })
    list.value = res.data?.records || res.records || res.data || []
    total.value = res.data?.total || res.total || 0
  } catch {
    list.value = []
  }
}

onMounted(loadData)
</script>

<style scoped>
.product-item {
  cursor: pointer;
  margin-bottom: 20px;
}
.cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}
.info h3 {
  margin: 12px 0 8px;
  color: #0a1628;
}
.info p {
  color: #666;
  font-size: 14px;
}
</style>
