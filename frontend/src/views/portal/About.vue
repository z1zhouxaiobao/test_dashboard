<template>
  <div class="about-page">
    <section class="hero-mini">
      <h1>{{ t('aboutPageTitle') }}</h1>
      <p>{{ t('aboutPageSub') }}</p>
    </section>

    <section class="content">
      <div class="grid">
        <div class="text">
          <h2>{{ t('aboutSub') }}</h2>
          <p>{{ t('aboutP1') }}</p>
          <p>{{ t('aboutP2') }}</p>
          <ul>
            <li>{{ t('mission') }}</li>
            <li>{{ t('vision') }}</li>
            <li>{{ t('values') }}</li>
          </ul>
        </div>
        <div class="visual">
          <img src="/images/about-diagram.jpg" alt="about" />
        </div>
      </div>

      <div class="stats">
        <div v-for="s in stats" :key="s.labelKey" class="stat-item">
          <div class="stat-value">{{ s.value }}</div>
          <div class="stat-label">{{ t(s.labelKey) }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const aboutImg = '/images/about-diagram.jpg'

const stats = [
  { value: '10+', labelKey: 'statExp' },
  { value: '200+', labelKey: 'statCases' },
  { value: '50+', labelKey: 'statPatents' },
  { value: '1.06', labelKey: 'statPue' }
]
</script>

<style scoped>
.hero-mini {
  background: #0B5ED7;
  color: #fff;
  text-align: center;
  padding: 56px 24px;
}
.hero-mini h1 {
  margin: 0 0 10px;
  font-size: 32px;
}
.hero-mini p {
  margin: 0;
  opacity: 0.92;
}
.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 24px 64px;
}
.grid {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 40px;
  align-items: center;
}
.text h2 {
  margin: 0 0 16px;
  color: #0B5ED7;
}
.text p,
.text li {
  line-height: 1.9;
  color: #444;
}
.visual img {
  width: 100%;
  height: 360px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 10px 28px rgba(11, 94, 215, 0.12);
}
.stats {
  margin-top: 40px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.stat-item {
  text-align: center;
  padding: 24px 12px;
  border-radius: 8px;
  color: #fff;
  background: linear-gradient(135deg, #0B5ED7, #0a1628);
}
.stat-value {
  font-size: 30px;
  font-weight: 700;
}
.stat-label {
  margin-top: 6px;
  font-size: 13px;
  opacity: 0.9;
}
@media (max-width: 900px) {
  .grid,
  .stats {
    grid-template-columns: 1fr;
  }
}
</style>
