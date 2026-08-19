<template>
  <div class="portal-layout" :key="locale">
    <header class="portal-header">
      <div class="header-inner">
        <router-link to="/portal/home" class="brand">
          <div class="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 48 48" width="42" height="42">
              <defs>
                <linearGradient id="lc" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0%" stop-color="#0B5ED7" />
                  <stop offset="100%" stop-color="#DC3545" />
                </linearGradient>
              </defs>
              <circle cx="24" cy="24" r="22" fill="none" stroke="url(#lc)" stroke-width="3" />
              <path d="M14 28c4-12 16-16 22-8 2 3 2 7-1 10-4 4-12 5-18 2" fill="none" stroke="#0B5ED7" stroke-width="3" stroke-linecap="round" />
              <path d="M16 20c6-2 12 0 16 6" fill="none" stroke="#DC3545" stroke-width="2.5" stroke-linecap="round" />
            </svg>
          </div>
          <div class="brand-text">
            <div class="brand-en">LIQUICOOL</div>
            <div class="brand-cn">{{ t('brandCn') }}</div>
          </div>
        </router-link>

        <nav class="nav-menu">
          <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="nav-item"
            active-class="active"
          >
            {{ t(item.labelKey) }}
          </router-link>
        </nav>

        <div class="header-actions">
          <LangSwitcher variant="portal" />
        </div>
      </div>
    </header>

    <main class="portal-main">
      <router-view />
    </main>

    <footer class="portal-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="brand-en">LIQUICOOL</div>
          <p>{{ t('footerSlogan') }}</p>
        </div>
        <div class="footer-links">
          <router-link to="/portal/products">{{ t('products') }}</router-link>
          <router-link to="/portal/cases">{{ t('cases') }}</router-link>
          <router-link to="/portal/news">{{ t('news') }}</router-link>
          <router-link to="/portal/contact">{{ t('navContact') }}</router-link>
        </div>
        <p class="copyright">{{ t('copyright') }}</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from '@/composables/useI18n'
import LangSwitcher from '@/components/LangSwitcher.vue'

const { t, locale } = useI18n()

const navItems = computed(() => [
  { labelKey: 'navHome', path: '/portal/home' },
  { labelKey: 'navAbout', path: '/portal/about' },
  { labelKey: 'navTech', path: '/portal/technologies' },
  { labelKey: 'navHonor', path: '/portal/honors' },
  { labelKey: 'navContact', path: '/portal/contact' }
])

</script>

<style scoped>
.portal-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.portal-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  border-bottom: 1px solid #eef1f6;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  gap: 28px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.brand-text {
  line-height: 1.15;
}
.brand-en {
  color: #0B5ED7;
  font-size: 22px;
  font-weight: 800;
  letter-spacing: 1px;
}
.brand-cn {
  color: #666;
  font-size: 12px;
  margin-top: 2px;
}
.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 8px;
}
.nav-item {
  padding: 8px 16px;
  color: #222;
  font-size: 15px;
  border-radius: 4px;
  transition: all 0.2s;
}
.nav-item:hover,
.nav-item.active {
  color: #0B5ED7;
  background: rgba(11, 94, 215, 0.08);
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.portal-main {
  flex: 1;
}
.portal-footer {
  background: #0a1628;
  color: rgba(255, 255, 255, 0.75);
  padding: 36px 24px 24px;
}
.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}
.footer-brand .brand-en {
  color: #fff;
  margin-bottom: 6px;
}
.footer-links {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin: 16px 0 20px;
}
.footer-links a {
  color: rgba(255, 255, 255, 0.8);
}
.footer-links a:hover {
  color: #fff;
}
.copyright {
  margin: 0;
  font-size: 13px;
  opacity: 0.7;
}
@media (max-width: 960px) {
  .header-inner {
    height: auto;
    flex-wrap: wrap;
    padding: 12px 16px;
  }
  .nav-menu {
    order: 3;
    width: 100%;
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>
