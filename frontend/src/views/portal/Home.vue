<template>
  <div class="portal-home">
    <section class="hero-carousel">
      <el-carousel :height="carouselHeight" :interval="5500" :arrow="isMobile ? 'never' : 'always'" indicator-position="outside">
        <el-carousel-item v-for="item in slides" :key="item.id">
          <div class="carousel-slide" @click="goLink(item.linkUrl)">
            <img :src="resolveMediaUrl(item.imageUrl) || fallbackImg(item.id)" :alt="item.title" />
            <div class="carousel-caption">
              <div class="caption-kicker">LIQUICOOL</div>
              <h1>{{ item.title || t('homeHeroTitle') }}</h1>
              <p>{{ item.subtitle || t('homeHeroDesc') }}</p>
              <button class="join-btn" type="button" @click.stop="$router.push('/portal/contact')">
                {{ t('joinUs') }}
              </button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <section class="about-section">
      <div class="section-label">{{ t('aboutTitle') }}</div>
      <div class="about-grid">
        <div class="about-text">
          <h2>{{ t('aboutSub') }}</h2>
          <p>{{ t('aboutP1') }}</p>
          <p>{{ t('aboutP2') }}</p>
          <div class="about-actions">
            <button class="btn-primary" type="button" @click="$router.push('/portal/about')">{{ t('learnMore') }}</button>
            <button class="btn-ghost" type="button" @click="$router.push('/portal/technologies')">{{ t('coreTech') }}</button>
          </div>
        </div>
        <div class="about-visual">
          <img :src="aboutImg" alt="liquid cooling diagram" />
        </div>
      </div>
    </section>

    <section class="products-preview">
      <div class="products-inner">
        <div class="section-header">
          <h2>{{ t('coreSolutions') }}</h2>
          <router-link to="/portal/products" class="more-link">{{ t('viewMore') }}</router-link>
        </div>
        <div class="product-list">
          <article
            v-for="p in products"
            :key="p.id"
            class="product-row"
            @click="$router.push(`/portal/products/${p.id}`)"
          >
            <img :src="resolveMediaUrl(p.coverUrl) || defaultCover('product')" class="row-img" alt="" />
            <div class="row-body">
              <h3>{{ p.name }}</h3>
              <p>{{ p.summary }}</p>
            </div>
          </article>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { carouselApi, productApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const router = useRouter()
const aboutImg = '/images/about-diagram.jpg'
const carousels = ref([])
const products = ref([])
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)
const carouselHeight = computed(() => {
  if (viewportWidth.value <= 480) return '320px'
  if (viewportWidth.value <= 768) return '400px'
  return '560px'
})

function onResize() {
  viewportWidth.value = window.innerWidth
}

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
  onResize()
  window.addEventListener('resize', onResize)
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

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.hero-carousel {
  background: #101820;
}
.hero-carousel :deep(.el-carousel__indicators--outside) {
  margin-top: 0;
  background: #101820;
  padding: 10px 0 12px;
}
.hero-carousel :deep(.el-carousel__button) {
  width: 28px;
  height: 3px;
  border-radius: 0;
  background: rgba(255, 255, 255, 0.35);
}
.hero-carousel :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background: #0a4fb8;
}
.carousel-slide {
  position: relative;
  height: 100%;
  min-height: 280px;
  background: #101820;
  cursor: pointer;
}
.carousel-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  filter: grayscale(18%) contrast(1.05);
}
.carousel-caption {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: min(520px, 88%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px 48px;
  background: linear-gradient(90deg, rgba(16, 24, 32, 0.92) 0%, rgba(16, 24, 32, 0.72) 70%, rgba(16, 24, 32, 0) 100%);
  color: #fff;
  z-index: 2;
  text-align: left;
}
.caption-kicker {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.22em;
  color: #8eb6ff;
  margin-bottom: 14px;
}
.carousel-caption h1 {
  margin: 0 0 14px;
  font-size: 34px;
  font-weight: 700;
  line-height: 1.25;
  letter-spacing: 0.02em;
  max-width: 420px;
}
.carousel-caption p {
  margin: 0 0 24px;
  max-width: 380px;
  font-size: 15px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.82);
}
.join-btn {
  align-self: flex-start;
  background: #0a4fb8;
  border: 1px solid #0a4fb8;
  color: #fff;
  padding: 11px 28px;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.04em;
  cursor: pointer;
  font-family: inherit;
}
.join-btn:hover {
  background: #083d90;
  border-color: #083d90;
}
.about-section {
  max-width: 1180px;
  margin: 0 auto;
  padding: 64px 24px 56px;
  border-bottom: 1px solid #d5dae0;
}
.section-label {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.18em;
  text-transform: uppercase;
  color: #0a4fb8;
  margin-bottom: 12px;
}
.about-grid {
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  gap: 48px;
  align-items: stretch;
}
.about-text h2 {
  margin: 0 0 18px;
  color: #101820;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 0.02em;
}
.about-text p {
  margin: 0 0 14px;
  color: #5c6570;
  line-height: 1.85;
  font-size: 15px;
}
.about-actions {
  margin-top: 28px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.btn-primary,
.btn-ghost {
  padding: 10px 22px;
  font-size: 14px;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  border-radius: 0;
}
.btn-primary {
  background: #0a4fb8;
  border: 1px solid #0a4fb8;
  color: #fff;
}
.btn-ghost {
  background: #fff;
  border: 1px solid #101820;
  color: #101820;
}
.btn-primary:hover {
  background: #083d90;
}
.btn-ghost:hover {
  background: #f2f3f5;
}
.about-visual {
  border: 1px solid #d5dae0;
  background: #f2f3f5;
  overflow: hidden;
}
.about-visual img {
  display: block;
  width: 100%;
  height: 100%;
  min-height: 320px;
  object-fit: cover;
  filter: grayscale(10%);
}
.products-preview {
  background: #f2f3f5;
}
.products-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 56px 24px 64px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 0;
  padding-bottom: 16px;
  border-bottom: 2px solid #101820;
}
.section-header h2 {
  margin: 0;
  font-size: 24px;
  color: #101820;
  font-weight: 700;
}
.more-link {
  color: #0a4fb8;
  font-size: 14px;
  font-weight: 500;
}
.product-list {
  display: flex;
  flex-direction: column;
}
.product-row {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 24px;
  align-items: center;
  padding: 22px 0;
  border-bottom: 1px solid #d5dae0;
  cursor: pointer;
  background: transparent;
}
.product-row:hover .row-body h3 {
  color: #0a4fb8;
}
.row-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
  border: 1px solid #d5dae0;
  background: #fff;
}
.row-body h3 {
  margin: 0 0 8px;
  color: #101820;
  font-size: 17px;
  font-weight: 600;
  transition: color 0.15s;
}
.row-body p {
  color: #5c6570;
  font-size: 14px;
  margin: 0;
  line-height: 1.65;
}

@media (max-width: 768px) {
  .carousel-caption {
    width: 100%;
    padding: 28px 20px;
    background: linear-gradient(0deg, rgba(16, 24, 32, 0.9) 0%, rgba(16, 24, 32, 0.55) 55%, rgba(16, 24, 32, 0.2) 100%);
    justify-content: flex-end;
  }
  .carousel-caption h1 {
    font-size: 24px;
    max-width: none;
  }
  .carousel-caption p {
    font-size: 14px;
    max-width: none;
    margin-bottom: 18px;
  }
  .about-section {
    padding: 40px 14px 36px;
  }
  .about-text h2 {
    font-size: 22px;
  }
  .about-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  .about-visual img {
    min-height: 200px;
    height: 220px;
  }
  .products-inner {
    padding: 36px 14px 44px;
  }
  .section-header h2 {
    font-size: 20px;
  }
  .product-row {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  .row-img {
    height: 160px;
  }
}

@media (max-width: 480px) {
  .carousel-caption h1 {
    font-size: 20px;
  }
  .carousel-caption p {
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}
</style>
