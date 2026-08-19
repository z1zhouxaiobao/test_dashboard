<template>
  <div class="portal-home">
    <section class="hero-carousel">
      <el-carousel height="520px" :interval="5000" arrow="always" indicator-position="inside">
        <el-carousel-item v-for="item in slides" :key="item.id">
          <div class="carousel-slide" @click="goLink(item.linkUrl)">
            <img :src="resolveMediaUrl(item.imageUrl) || fallbackImg(item.id)" :alt="item.title" />
            <div class="carousel-mask"></div>
            <div class="carousel-caption">
              <h1>{{ item.title || t('homeHeroTitle') }}</h1>
              <p>{{ item.subtitle || t('homeHeroDesc') }}</p>
              <el-button class="join-btn" size="large" @click.stop="$router.push('/portal/contact')">
                {{ t('joinUs') }}
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>


    <section class="about-section">
      <h2 class="section-title">{{ t('aboutTitle') }}</h2>
      <div class="about-grid">
        <div class="about-text">
          <h3>{{ t('aboutSub') }}</h3>
          <p>{{ t('aboutP1') }}</p>
          <p>{{ t('aboutP2') }}</p>
          <div class="about-actions">
            <el-button type="primary" @click="$router.push('/portal/about')">{{ t('learnMore') }}</el-button>
            <el-button @click="$router.push('/portal/technologies')">{{ t('coreTech') }}</el-button>
          </div>
        </div>
        <div class="about-visual">
          <img :src="aboutImg" alt="liquid cooling diagram" />
        </div>
      </div>
    </section>

    <section class="portal-section products-preview">
      <div class="section-header">
        <h2 class="section-title">{{ t('coreSolutions') }}</h2>
        <router-link to="/portal/products">{{ t('viewMore') }}</router-link>
      </div>
      <el-row :gutter="20">
        <el-col v-for="p in products" :key="p.id" :xs="24" :sm="12" :md="8">
          <el-card shadow="hover" class="product-card" @click="$router.push(`/portal/products/${p.id}`)">
            <img :src="resolveMediaUrl(p.coverUrl) || defaultCover('product')" class="card-img" alt="" />
            <h3>{{ p.name }}</h3>
            <p>{{ p.summary }}</p>
          </el-card>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { carouselApi, productApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const router = useRouter()
const aboutImg = '/images/about-diagram.jpg'
const carousels = ref([])
const products = ref([])

const fallbackSlides = [
  {
    id: 'f1',
    title: '数据中心液冷解决方案专家',
    subtitle: '专业的热管理技术，高效节能的冷却系统，为您的数据中心提供最佳解决方案',
    imageUrl: '/images/carousel-1.jpg',
    linkUrl: '/portal/products'
  },
  {
    id: 'f2',
    title: '高效节能 CDU 冷量分配系统',
    subtitle: '模块化 CDU，智能流量与温度控制',
    imageUrl: '/images/carousel-2.jpg',
    linkUrl: '/portal/technologies'
  },
  {
    id: 'f3',
    title: '助力绿色算力 PUE 低至 1.06',
    subtitle: '助力绿色数据中心建设，显著降低机房能耗',
    imageUrl: '/images/carousel-3.jpg',
    linkUrl: '/portal/cases'
  }
]

const slides = computed(() => (Array.isArray(carousels.value) && carousels.value.length ? carousels.value : fallbackSlides))

function fallbackImg(id) {
  const map = { 1: '/images/carousel-1.jpg', 2: '/images/carousel-2.jpg', 3: '/images/carousel-3.jpg' }
  return map[id] || '/images/carousel-1.jpg'
}

function goLink(url) {
  if (!url) return
  if (url.startsWith('http')) window.open(url, '_blank')
  else router.push(url)
}

onMounted(async () => {
  try {
    const [cRes, pRes] = await Promise.all([
      carouselApi.portalList(),
      productApi.portalPage({ page: 1, size: 3 })
    ])
    const raw = cRes.data?.records || cRes.data || []
    carousels.value = Array.isArray(raw) ? raw : []
    products.value = (pRes.data?.records || []).slice(0, 3)
  } catch {
    carousels.value = []
    products.value = []
  }
})
</script>

<style scoped>
.hero-carousel {
  background: #0a1628;
}
.carousel-slide {
  position: relative;
  height: 520px;
  background: #0a1628;
  cursor: pointer;
}
.carousel-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.carousel-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(10, 22, 40, 0.35) 0%, rgba(10, 22, 40, 0.62) 100%);
}
.carousel-caption {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: min(900px, 90%);
  text-align: center;
  color: #fff;
  text-shadow: 0 2px 12px rgba(0, 0, 0, 0.45);
  z-index: 2;
}
.carousel-caption h1 {
  margin: 0 0 16px;
  font-size: 40px;
  font-weight: 800;
}
.carousel-caption p {
  margin: 0 auto 28px;
  max-width: 720px;
  font-size: 16px;
  line-height: 1.8;
}
.join-btn {
  background: #fff !important;
  border-color: #fff !important;
  color: #0B5ED7 !important;
  padding: 12px 36px;
  font-weight: 600;
}
.about-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 64px 24px 40px;
}
.section-title {
  text-align: center;
  font-size: 32px;
  color: #1a1a1a;
  margin: 0 0 36px;
  font-weight: 700;
}
.about-grid {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 40px;
  align-items: center;
}
.about-text h3 {
  margin: 0 0 16px;
  color: #0B5ED7;
  font-size: 22px;
}
.about-text p {
  margin: 0 0 14px;
  color: #444;
  line-height: 1.9;
  font-size: 15px;
}
.about-actions {
  margin-top: 20px;
  display: flex;
  gap: 12px;
}
.about-visual {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(11, 94, 215, 0.12);
  background: #f5f8ff;
}
.about-visual img {
  display: block;
  width: 100%;
  height: 340px;
  object-fit: cover;
}
.products-preview {
  background: #f7f9fc;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.section-header .section-title {
  margin: 0;
  font-size: 28px;
}
.product-card {
  cursor: pointer;
  margin-bottom: 20px;
  min-height: 100%;
}
.card-img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 4px;
}
.product-card h3 {
  margin: 12px 0 8px;
  color: #0a1628;
  font-size: 16px;
}
.product-card p {
  color: #666;
  font-size: 14px;
  margin: 0;
  line-height: 1.6;
}
@media (max-width: 900px) {
  .carousel-caption h1 {
    font-size: 26px;
  }
  .about-grid {
    grid-template-columns: 1fr;
  }
}
</style>
