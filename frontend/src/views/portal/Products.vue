<template>
  <div class="products-page">
    <section class="page-hero">
      <div class="hero-inner">
        <div class="crumb">{{ t('navProducts') }}</div>
        <h1>{{ t('productsTitle') }}</h1>
        <p>{{ t('productsIntro') }}</p>
      </div>
    </section>

    <div class="page-body">
      <aside class="filter-panel">
        <h3>{{ t('refine') }}</h3>
        <div v-for="group in filterGroups" :key="group.id" class="filter-group">
          <button class="filter-title" type="button" @click="toggleGroup(group.id)">
            <span>{{ displayName(group) }}</span>
            <span>{{ expanded[group.id] === false ? '+' : '−' }}</span>
          </button>
          <div v-show="expanded[group.id] !== false" class="filter-list">
            <label
              v-for="item in group.children || []"
              :key="item.id"
              class="filter-item"
            >
              <input
                type="checkbox"
                :checked="selectedCategory === (item.code || item.nameZh)"
                @change="selectCategory(item)"
              />
              <span>{{ displayName(item) }}</span>
            </label>
          </div>
        </div>
        <button v-if="selectedCategory" class="clear-btn" type="button" @click="clearCategory">
          {{ t('clearFilter') }}
        </button>
      </aside>

      <section class="list-panel">
        <div class="list-toolbar">
          <span v-if="loading">{{ t('loadingList') }}</span>
          <span v-else>{{ t('showingResults', { n: total }) }}</span>
        </div>

        <div v-if="loading" class="skeleton-list" aria-busy="true" aria-live="polite">
          <div v-for="n in 5" :key="n" class="product-row skeleton-row">
            <el-skeleton animated>
              <template #template>
                <div class="skeleton-grid">
                  <el-skeleton-item variant="image" class="skeleton-cover" />
                  <div class="skeleton-info">
                    <el-skeleton-item variant="h3" style="width: 42%; margin-bottom: 12px" />
                    <el-skeleton-item variant="text" style="width: 92%" />
                    <el-skeleton-item variant="text" style="width: 78%" />
                    <el-skeleton-item variant="text" style="width: 64%" />
                  </div>
                  <div class="skeleton-actions">
                    <el-skeleton-item variant="text" style="width: 70%" />
                    <el-skeleton-item variant="text" style="width: 70%" />
                    <el-skeleton-item variant="text" style="width: 70%" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </div>

        <template v-else>
          <article v-for="item in list" :key="item.id" class="product-row">
            <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('product')" class="cover" alt="" />
            <div class="info">
              <h3 @click="$router.push(`/portal/products/${item.id}`)">{{ localizedText(item, 'name', locale) }}</h3>
              <p>{{ localizedText(item, 'summary', locale) }}</p>
            </div>
            <div class="actions">
              <a href="javascript:;" @click.prevent="$router.push(`/portal/products/${item.id}`)">{{ t('viewSeries') }}</a>
              <a href="javascript:;" @click.prevent="$router.push('/portal/contact')">{{ t('contactSales') }}</a>
              <a href="javascript:;" @click.prevent="$router.push('/portal/contact')">{{ t('getSupport') }}</a>
            </div>
          </article>
          <el-empty v-if="!list.length" :description="t('noData')" />
        </template>

        <div v-show="!loading" class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            v-model:page-size="size"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadData"
          />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi, navMenuApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const route = useRoute()
const router = useRouter()

const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const loading = ref(true)
const filterGroups = ref([])
const expanded = reactive({})
const selectedCategory = ref(route.query.category || '')

const displayName = (node) => {
  if (!node) return ''
  if (locale.value === 'zh-TW') return node.nameTw || node.nameZh
  if (locale.value === 'en') return node.nameEn || node.nameZh
  return node.nameZh
}

async function loadFilters() {
  try {
    const res = await navMenuApi.portalTree('PRODUCTS')
    const tree = res.data || res || []
    const root = Array.isArray(tree) ? tree.find((n) => n.moduleCode === 'PRODUCTS') || tree[0] : null
    filterGroups.value = root?.children || []
  } catch {
    filterGroups.value = []
  }
}

async function loadData() {
  loading.value = true
  try {
    const res = await productApi.portalPage({
      page: page.value,
      size: size.value,
      category: selectedCategory.value || undefined
    })
    list.value = res.data?.records || res.records || res.data || []
    total.value = res.data?.total || res.total || 0
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

function toggleGroup(id) {
  expanded[id] = expanded[id] === false ? true : false
}

function selectCategory(item) {
  const code = item.code || item.nameZh
  selectedCategory.value = selectedCategory.value === code ? '' : code
  page.value = 1
  router.replace({
    path: '/portal/products',
    query: selectedCategory.value ? { category: selectedCategory.value } : {}
  })
}

function clearCategory() {
  selectedCategory.value = ''
  page.value = 1
  router.replace({ path: '/portal/products' })
}

watch(
  () => route.query.category,
  (val) => {
    selectedCategory.value = val || ''
    page.value = 1
    loadData()
  }
)

onMounted(async () => {
  await loadFilters()
  await loadData()
})
</script>

<style scoped>
.page-hero {
  background: #101820;
  color: #fff;
  border-bottom: 3px solid #0a4fb8;
}
.hero-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 36px 24px 40px;
}
.crumb {
  font-size: 12px;
  letter-spacing: 0.12em;
  color: #8eb6ff;
  margin-bottom: 10px;
}
.hero-inner h1 {
  margin: 0 0 10px;
  font-size: 28px;
}
.hero-inner p {
  margin: 0;
  max-width: 760px;
  color: rgba(255, 255, 255, 0.78);
  line-height: 1.7;
  font-size: 14px;
}
.page-body {
  max-width: 1180px;
  margin: 0 auto;
  padding: 28px 24px 48px;
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 28px;
}
.filter-panel {
  border: 1px solid #d5dae0;
  background: #fff;
  padding: 16px;
  align-self: start;
}
.filter-panel h3 {
  margin: 0 0 12px;
  font-size: 16px;
}
.filter-group + .filter-group {
  border-top: 1px solid #e8ebef;
  margin-top: 8px;
  padding-top: 8px;
}
.filter-title {
  width: 100%;
  display: flex;
  justify-content: space-between;
  border: none;
  background: none;
  padding: 8px 0;
  font-weight: 700;
  font-size: 13px;
  cursor: pointer;
  font-family: inherit;
}
.filter-item {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  font-size: 13px;
  color: #333;
  margin: 6px 0;
  cursor: pointer;
}
.clear-btn {
  margin-top: 12px;
  width: 100%;
  border: 1px solid #101820;
  background: #fff;
  padding: 8px;
  cursor: pointer;
  font-family: inherit;
}
.list-toolbar {
  margin-bottom: 12px;
  font-size: 13px;
  color: #5c6570;
}
.product-row {
  display: grid;
  grid-template-columns: 160px 1fr 160px;
  gap: 20px;
  padding: 20px 0;
  border-top: 1px solid #d5dae0;
}
.product-row:last-of-type {
  border-bottom: 1px solid #d5dae0;
}
.cover {
  width: 100%;
  height: 110px;
  object-fit: cover;
  border: 1px solid #d5dae0;
  background: #fff;
}
.info h3 {
  margin: 0 0 8px;
  color: #0a4fb8;
  font-size: 18px;
  cursor: pointer;
}
.info p {
  margin: 0;
  color: #333;
  font-size: 14px;
  line-height: 1.7;
}
.actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 13px;
}
.actions a {
  color: #0a4fb8;
  text-decoration: none;
}
.actions a:hover {
  text-decoration: underline;
}
.skeleton-list {
  border-top: 1px solid #d5dae0;
}
.skeleton-row {
  border-top: none;
  border-bottom: 1px solid #d5dae0;
}
.skeleton-grid {
  display: grid;
  grid-template-columns: 160px 1fr 160px;
  gap: 20px;
  width: 100%;
}
.skeleton-cover {
  width: 100%;
  height: 110px;
}
.skeleton-info {
  padding-top: 4px;
}
.skeleton-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 8px;
}
@media (max-width: 900px) {
  .page-body {
    grid-template-columns: 1fr;
  }
  .product-row {
    grid-template-columns: 1fr;
  }
  .skeleton-grid {
    grid-template-columns: 1fr;
  }
}
</style>
