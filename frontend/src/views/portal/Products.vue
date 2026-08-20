<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('productsTitle') }}</h1>
    <div class="product-grid">
      <article
        v-for="item in list"
        :key="item.id"
        class="product-item"
        @click="$router.push(`/portal/products/${item.id}`)"
      >
        <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('product')" class="cover" alt="" />
        <div class="info">
          <span class="cat">{{ item.category || '液冷方案' }}</span>
          <h3>{{ item.name }}</h3>
          <p>{{ item.summary }}</p>
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
.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  border-top: 1px solid #d5dae0;
  border-left: 1px solid #d5dae0;
}
.product-item {
  cursor: pointer;
  background: #fff;
  border-right: 1px solid #d5dae0;
  border-bottom: 1px solid #d5dae0;
  display: flex;
  flex-direction: column;
}
.product-item:hover h3 {
  color: #0a4fb8;
}
.cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  display: block;
  border-bottom: 1px solid #d5dae0;
}
.info {
  padding: 16px 18px 20px;
}
.cat {
  display: inline-block;
  font-size: 11px;
  letter-spacing: 0.08em;
  color: #0a4fb8;
  border: 1px solid #0a4fb8;
  padding: 2px 8px;
  margin-bottom: 10px;
}
.info h3 {
  margin: 0 0 8px;
  color: #101820;
  font-size: 16px;
  transition: color 0.15s;
}
.info p {
  color: #5c6570;
  font-size: 14px;
  margin: 0;
  line-height: 1.6;
}
@media (max-width: 900px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 560px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
  .cover {
    height: 160px;
  }
}
</style>
