<template>
  <div class="contact-page">
    <section class="portal-section contact-top">
      <h1 class="page-title">{{ t('contactTitle') }}</h1>
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
          <a class="service-btn" :href="`tel:${settings.presalesPhone}`">{{ localizedText(settings, 'presalesBtn', locale) }}</a>
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
          <a class="service-btn" :href="`tel:${settings.aftersalesPhone}`">{{ localizedText(settings, 'aftersalesBtn', locale) }}</a>
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
        <p><el-icon><Location /></el-icon> {{ localizedText(settings, 'address', locale) }}</p>
      </div>
    </section>

    <section id="contact-message" class="portal-section forms-wrap">
      <div id="contact-sales" class="contact-grid">
        <section class="panel">
          <h3>{{ t('onlineMsg') }}</h3>
          <el-form
            :model="form"
            :label-position="isMobile ? 'top' : 'right'"
            :label-width="isMobile ? undefined : '80px'"
          >
            <el-form-item :label="t('name')" required>
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item :label="t('phone')" required>
              <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item :label="t('message')" required>
              <el-input v-model="form.content" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="submitting" @click="submitFeedback">
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
import { Phone, Message, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { feedbackApi, consultationApi, contactSettingsApi } from '@/api'
import { localizedText } from '@/utils/localized'
import { useI18n } from '@/composables/useI18n'

const { t, locale } = useI18n()
const submitting = ref(false)
const consulting = ref(false)
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)

const settings = reactive({
  talkNow: '即刻对话',
  presalesTitle: '售前人工客服',
  presalesDesc: '售前咨询、方案选型与商务对接，工作日人工客服在线响应。',
  presalesPhone: '400-888-0000',
  presalesBtn: '售前人工客服',
  aftersalesTitle: '售后技术支持',
  aftersalesDesc: '已购产品的技术支持、故障排查与运维协助，智能与人工协同服务。',
  aftersalesPhone: '400-888-0001',
  aftersalesBtn: '售后技术支持',
  supportHeading: '获取产品和服务支持',
  email: 'contact@liquicool.com',
  address: '北京市海淀区科技园区',
  companyPhone: '400-888-0000'
})

const form = reactive({ name: '', phone: '', content: '' })
const consultForm = reactive({ name: '', phone: '', title: '', content: '' })

function onResize() {
  viewportWidth.value = window.innerWidth
}

function scrollTo(id) {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

async function loadSettings() {
  try {
    const res = await contactSettingsApi.portal()
    Object.assign(settings, res.data || res || {})
  } catch { /* keep defaults */ }
}

async function submitFeedback() {
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
  align-items: center;
  gap: 8px;
  margin: 0;
  line-height: 2.2;
  color: #5c6570;
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
