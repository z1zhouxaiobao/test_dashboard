<template>
  <div class="portal-home">
    <section class="hero-carousel">
      <el-carousel :height="carouselHeight" :interval="5500" :arrow="isMobile ? 'never' : 'hover'" indicator-position="inside">
        <el-carousel-item v-for="item in slides" :key="item.id">
          <div class="carousel-slide" @click="goLink(item.linkUrl)">
            <img :src="resolveMediaUrl(item.imageUrl) || fallbackImg(item.id)" :alt="localizedText(item, 'title', locale)" />
            <div class="carousel-mask"></div>
            <div class="carousel-caption">
              <h1>{{ localizedText(item, 'title', locale) || t('homeHeroTitle') }}</h1>
              <p>{{ localizedText(item, 'subtitle', locale) || t('homeHeroDesc') }}</p>
              <button class="join-btn" type="button" @click.stop="$router.push('/portal/join')">
                {{ t('joinUs') }}
              </button>
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
            <button class="btn-primary" type="button" @click="$router.push('/portal/about')">{{ t('learnMore') }}</button>
            <button class="btn-ghost" type="button" @click="$router.push('/portal/honors')">{{ t('honorTitle') }}</button>
            <button class="btn-ghost" type="button" @click="$router.push('/portal/technologies')">{{ t('coreTech') }}</button>
          </div>
        </div>
        <div class="about-visual">
          <img :src="aboutImg" alt="liquid cooling diagram" />
        </div>
      </div>
    </section>

    <section class="honors-preview">
      <div class="honors-inner">
        <div class="section-header">
          <h2>{{ t('honorTitle') }}</h2>
          <router-link to="/portal/honors" class="more-link">{{ t('viewMore') }}</router-link>
        </div>
        <div v-if="honors.length" class="honor-grid">
          <article
            v-for="item in honors"
            :key="item.id"
            class="honor-item"
            role="link"
            tabindex="0"
            @click="openHonor(item)"
            @keyup.enter="openHonor(item)"
          >
            <img
              :src="resolveMediaUrl(item.coverUrl || item.imageUrl) || defaultCover('honor')"
              class="honor-img"
              alt=""
            />
            <h3>{{ localizedText(item, 'title', locale) }}</h3>
            <p>{{ localizedText(item, 'summary', locale) }}</p>
            <span class="year">{{ item.awardYear || item.year }}</span>
          </article>
        </div>
        <div v-else class="honor-empty">
          <p>{{ t('honorHomeEmpty') }}</p>
          <router-link to="/portal/honors" class="more-link">{{ t('viewMore') }}</router-link>
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
              <h3>{{ localizedText(p, 'name', locale) }}</h3>
              <p>{{ localizedText(p, 'summary', locale) }}</p>
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
import { carouselApi, productApi, honorApi } from '@/api'
import { resolveMediaUrl, defaultCover } from '@/utils/media'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const router = useRouter()
const aboutImg = '/images/about-diagram.jpg'
const carousels = ref([])
const products = ref([])
const honors = ref([])
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)
const carouselHeight = computed(() => {
  if (viewportWidth.value <= 480) return '360px'
  if (viewportWidth.value <= 768) return '460px'
  return '620px'
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

function openHonor(item) {
  const raw = String(item?.linkUrl || '').trim()
  if (raw) {
    const href = /^(https?:|mailto:|tel:)/i.test(raw) || raw.startsWith('/')
      ? raw
      : `https://${raw}`
    window.open(href, '_blank', 'noopener,noreferrer')
    return
  }
  router.push('/portal/honors')
}

onMounted(async () => {
  onResize()
  window.addEventListener('resize', onResize)
  try {
    const [cRes, pRes, hRes] = await Promise.all([
      carouselApi.portalList(),
      productApi.portalPage({ page: 1, size: 3 }),
      honorApi.portalList()
    ])
    const raw = cRes.data?.records || cRes.data || []
    carousels.value = Array.isArray(raw) ? raw : []
    products.value = (pRes.data?.records || []).slice(0, 3)
    const honorRaw = hRes.data?.records || hRes.data || []
    honors.value = (Array.isArray(honorRaw) ? honorRaw : []).slice(0, 4)
  } catch {
    carousels.value = []
    products.value = []
    honors.value = []
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.hero-carousel {
  background: #0a1628;
}
.hero-carousel :deep(.el-carousel__indicators--inside) {
  bottom: 22px;
}
.hero-carousel :deep(.el-carousel__button) {
  width: 28px;
  height: 3px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.45);
}
.hero-carousel :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background: #fff;
}
.carousel-slide {
  position: relative;
  height: 100%;
  min-height: 280px;
  background: #0a1628;
  cursor: pointer;
  overflow: hidden;
}
.carousel-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transform: scale(1.02);
}
.carousel-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(8, 18, 32, 0.35) 0%, rgba(8, 18, 32, 0.55) 55%, rgba(8, 18, 32, 0.72) 100%);
  z-index: 1;
}
.carousel-caption {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: min(920px, 90%);
  z-index: 2;
  text-align: center;
  color: #fff;
  text-shadow: 0 2px 18px rgba(0, 0, 0, 0.35);
}
.carousel-caption h1 {
  margin: 0 0 16px;
  font-size: clamp(28px, 4.2vw, 48px);
  font-weight: 800;
  line-height: 1.25;
  letter-spacing: 0.02em;
}
.carousel-caption p {
  margin: 0 auto 28px;
  max-width: 720px;
  font-size: clamp(14px, 1.5vw, 17px);
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.92);
}
.join-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 148px;
  height: 46px;
  padding: 0 32px;
  border: none;
  border-radius: 4px;
  background: #fff;
  color: #0a4fb8;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.04em;
  cursor: pointer;
  font-family: inherit;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.22);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.join-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 32px rgba(0, 0, 0, 0.28);
}
.about-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 72px 24px 56px;
}
.section-title {
  text-align: center;
  font-size: 32px;
  color: #101820;
  margin: 0 0 40px;
  font-weight: 800;
  letter-spacing: 0.02em;
}
.about-grid {
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  gap: 48px;
  align-items: stretch;
}
.about-text h3 {
  margin: 0 0 18px;
  color: #101820;
  font-size: 22px;
  font-weight: 700;
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
  padding: 11px 22px;
  font-size: 14px;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  border-radius: 4px;
}
.btn-primary {
  background: #0a4fb8;
  border: 1px solid #0a4fb8;
  color: #fff;
}
.btn-ghost {
  background: #fff;
  border: 1px solid #cfd5dc;
  color: #101820;
}
.btn-primary:hover {
  background: #0860d8;
}
.btn-ghost:hover {
  border-color: #0a4fb8;
  color: #0a4fb8;
}
.about-visual {
  border-radius: 12px;
  overflow: hidden;
  background: #f2f5f9;
  box-shadow: 0 12px 40px rgba(16, 24, 32, 0.08);
}
.about-visual img {
  display: block;
  width: 100%;
  height: 100%;
  min-height: 320px;
  object-fit: cover;
}
.honors-preview {
  background: linear-gradient(180deg, #f7f9fc 0%, #fff 100%);
}
.honors-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 56px 24px 64px;
}
.section-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
}
.section-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 800;
  color: #101820;
}
.more-link {
  color: #0a4fb8;
  font-size: 14px;
  font-weight: 600;
}
.honor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.honor-item {
  text-align: left;
  background: #fff;
  border: 1px solid #e8ebef;
  border-radius: 12px;
  padding: 18px 16px 20px;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}
.honor-item:hover {
  transform: translateY(-2px);
  border-color: #c9d7ef;
  box-shadow: 0 12px 28px rgba(16, 24, 32, 0.08);
}
.honor-item:hover h3 {
  color: #0a4fb8;
}
.honor-img {
  width: 100%;
  height: 120px;
  object-fit: contain;
  display: block;
  margin-bottom: 12px;
  background: #f7f8fa;
  border-radius: 8px;
}
.honor-item h3 {
  margin: 0 0 8px;
  font-size: 15px;
  color: #101820;
}
.honor-item p {
  margin: 0 0 10px;
  font-size: 13px;
  color: #5c6570;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.honor-item .year {
  color: #0a4fb8;
  font-weight: 700;
  font-size: 13px;
}
.honor-empty {
  padding: 28px;
  text-align: center;
  border: 1px dashed #d5dae0;
  border-radius: 12px;
  background: #fff;
}
.honor-empty p {
  margin: 0 0 10px;
  color: #5c6570;
}
.products-preview {
  background: #fff;
}
.products-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 24px 72px;
}
.product-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.product-row {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 20px;
  align-items: center;
  padding: 16px;
  border: 1px solid #e8ebef;
  border-radius: 12px;
  cursor: pointer;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}
.product-row:hover {
  border-color: #c9d7ef;
  box-shadow: 0 10px 28px rgba(16, 24, 32, 0.06);
}
.row-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  background: #f2f5f9;
}
.row-body h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #101820;
}
.row-body p {
  margin: 0;
  color: #5c6570;
  line-height: 1.6;
  font-size: 14px;
}
@media (max-width: 900px) {
  .about-grid {
    grid-template-columns: 1fr;
  }
  .honor-grid {
    grid-template-columns: 1fr 1fr;
  }
  .product-row {
    grid-template-columns: 1fr;
  }
}
@media (max-width: 560px) {
  .honor-grid {
    grid-template-columns: 1fr;
  }
  .carousel-caption h1 {
    font-size: 26px;
  }
  .join-btn {
    min-width: 132px;
    height: 42px;
  }
}
</style>
