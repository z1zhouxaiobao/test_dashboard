<template>
  <div class="join-page">
    <section class="page-hero">
      <div class="hero-inner">
        <div class="crumb">{{ t('navAbout') }} / {{ t('joinUs') }}</div>
        <h1>{{ t('joinUs') }}</h1>
        <p>{{ t('joinIntro') }}</p>
      </div>
    </section>
    <section class="portal-section">
      <div v-loading="loading" class="jobs">
        <article
          v-for="job in jobs"
          :key="job.id"
          class="job-card"
          :class="{ open: expandedId === job.id }"
        >
          <button class="job-head" type="button" @click="toggle(job.id)">
            <div class="job-head-text">
              <h3>{{ localizedText(job, 'title', locale) }}</h3>
              <p class="job-summary">{{ localizedText(job, 'summary', locale) }}</p>
              <span v-if="localizedText(job, 'location', locale)" class="job-loc">
                {{ localizedText(job, 'location', locale) }}
              </span>
            </div>
            <span class="job-toggle">{{ expandedId === job.id ? t('jobCollapse') : t('jobExpand') }}</span>
          </button>
          <div v-show="expandedId === job.id" class="job-detail">
            <div v-if="localizedText(job, 'responsibilities', locale)" class="detail-block">
              <h4>{{ t('jobResponsibilities') }}</h4>
              <ul>
                <li v-for="(line, i) in linesOf(localizedText(job, 'responsibilities', locale))" :key="'r' + i">
                  {{ line }}
                </li>
              </ul>
            </div>
            <div v-if="localizedText(job, 'requirements', locale)" class="detail-block">
              <h4>{{ t('jobRequirements') }}</h4>
              <ul>
                <li v-for="(line, i) in linesOf(localizedText(job, 'requirements', locale))" :key="'q' + i">
                  {{ line }}
                </li>
              </ul>
            </div>
          </div>
        </article>
        <el-empty v-if="!loading && !jobs.length" :description="t('noData')" />
      </div>
      <div class="cta">
        <p>{{ t('joinCta') }}</p>
        <el-button type="primary" @click="$router.push('/portal/contact')">{{ t('navContact') }}</el-button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { jobApi } from '@/api'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const loading = ref(false)
const jobs = ref([])
const expandedId = ref(null)

function linesOf(text) {
  return String(text || '')
    .split(/\r?\n/)
    .map((s) => s.replace(/^\d+[\.、．)\s]+/, '').trim())
    .filter(Boolean)
}

function toggle(id) {
  expandedId.value = expandedId.value === id ? null : id
}

async function loadJobs() {
  loading.value = true
  try {
    const res = await jobApi.portalList()
    jobs.value = res.data || res || []
  } catch {
    jobs.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadJobs)
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
  color: #8eb6ff;
  margin-bottom: 10px;
}
.hero-inner h1 {
  margin: 0 0 10px;
  font-size: 28px;
}
.hero-inner p {
  margin: 0;
  color: rgba(255, 255, 255, 0.78);
  max-width: 720px;
  line-height: 1.7;
}
.jobs {
  display: flex;
  flex-direction: column;
  border: 1px solid #d5dae0;
  background: #fff;
  min-height: 80px;
}
.job-card {
  border-bottom: 1px solid #d5dae0;
}
.job-card:last-child {
  border-bottom: none;
}
.job-head {
  width: 100%;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  padding: 22px 20px;
  border: none;
  background: transparent;
  text-align: left;
  cursor: pointer;
  font-family: inherit;
}
.job-head:hover {
  background: #f7f9fc;
}
.job-head-text h3 {
  margin: 0 0 8px;
  font-size: 17px;
  color: #101820;
}
.job-summary {
  margin: 0;
  color: #5c6570;
  font-size: 14px;
  line-height: 1.6;
}
.job-loc {
  display: inline-block;
  margin-top: 8px;
  font-size: 12px;
  color: #0a4fb8;
}
.job-toggle {
  flex-shrink: 0;
  font-size: 13px;
  color: #0a4fb8;
  padding-top: 2px;
  white-space: nowrap;
}
.job-detail {
  padding: 0 20px 22px;
}
.detail-block + .detail-block {
  margin-top: 14px;
}
.detail-block h4 {
  margin: 0 0 8px;
  font-size: 14px;
  color: #101820;
}
.detail-block ul {
  margin: 0;
  padding-left: 18px;
  color: #333;
  font-size: 13px;
  line-height: 1.85;
}
.cta {
  margin-top: 28px;
  padding: 20px;
  border: 1px solid #d5dae0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}
.cta p {
  margin: 0;
  color: #333;
}
@media (max-width: 900px) {
  .cta {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
