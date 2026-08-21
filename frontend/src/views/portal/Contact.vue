<template>
  <div class="contact-page">
    <section class="portal-section contact-top">
      <!-- <h1 class="page-title">{{ t('contactTitle') }}</h1> -->
      <h2 class="talk-title">{{ localizedText(settings, 'talkNow', locale) }}</h2>

      <div class="service-list">
        <article class="service-row">
          <div class="service-icon" aria-hidden="true">
            <svg viewBox="0 0 64 64" width="56" height="56">
              <circle cx="32" cy="22" r="10" fill="none" stroke="#101820" stroke-width="2.5" />
              <path d="M18 52c2-12 10-18 14-18s12 6 14 18" fill="none" stroke="#101820" stroke-width="2.5" />
              <path d="M14 28h8M42 28h8" stroke="#101820" stroke-width="2.5" />
              <path d="M44 18c6 2 8 8 6 14" fill="none" stroke="#0a4fb8" stroke-width="2.5" />
            </svg>
          </div>
          <div class="service-body">
            <h3>{{ localizedText(settings, 'presalesTitle', locale) }}</h3>
            <p>{{ localizedText(settings, 'presalesDesc', locale) }}</p>
            <strong class="service-phone">{{ settings.presalesPhone }}</strong>
          </div>
          <a
            class="service-btn"
            :href="resolveActionHref(settings.presalesBtnLink, settings.presalesPhone)"
            :target="isExternalHref(settings.presalesBtnLink) ? '_blank' : undefined"
            :rel="isExternalHref(settings.presalesBtnLink) ? 'noopener noreferrer' : undefined"
            @click="onServiceBtnClick($event, settings.presalesBtnLink, settings.presalesPhone)"
          >{{ localizedText(settings, 'presalesBtn', locale) }}</a>
        </article>

        <article class="service-row">
          <div class="service-icon" aria-hidden="true">
            <svg viewBox="0 0 64 64" width="56" height="56">
              <circle cx="32" cy="22" r="10" fill="none" stroke="#101820" stroke-width="2.5" />
              <path d="M18 52c2-12 10-18 14-18s12 6 14 18" fill="none" stroke="#101820" stroke-width="2.5" />
              <rect x="40" y="14" width="14" height="18" rx="1" fill="none" stroke="#0a4fb8" stroke-width="2.5" />
              <path d="M44 22h6M44 26h6" stroke="#0a4fb8" stroke-width="2" />
            </svg>
          </div>
          <div class="service-body">
            <h3>{{ localizedText(settings, 'aftersalesTitle', locale) }}</h3>
            <p>{{ localizedText(settings, 'aftersalesDesc', locale) }}</p>
            <strong class="service-phone">{{ settings.aftersalesPhone }}</strong>
          </div>
          <a
            class="service-btn"
            :href="resolveActionHref(settings.aftersalesBtnLink, settings.aftersalesPhone)"
            :target="isExternalHref(settings.aftersalesBtnLink) ? '_blank' : undefined"
            :rel="isExternalHref(settings.aftersalesBtnLink) ? 'noopener noreferrer' : undefined"
            @click="onServiceBtnClick($event, settings.aftersalesBtnLink, settings.aftersalesPhone)"
          >{{ localizedText(settings, 'aftersalesBtn', locale) }}</a>
        </article>
      </div>
    </section>

    <nav class="contact-ribbon" aria-label="contact sections">
      <a href="#contact-sales" @click.prevent="scrollTo('contact-sales')">{{ t('ribbonSales') }}</a>
      <a href="#contact-support" @click.prevent="scrollTo('contact-support')">{{ t('ribbonSupport') }}</a>
      <a href="#contact-company" @click.prevent="scrollTo('contact-company')">{{ t('ribbonCompany') }}</a>
      <a href="#contact-message" @click.prevent="scrollTo('contact-message')">{{ t('ribbonMessage') }}</a>
    </nav>

    <section id="contact-support" class="portal-section support-block">
      <h2 class="section-heading">{{ localizedText(settings, 'supportHeading', locale) }}</h2>
      <div class="support-grid">
        <div class="support-item">
          <el-icon :size="28"><Phone /></el-icon>
          <div>
            <h3>{{ localizedText(settings, 'presalesTitle', locale) }}</h3>
            <p>{{ settings.presalesPhone }}</p>
          </div>
        </div>
        <div class="support-item">
          <el-icon :size="28"><Message /></el-icon>
          <div>
            <h3>{{ t('contactEmail') }}</h3>
            <p>{{ settings.email }}</p>
          </div>
        </div>
      </div>
    </section>

    <section id="contact-company" class="portal-section company-block">
      <h2 class="section-heading">{{ t('contactMethods') }}</h2>
      <div class="company-info">
        <p><el-icon><Phone /></el-icon> {{ settings.companyPhone || settings.presalesPhone }}</p>
        <p><el-icon><Message /></el-icon> {{ settings.email }}</p>
        <p v-for="(addr, idx) in displayAddresses" :key="idx">
          <el-icon><Location /></el-icon> {{ addr }}
        </p>
      </div>
    </section>

    <section id="contact-message" class="portal-section forms-wrap">
      <div id="contact-sales" class="contact-grid">
        <section class="panel">
          <h3>{{ t('onlineMsg') }}</h3>
          <p v-if="!auth.isLoggedIn" class="panel-hint login-hint">
            {{ t('msgNeedLogin') }}
            <router-link :to="{ path: '/login', query: { redirect: '/portal/contact', redirectHash: 'contact-message' } }">{{ t('login') }}</router-link>
            /
            <router-link :to="{ path: '/register', query: { redirect: '/portal/contact', redirectHash: 'contact-message' } }">{{ t('registerTitle') }}</router-link>
          </p>
          <el-form
            :model="form"
            :label-position="isMobile ? 'top' : 'right'"
            :label-width="isMobile ? undefined : '80px'"
          >
            <el-form-item :label="t('name')" required>
              <el-input v-model="form.name" :disabled="!auth.isLoggedIn" />
            </el-form-item>
            <el-form-item :label="t('phone')" required>
              <el-input v-model="form.phone" :disabled="!auth.isLoggedIn" />
            </el-form-item>
            <el-form-item :label="t('message')" required>
              <el-input v-model="form.content" type="textarea" :rows="4" :disabled="!auth.isLoggedIn" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="submitting" :disabled="!auth.isLoggedIn" @click="submitFeedback">
                {{ t('submitMsg') }}
              </el-button>
            </el-form-item>
          </el-form>
        </section>

        <section class="panel">
          <h3>{{ t('consultTicket') }}</h3>
          <p class="panel-hint">{{ t('consultHint') }}</p>
          <el-form
            :model="consultForm"
            :label-position="isMobile ? 'top' : 'right'"
            :label-width="isMobile ? undefined : '80px'"
          >
            <el-form-item :label="t('name')" required>
              <el-input v-model="consultForm.name" />
            </el-form-item>
            <el-form-item :label="t('phone')" required>
              <el-input v-model="consultForm.phone" />
            </el-form-item>
            <el-form-item :label="t('title')" required>
              <el-input v-model="consultForm.title" />
            </el-form-item>
            <el-form-item :label="t('content')" required>
              <el-input v-model="consultForm.content" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="consulting" @click="submitConsult">
                {{ t('submitConsult') }}
              </el-button>
            </el-form-item>
          </el-form>
        </section>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Phone, Message, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { feedbackApi, consultationApi, contactSettingsApi } from '@/api'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'
import { useAuthStore } from '@/stores/auth'

const { t, locale } = useI18n()
const auth = useAuthStore()
const router = useRouter()
const submitting = ref(false)
const consulting = ref(false)
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)

const settings = reactive({
  talkNow: '即刻对话',
  talkNowTw: '',
  talkNowEn: '',
  presalesTitle: '售前人工客服',
  presalesDesc: '售前咨询、方案选型与商务对接，工作日人工客服在线响应。',
  presalesPhone: '400-888-0000',
  presalesBtn: '售前人工客服',
  presalesBtnLink: 'tel:400-888-0000',
  aftersalesTitle: '售后技术支持',
  aftersalesDesc: '已购产品的技术支持、故障排查与运维协助，智能与人工协同服务。',
  aftersalesPhone: '400-888-0001',
  aftersalesBtn: '售后技术支持',
  aftersalesBtnLink: 'tel:400-888-0001',
  supportHeading: '获取产品和服务支持',
  email: 'contact@liquicool.com',
  address: '北京市海淀区科技园区',
  addresses: [
    { text: '北京市海淀区科技园区', textTw: '北京市海淀區科技園區', textEn: 'Haidian Science Park, Beijing' }
  ],
  companyPhone: '400-888-0000'
})

const displayAddresses = computed(() => {
  const list = Array.isArray(settings.addresses) ? settings.addresses : []
  const mapped = list
    .map((item) => localizedText(item, 'text', locale.value))
    .filter((s) => !!s)
  if (mapped.length) return mapped
  const legacy = localizedText(settings, 'address', locale.value)
  return legacy ? [legacy] : []
})

const form = reactive({ name: '', phone: '', content: '' })
const consultForm = reactive({ name: '', phone: '', title: '', content: '' })

function onResize() {
  viewportWidth.value = window.innerWidth
}

function scrollTo(id) {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

/** 按钮链接：支持 tel: / mailto: / http(s):；为空时回退到电话 */
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

function isExternalHref(link) {
  const href = resolveActionHref(link, '')
  return /^https?:/i.test(href)
}

/** 仅做提示，绝不 preventDefault，否则会打断系统打开 tel:/mailto: */
function onServiceBtnClick(_e, link, phoneFallback) {
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

async function loadSettings() {
  try {
    const res = await contactSettingsApi.portal()
    Object.assign(settings, res.data || res || {})
  } catch { /* keep defaults */ }
}

async function submitFeedback() {
  if (!auth.isLoggedIn) {
    ElMessage.warning(t('pleaseLogin'))
    router.push({ path: '/login', query: { redirect: '/portal/contact', redirectHash: 'contact-message' } })
    return
  }
  if (!form.name.trim() || !form.phone.trim() || !form.content.trim()) {
    ElMessage.warning(t('fillRequired'))
    return
  }
  submitting.value = true
  try {
    await feedbackApi.create({
      contactName: form.name.trim(),
      contact: form.phone.trim(),
      content: form.content.trim()
    })
    ElMessage.success(t('msgSubmitted'))
    form.name = ''
    form.phone = ''
    form.content = ''
  } catch { /* handled */ } finally {
    submitting.value = false
  }
}

async function submitConsult() {
  if (!consultForm.name.trim() || !consultForm.phone.trim() || !consultForm.title.trim() || !consultForm.content.trim()) {
    ElMessage.warning(t('fillRequired'))
    return
  }
  consulting.value = true
  try {
    await consultationApi.create({
      needType: consultForm.title.trim(),
      content: consultForm.content.trim(),
      contactName: consultForm.name.trim(),
      phone: consultForm.phone.trim()
    })
    ElMessage.success(t('consultSubmitted'))
    consultForm.name = ''
    consultForm.phone = ''
    consultForm.title = ''
    consultForm.content = ''
  } catch { /* handled */ } finally {
    consulting.value = false
  }
}

onMounted(() => {
  onResize()
  window.addEventListener('resize', onResize)
  loadSettings()
  if (auth.isLoggedIn && auth.user) {
    if (!form.name) form.name = auth.user.nickname || auth.user.username || ''
    if (!form.phone) form.phone = auth.user.phone || ''
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.contact-top {
  padding-bottom: 8px;
}
.talk-title {
  margin: 8px 0 28px;
  font-size: 22px;
  color: #101820;
  font-weight: 700;
}
.service-list {
  display: flex;
  flex-direction: column;
  gap: 0;
  border-top: 1px solid #d5dae0;
}
.service-row {
  display: grid;
  grid-template-columns: 72px 1fr auto;
  gap: 20px;
  align-items: center;
  padding: 28px 0;
  border-bottom: 1px solid #d5dae0;
}
.service-body h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #101820;
}
.service-body p {
  margin: 0 0 10px;
  color: #5c6570;
  line-height: 1.6;
  max-width: 640px;
}
.service-phone {
  display: block;
  font-size: 20px;
  color: #101820;
  letter-spacing: 0.5px;
}
.service-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 160px;
  height: 44px;
  padding: 0 20px;
  background: #101820;
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  white-space: nowrap;
}
.service-btn:hover {
  background: #0a4fb8;
}
.contact-ribbon {
  background: #3a4048;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  width: 100%;
}
.contact-ribbon a {
  color: #fff;
  text-align: center;
  padding: 16px 8px;
  text-decoration: none;
  font-size: 15px;
  border-right: 1px solid rgba(255, 255, 255, 0.12);
}
.contact-ribbon a:last-child {
  border-right: none;
}
.contact-ribbon a:hover {
  background: #0a4fb8;
}
.section-heading {
  margin: 0 0 20px;
  font-size: 22px;
  color: #101820;
}
.support-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}
.support-item {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  padding: 20px;
  border: 1px solid #d5dae0;
  background: #fff;
}
.support-item h3 {
  margin: 0 0 6px;
  font-size: 16px;
}
.support-item p {
  margin: 0;
  color: #5c6570;
}
.company-info p {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin: 0;
  line-height: 2.2;
  color: #5c6570;
}
.company-info p .el-icon {
  margin-top: 0.55em;
  flex-shrink: 0;
}
.forms-wrap {
  padding-top: 8px;
}
.contact-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0;
  border: 1px solid #d5dae0;
}
.panel {
  padding: 28px 24px;
  background: #fff;
}
.panel + .panel {
  border-left: 1px solid #d5dae0;
}
.panel h3 {
  margin: 0 0 8px;
  color: #101820;
  font-size: 18px;
}
.panel-hint {
  margin: 0 0 18px;
  color: #8a929c;
  font-size: 13px;
}
.login-hint a {
  color: #0a4fb8;
  text-decoration: none;
}
.login-hint a:hover {
  text-decoration: underline;
}
@media (max-width: 900px) {
  .service-row {
    grid-template-columns: 56px 1fr;
  }
  .service-btn {
    grid-column: 2;
    justify-self: start;
  }
  .contact-ribbon {
    grid-template-columns: 1fr 1fr;
  }
  .contact-ribbon a {
    border-bottom: 1px solid rgba(255, 255, 255, 0.12);
  }
  .support-grid,
  .contact-grid {
    grid-template-columns: 1fr;
  }
  .panel + .panel {
    border-left: none;
    border-top: 1px solid #d5dae0;
  }
}
</style>
