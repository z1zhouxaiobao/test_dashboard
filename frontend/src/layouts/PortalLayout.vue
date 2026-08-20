<template>
  <div class="portal-layout" :key="locale">
    <header class="portal-header">
      <div class="header-inner">
        <router-link to="/portal/home" class="brand" @click="closeMenu">
          <div class="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 40 40" width="36" height="36">
              <rect x="2" y="2" width="36" height="36" fill="#101820" />
              <path d="M10 26V14h6.2c3.4 0 5.4 1.7 5.4 4.4 0 2.8-2 4.5-5.4 4.5H14.6V26H10zm4.6-6.4h1.4c1.4 0 2.2-.7 2.2-1.7s-.8-1.6-2.2-1.6h-1.4v3.3z" fill="#fff" />
              <rect x="24" y="14" width="6" height="12" fill="#0a4fb8" />
            </svg>
          </div>
          <div class="brand-text">
            <div class="brand-en">LIQUICOOL</div>
            <div class="brand-cn">{{ t('brandCn') }}</div>
          </div>
        </router-link>

        <nav class="nav-menu" :class="{ open: menuOpen }">
          <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="nav-item"
            active-class="active"
            @click="closeMenu"
          >
            {{ t(item.labelKey) }}
          </router-link>
        </nav>

        <div class="header-actions">
          <LangSwitcher variant="portal" />
          <button
            class="menu-toggle"
            type="button"
            :aria-expanded="menuOpen"
            aria-label="menu"
            @click="menuOpen = !menuOpen"
          >
            <span></span>
            <span></span>
            <span></span>
          </button>
        </div>
      </div>
    </header>

    <div v-if="menuOpen" class="nav-backdrop" @click="closeMenu"></div>

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
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from '@/composables/useI18n'
import LangSwitcher from '@/components/LangSwitcher.vue'

const { t, locale } = useI18n()
const route = useRoute()
const menuOpen = ref(false)

const navItems = computed(() => [
  { labelKey: 'navHome', path: '/portal/home' },
  { labelKey: 'navAbout', path: '/portal/about' },
  { labelKey: 'navTech', path: '/portal/technologies' },
  { labelKey: 'navHonor', path: '/portal/honors' },
  { labelKey: 'navContact', path: '/portal/contact' }
])

function closeMenu() {
  menuOpen.value = false
}

watch(() => route.fullPath, closeMenu)
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
  border-bottom: 2px solid #101820;
}
.header-inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 0 24px;
  height: 68px;
  display: flex;
  align-items: center;
  gap: 28px;
  position: relative;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.brand-mark svg {
  width: 36px;
  height: 36px;
  display: block;
}
.brand-text {
  line-height: 1.1;
}
.brand-en {
  color: #101820;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.08em;
  font-family: 'IBM Plex Sans', sans-serif;
}
.brand-cn {
  color: #5c6570;
  font-size: 11px;
  margin-top: 3px;
  letter-spacing: 0.04em;
}
.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 0;
}
.nav-item {
  padding: 8px 14px;
  color: #1a1f26;
  font-size: 14px;
  font-weight: 500;
  border-radius: 0;
  border-bottom: 2px solid transparent;
  transition: color 0.15s, border-color 0.15s;
  white-space: nowrap;
}
.nav-item:hover,
.nav-item.active {
  color: #0a4fb8;
  background: transparent;
  border-bottom-color: #0a4fb8;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
  margin-left: auto;
}
.menu-toggle {
  display: none;
  width: 40px;
  height: 40px;
  border: 1px solid #d5dae0;
  border-radius: 0;
  background: #fff;
  padding: 10px 9px;
  cursor: pointer;
  flex-direction: column;
  justify-content: space-between;
}
.menu-toggle span {
  display: block;
  height: 2px;
  background: #101820;
  border-radius: 0;
  transition: transform 0.2s, opacity 0.2s;
}
.nav-backdrop {
  display: none;
}
.portal-main {
  flex: 1;
}
.portal-footer {
  background: #101820;
  color: rgba(255, 255, 255, 0.72);
  padding: 40px 24px 28px;
  border-top: 3px solid #0a4fb8;
}
.footer-inner {
  max-width: 1180px;
  margin: 0 auto;
  text-align: left;
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 20px 40px;
  align-items: end;
}
.footer-brand .brand-en {
  color: #fff;
  margin-bottom: 8px;
}
.footer-brand p {
  margin: 0;
  max-width: 360px;
  line-height: 1.6;
  font-size: 14px;
}
.footer-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 24px;
  justify-content: flex-end;
}
.footer-links a {
  color: rgba(255, 255, 255, 0.78);
  font-size: 14px;
}
.footer-links a:hover {
  color: #fff;
}
.copyright {
  grid-column: 1 / -1;
  margin: 8px 0 0;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  font-size: 12px;
  opacity: 0.65;
}

@media (max-width: 768px) {
  .header-inner {
    height: 58px;
    padding: 0 14px;
    gap: 12px;
  }
  .brand-mark svg {
    width: 32px;
    height: 32px;
  }
  .brand-en {
    font-size: 16px;
  }
  .brand-cn {
    font-size: 10px;
  }
  .menu-toggle {
    display: inline-flex;
  }
  .nav-menu {
    display: none;
    position: absolute;
    left: 0;
    right: 0;
    top: 58px;
    flex-direction: column;
    align-items: stretch;
    gap: 0;
    padding: 0;
    background: #fff;
    border-bottom: 2px solid #101820;
    box-shadow: none;
    z-index: 120;
  }
  .nav-menu.open {
    display: flex;
  }
  .nav-item {
    padding: 14px 16px;
    font-size: 15px;
    border-bottom: 1px solid #e8ebef;
  }
  .nav-item.active {
    border-bottom-color: #0a4fb8;
    background: #f5f7fa;
  }
  .nav-backdrop {
    display: block;
    position: fixed;
    inset: 58px 0 0;
    background: rgba(16, 24, 32, 0.35);
    z-index: 90;
  }
  .footer-inner {
    grid-template-columns: 1fr;
    text-align: left;
  }
  .footer-links {
    justify-content: flex-start;
  }
  .portal-footer {
    padding: 28px 16px 20px;
  }
}

@media (max-width: 480px) {
  .brand-cn {
    display: none;
  }
}
</style>
