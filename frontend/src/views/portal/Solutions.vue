<template>
  <div class="solutions-page">
    <section class="page-hero">
      <div class="hero-inner">
        <div class="crumb">{{ t('navSolutions') }}</div>
        <h1>{{ t('solutionsTitle') }}</h1>
        <p>{{ t('solutionsIntro') }}</p>
      </div>
    </section>

    <section class="portal-section">
      <div class="columns">
        <div v-for="col in columns" :key="col.id" class="col">
          <h2>{{ displayName(col) }}</h2>
          <router-link
            v-for="item in col.children || []"
            :key="item.id"
            :to="item.linkPath || '/portal/cases'"
            class="link"
          >
            {{ displayName(item) }}
          </router-link>
          <router-link
            v-if="!(col.children || []).length"
            :to="col.linkPath || '/portal/cases'"
            class="link"
          >
            {{ displayName(col) }}
          </router-link>
        </div>
      </div>

      <div class="case-preview">
        <div class="section-head">
          <h2>{{ t('casesTitle') }}</h2>
          <router-link to="/portal/cases">{{ t('viewMore') }}</router-link>
        </div>
        <article
          v-for="item in cases"
          :key="item.id"
          class="case-row"
          @click="$router.push(`/portal/cases/${item.id}`)"
        >
          <img :src="resolveMediaUrl(item.coverUrl) || defaultCover('case')" alt="" />
          <div>
            <h3>{{ localizedText(item, 'title', locale) }}</h3>
            <p>{{ localizedText(item, 'summary', locale) }}</p>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { navMenuApi, caseApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const columns = ref([])
const cases = ref([])

function displayName(node) {
  if (!node) return ''
  if (locale.value === 'zh-TW') return node.nameTw || node.nameZh
  if (locale.value === 'en') return node.nameEn || node.nameZh
  return node.nameZh
}

onMounted(async () => {
  try {
    const res = await navMenuApi.portalTree('SOLUTIONS')
    const tree = res.data || res || []
    const root = Array.isArray(tree) ? tree.find((n) => n.moduleCode === 'SOLUTIONS') || tree[0] : null
    columns.value = root?.children || []
  } catch {
    columns.value = []
  }
  try {
    const cRes = await caseApi.portalPage({ page: 1, size: 4 })
    cases.value = cRes.data?.records || cRes.records || []
  } catch {
    cases.value = []
  }
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
.columns {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 28px;
  margin-bottom: 40px;
}
.col h2 {
  margin: 0 0 14px;
  font-size: 18px;
  border-bottom: 2px solid #101820;
  padding-bottom: 8px;
}
.link {
  display: block;
  color: #333;
  font-size: 14px;
  line-height: 2;
  text-decoration: none;
}
.link:hover {
  color: #0a4fb8;
}
.section-head {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  border-bottom: 2px solid #101820;
  margin-bottom: 8px;
  padding-bottom: 10px;
}
.section-head h2 {
  margin: 0;
  font-size: 20px;
}
.case-row {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 18px;
  padding: 18px 0;
  border-bottom: 1px solid #d5dae0;
  cursor: pointer;
}
.case-row img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border: 1px solid #d5dae0;
}
.case-row h3 {
  margin: 0 0 8px;
  color: #0a4fb8;
}
.case-row p {
  margin: 0;
  color: #5c6570;
  font-size: 14px;
  line-height: 1.6;
}
@media (max-width: 900px) {
  .columns {
    grid-template-columns: 1fr;
  }
  .case-row {
    grid-template-columns: 1fr;
  }
}
</style>
