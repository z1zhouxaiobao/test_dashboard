<template>
  <div class="about-page">
    <section class="hero-mini">
      <div class="hero-inner">
        <div class="kicker">ABOUT</div>
        <h1>{{ t('aboutPageTitle') }}</h1>
        <p>{{ t('aboutPageSub') }}</p>
      </div>
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

const stats = [
  { value: '10+', labelKey: 'statExp' },
  { value: '200+', labelKey: 'statCases' },
  { value: '50+', labelKey: 'statPatents' },
  { value: '1.06', labelKey: 'statPue' }
]
</script>

<style scoped>
.hero-mini {
  background: #101820;
  color: #fff;
  border-bottom: 3px solid #0a4fb8;
}
.hero-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 48px 24px;
  text-align: left;
}
.kicker {
  font-size: 11px;
  letter-spacing: 0.2em;
  font-weight: 600;
  color: #8eb6ff;
  margin-bottom: 10px;
}
.hero-mini h1 {
  margin: 0 0 10px;
  font-size: 30px;
  font-weight: 700;
  letter-spacing: 0.02em;
}
.hero-mini p {
  margin: 0;
  color: rgba(255, 255, 255, 0.75);
  font-size: 15px;
}
.content {
  max-width: 1180px;
  margin: 0 auto;
  padding: 48px 24px 64px;
}
.grid {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 40px;
  align-items: start;
}
.text h2 {
  margin: 0 0 16px;
  color: #101820;
  font-size: 24px;
}
.text p,
.text li {
  line-height: 1.85;
  color: #5c6570;
  font-size: 15px;
}
.text ul {
  margin: 20px 0 0;
  padding-left: 18px;
}
.visual {
  border: 1px solid #d5dae0;
  background: #f2f3f5;
}
.visual img {
  width: 100%;
  height: 360px;
  object-fit: cover;
  display: block;
  filter: grayscale(8%);
}
.stats {
  margin-top: 40px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  border: 1px solid #d5dae0;
}
.stat-item {
  text-align: left;
  padding: 22px 20px;
  background: #fff;
  border-right: 1px solid #d5dae0;
}
.stat-item:last-child {
  border-right: none;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #0a4fb8;
  font-family: 'IBM Plex Sans', sans-serif;
  letter-spacing: 0.02em;
}
.stat-label {
  margin-top: 6px;
  font-size: 13px;
  color: #5c6570;
}
@media (max-width: 768px) {
  .hero-inner {
    padding: 36px 16px;
  }
  .hero-mini h1 {
    font-size: 24px;
  }
  .content {
    padding: 28px 14px 40px;
  }
  .grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  .visual img {
    height: 220px;
  }
  .stats {
    grid-template-columns: repeat(2, 1fr);
  }
  .stat-item {
    border-right: 1px solid #d5dae0;
    border-bottom: 1px solid #d5dae0;
  }
  .stat-item:nth-child(2n) {
    border-right: none;
  }
  .stat-item:nth-child(n + 3) {
    border-bottom: none;
  }
}

@media (max-width: 480px) {
  .hero-mini h1 {
    font-size: 20px;
  }
  .stats {
    grid-template-columns: 1fr;
  }
  .stat-item {
    border-right: none;
    border-bottom: 1px solid #d5dae0;
  }
  .stat-item:last-child {
    border-bottom: none;
  }
  .stat-value {
    font-size: 24px;
  }
}
</style>
