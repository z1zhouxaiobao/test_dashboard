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
      <div class="jobs">
        <article v-for="job in jobs" :key="job.title" class="job-card">
          <h3>{{ job.title }}</h3>
          <p>{{ job.desc }}</p>
          <ul>
            <li v-for="req in job.reqs" :key="req">{{ req }}</li>
          </ul>
        </article>
      </div>
      <div class="cta">
        <p>{{ t('joinCta') }}</p>
        <el-button type="primary" @click="$router.push('/portal/contact')">{{ t('navContact') }}</el-button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()

const jobs = computed(() => {
  if (locale.value === 'en') {
    return [
      { title: 'Thermal Solution Engineer', desc: 'Design liquid cooling solutions for high-density data centers.', reqs: ['Thermal engineering background', '3+ years experience', 'On-site deployment capability'] },
      { title: 'CDU Product Manager', desc: 'Own CDU roadmap and customer delivery.', reqs: ['Mechanical / electrical background', 'B2B product experience'] },
      { title: 'Field Service Engineer', desc: 'Commissioning and O&M for liquid cooling systems.', reqs: ['Willingness to travel', 'Strong troubleshooting skills'] }
    ]
  }
  if (locale.value === 'zh-TW') {
    return [
      { title: '熱管理方案工程師', desc: '面向高密度資料中心設計液冷方案。', reqs: ['熱管理相關背景', '3年以上經驗', '具備現場交付能力'] },
      { title: 'CDU 產品經理', desc: '負責 CDU 產品規劃與客戶交付。', reqs: ['機電背景優先', 'B2B 產品經驗'] },
      { title: '現場服務工程師', desc: '負責液冷系統調試與運維。', reqs: ['可接受出差', '故障排查能力強'] }
    ]
  }
  return [
    { title: '热管理方案工程师', desc: '面向高密度数据中心设计液冷解决方案。', reqs: ['热管理相关背景', '3年以上经验', '具备现场交付能力'] },
    { title: 'CDU 产品经理', desc: '负责 CDU 产品规划与客户交付。', reqs: ['机电背景优先', 'B2B 产品经验'] },
    { title: '现场服务工程师', desc: '负责液冷系统调试与运维支持。', reqs: ['可接受出差', '故障排查能力强'] }
  ]
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
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  border: 1px solid #d5dae0;
}
.job-card {
  padding: 22px 20px;
  border-right: 1px solid #d5dae0;
  background: #fff;
}
.job-card:last-child {
  border-right: none;
}
.job-card h3 {
  margin: 0 0 10px;
  font-size: 17px;
}
.job-card p {
  color: #5c6570;
  font-size: 14px;
  line-height: 1.6;
}
.job-card ul {
  margin: 12px 0 0;
  padding-left: 18px;
  color: #333;
  font-size: 13px;
  line-height: 1.8;
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
  .jobs {
    grid-template-columns: 1fr;
  }
  .job-card {
    border-right: none;
    border-bottom: 1px solid #d5dae0;
  }
  .cta {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
