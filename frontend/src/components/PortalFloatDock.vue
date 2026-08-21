<template>
  <aside class="float-dock" aria-label="quick contact">
    <a
      class="float-item"
      :href="presalesHref"
      :target="isExternal(presalesHref) ? '_blank' : undefined"
      :rel="isExternal(presalesHref) ? 'noopener noreferrer' : undefined"
      @click="onActionClick($event, settings.presalesBtnLink, settings.presalesPhone)"
    >
      <span class="float-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 2c-3.3 0-6 1.7-6 3.8V20h12v-2.2c0-2.1-2.7-3.8-6-3.8Z"/></svg>
      </span>
      <span class="float-label">{{ t('floatPresales') }}</span>
      <span class="float-tip">
        <strong>{{ localizedText(settings, 'presalesTitle', locale) }}</strong>
        <em v-if="settings.presalesPhone">{{ settings.presalesPhone }}</em>
      </span>
    </a>

    <a
      class="float-item"
      :href="aftersalesHref"
      :target="isExternal(aftersalesHref) ? '_blank' : undefined"
      :rel="isExternal(aftersalesHref) ? 'noopener noreferrer' : undefined"
      @click="onActionClick($event, settings.aftersalesBtnLink, settings.aftersalesPhone)"
    >
      <span class="float-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M4 4h16v2H4zm0 5h10v2H4zm0 5h16v2H4zm0 5h10v2H4z"/></svg>
      </span>
      <span class="float-label">{{ t('floatAftersales') }}</span>
      <span class="float-tip">
        <strong>{{ localizedText(settings, 'aftersalesTitle', locale) }}</strong>
        <em v-if="settings.aftersalesPhone">{{ settings.aftersalesPhone }}</em>
      </span>
    </a>

    <a
      v-if="emailHref"
      class="float-item"
      :href="emailHref"
      @click="onActionClick($event, emailHref, '')"
    >
      <span class="float-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2Zm0 4-8 5L4 8V6l8 5 8-5Z"/></svg>
      </span>
      <span class="float-label">{{ t('floatEmail') }}</span>
      <span class="float-tip">
        <strong>{{ t('contactEmail') }}</strong>
        <em>{{ settings.email }}</em>
      </span>
    </a>

    <router-link class="float-item" :to="{ path: '/portal/contact', hash: '#contact-message' }">
      <span class="float-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M4 4h16a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2H8l-4 4V6a2 2 0 0 1 2-2Z"/></svg>
      </span>
      <span class="float-label">{{ t('floatMessage') }}</span>
      <span class="float-tip">
        <strong>{{ t('onlineMsg') }}</strong>
        <em>{{ t('floatMsgHint') }}</em>
      </span>
    </router-link>

    <button
      v-show="showTop"
      class="float-item float-top"
      type="button"
      :aria-label="t('backToTop')"
      @click="scrollTop"
    >
      <span class="float-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M12 5.5 5 12.5h4v6h6v-6h4Z"/></svg>
      </span>
      <span class="float-label">{{ t('backToTop') }}</span>
    </button>
  </aside>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const props = defineProps({
  settings: {
    type: Object,
    default: () => ({})
  }
})

const { settings } = toRefs(props)
const { t, locale } = useI18n()
const showTop = ref(false)

function resolveActionHref(link, phoneFallback) {
  const raw = String(link || '').trim()
  if (raw) {
    if (/^(https?:|mailto:|tel:)/i.test(raw)) return raw
    if (raw.includes('@')) return `mailto:${raw}`
    if (/^[\d\s\-()+]+$/.test(raw)) return `tel:${raw.replace(/\s/g, '')}`
    if (raw.startsWith('/')) return raw
    return `https://${raw}`
  }
  const phone = String(phoneFallback || '').trim().replace(/\s/g, '')
  return phone ? `tel:${phone}` : '#'
}

function isExternal(href) {
  return /^https?:/i.test(String(href || ''))
}

const presalesHref = computed(() =>
  resolveActionHref(settings.value.presalesBtnLink, settings.value.presalesPhone)
)
const aftersalesHref = computed(() =>
  resolveActionHref(settings.value.aftersalesBtnLink, settings.value.aftersalesPhone)
)
const emailHref = computed(() => {
  const email = String(settings.value.email || '').trim()
  return email ? `mailto:${email}` : ''
})

function onActionClick(_e, link, phoneFallback) {
  const href = resolveActionHref(link, phoneFallback)
  if (/^mailto:/i.test(href)) {
    const email = decodeURIComponent(href.replace(/^mailto:/i, '').split('?')[0].trim())
    if (!email) return
    navigator.clipboard?.writeText(email).then(
      () => ElMessage.success(t('mailtoCopied', { email })),
      () => ElMessage.info(t('mailtoHint', { email }))
    )
    return
  }
  if (/^tel:/i.test(href)) {
    const phone = href.replace(/^tel:/i, '').trim()
    if (!phone || /Mobi|Android|iPhone|iPad/i.test(navigator.userAgent)) return
    navigator.clipboard?.writeText(phone).then(
      () => ElMessage.success(t('telCopied', { phone })),
      () => ElMessage.info(t('telHint', { phone }))
    )
  }
}

function onScroll() {
  showTop.value = window.scrollY > 420
}

function scrollTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  onScroll()
  window.addEventListener('scroll', onScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped>
.float-dock {
  position: fixed;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 90;
  display: flex;
  flex-direction: column;
  gap: 1px;
  width: 56px;
  background: #101820;
  border-radius: 10px;
  overflow: visible;
  box-shadow: 0 10px 28px rgba(16, 24, 32, 0.28);
}

.float-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  min-height: 64px;
  padding: 10px 6px;
  color: #fff;
  text-decoration: none;
  background: #101820;
  border: none;
  cursor: pointer;
  font-family: inherit;
  transition: background 0.18s ease;
}

.float-item:first-child {
  border-radius: 10px 10px 0 0;
}

.float-item:last-child {
  border-radius: 0 0 10px 10px;
}

.float-item:hover,
.float-item:focus-visible {
  background: #0a4fb8;
  outline: none;
}

.float-icon {
  display: inline-flex;
  color: #fff;
}

.float-label {
  font-size: 11px;
  line-height: 1.2;
  text-align: center;
  color: rgba(255, 255, 255, 0.92);
  max-width: 100%;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.float-tip {
  position: absolute;
  right: calc(100% + 10px);
  top: 50%;
  transform: translateY(-50%) translateX(6px);
  min-width: 160px;
  max-width: 220px;
  padding: 10px 12px;
  border-radius: 8px;
  background: #fff;
  color: #101820;
  box-shadow: 0 8px 24px rgba(16, 24, 32, 0.18);
  opacity: 0;
  visibility: hidden;
  pointer-events: none;
  transition: opacity 0.15s ease, transform 0.15s ease, visibility 0.15s;
  text-align: left;
}

.float-tip::after {
  content: '';
  position: absolute;
  right: -6px;
  top: 50%;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-left-color: #fff;
}

.float-tip strong {
  display: block;
  font-size: 13px;
  font-weight: 650;
  margin-bottom: 4px;
}

.float-tip em {
  display: block;
  font-style: normal;
  font-size: 12px;
  color: #5c6570;
  word-break: break-all;
}

.float-item:hover .float-tip,
.float-item:focus-visible .float-tip {
  opacity: 1;
  visibility: visible;
  transform: translateY(-50%) translateX(0);
}

.float-top {
  border-top: 1px solid rgba(255, 255, 255, 0.12);
}

@media (max-width: 768px) {
  .float-dock {
    right: 8px;
    width: 48px;
    top: auto;
    bottom: 88px;
    transform: none;
  }

  .float-item {
    min-height: 52px;
    padding: 8px 4px;
  }

  .float-label {
    font-size: 10px;
  }

  .float-tip {
    display: none;
  }
}
</style>
