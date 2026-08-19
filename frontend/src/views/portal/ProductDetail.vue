<template>
  <div class="portal-section page-card">
    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else-if="detail">
      <img :src="resolveMediaUrl(detail.coverUrl) || defaultCover('product')" class="hero-img" alt="" />
      <h1 class="page-title">{{ detail.name }}</h1>
      <el-tag>{{ detail.category }}</el-tag>
      <p class="summary">{{ detail.summary }}</p>
      <div class="content" v-html="detail.content || detail.description"></div>
      <div class="actions">
        <el-button type="primary" @click="handleConsult">立即咨询</el-button>
        <el-button @click="handleFavorite">收藏方案</el-button>
      </div>
    </template>
    <el-empty v-else description="方案不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { productApi, consultationApi, favoriteApi } from '@/api'
import { useAuthStore } from '@/stores/auth'
import { resolveMediaUrl, defaultCover } from '@/utils/media'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
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

function requireLogin() {
  if (!auth.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return false
  }
  return true
}

async function handleConsult() {
  if (!requireLogin()) return
  try {
    await consultationApi.create({
      productId: detail.value.id,
      title: `咨询方案：${detail.value.name}`,
      content: '希望了解该方案的详细报价与部署周期'
    })
    ElMessage.success('咨询已提交')
    router.push('/portal/profile')
  } catch { /* handled */ }
}

async function handleFavorite() {
  if (!requireLogin()) return
  try {
    await favoriteApi.add({ targetType: 'PRODUCT', targetId: detail.value.id })
    ElMessage.success('收藏成功')
  } catch { /* handled */ }
}
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
}
.actions {
  margin-top: 32px;
  display: flex;
  gap: 12px;
}
</style>
