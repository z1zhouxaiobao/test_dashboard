<template>
  <div class="portal-layout" :key="locale">
    <header class="portal-header">
      <div class="header-inner">
        <router-link to="/portal/home" class="brand" @click="closeMenu">
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
  position: relative;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.brand-mark svg {
  width: 42px;
  height: 42px;
  display: block;
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
  white-space: nowrap;
}
.nav-item:hover,
.nav-item.active {
  color: #0B5ED7;
  background: rgba(11, 94, 215, 0.08);
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
  border: none;
  border-radius: 8px;
  background: #f3f6fb;
  padding: 10px 9px;
  cursor: pointer;
  flex-direction: column;
  justify-content: space-between;
}
.menu-toggle span {
  display: block;
  height: 2px;
  background: #0a1628;
  border-radius: 2px;
  transition: transform 0.2s, opacity 0.2s;
}
.nav-backdrop {
  display: none;
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
  flex-wrap: wrap;
  gap: 16px 24px;
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
  padding: 0 8px;
}

@media (max-width: 768px) {
  .header-inner {
    height: 60px;
    padding: 0 14px;
    gap: 12px;
  }
  .brand-mark svg {
    width: 34px;
    height: 34px;
  }
  .brand-en {
    font-size: 18px;
  }
  .brand-cn {
    font-size: 11px;
  }
  .menu-toggle {
    display: inline-flex;
  }
  .nav-menu {
    display: none;
    position: absolute;
    left: 0;
    right: 0;
    top: 60px;
    flex-direction: column;
    align-items: stretch;
    gap: 4px;
    padding: 12px;
    background: #fff;
    border-bottom: 1px solid #eef1f6;
    box-shadow: 0 12px 24px rgba(10, 22, 40, 0.08);
    z-index: 120;
  }
  .nav-menu.open {
    display: flex;
  }
  .nav-item {
    padding: 12px 14px;
    font-size: 15px;
  }
  .nav-backdrop {
    display: block;
    position: fixed;
    inset: 60px 0 0;
    background: rgba(10, 22, 40, 0.28);
    z-index: 90;
  }
  .portal-footer {
    padding: 28px 16px 20px;
  }
  .footer-links {
    gap: 12px 18px;
  }
}

@media (max-width: 480px) {
  .brand-cn {
    display: none;
  }
  .copyright {
    font-size: 12px;
    line-height: 1.6;
  }
}
</style>
