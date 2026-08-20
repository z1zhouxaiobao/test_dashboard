<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('contactTitle') }}</h1>
    <div class="contact-grid">
      <section class="panel">
        <h3>{{ t('contactMethods') }}</h3>
        <p><el-icon><Phone /></el-icon> 400-888-0000</p>
        <p><el-icon><Message /></el-icon> contact@liquicool.com</p>
        <p><el-icon><Location /></el-icon> {{ t('address') }}</p>
        <h3 class="sub">{{ t('onlineMsg') }}</h3>
        <el-form :model="form" :label-position="isMobile ? 'top' : 'right'" :label-width="isMobile ? undefined : '80px'">
          <el-form-item :label="t('name')">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item :label="t('phone')">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item :label="t('message')">
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
        <el-form :model="consultForm" :label-position="isMobile ? 'top' : 'right'" :label-width="isMobile ? undefined : '80px'">
          <el-form-item :label="t('title')">
            <el-input v-model="consultForm.title" />
          </el-form-item>
          <el-form-item :label="t('content')">
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
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Phone, Message, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { feedbackApi, consultationApi } from '@/api'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const router = useRouter()
const auth = useAuthStore()
const submitting = ref(false)
const consulting = ref(false)
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)

function onResize() {
  viewportWidth.value = window.innerWidth
}

const form = reactive({ name: '', phone: '', content: '' })
const consultForm = reactive({ title: '', content: '' })

function requireLogin() {
  if (!auth.isLoggedIn) {
    ElMessage.warning(t('pleaseLogin'))
    router.push('/login')
    return false
  }
  return true
}

async function submitFeedback() {
  if (!requireLogin()) return
  submitting.value = true
  try {
    await feedbackApi.create({
      content: `【${form.name}】${form.phone}\n${form.content}`,
      contact: form.phone
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
  if (!requireLogin()) return
  consulting.value = true
  try {
    await consultationApi.create({
      needType: consultForm.title,
      content: consultForm.content,
      contactName: auth.displayName,
      phone: auth.user?.phone || ''
    })
    ElMessage.success(t('consultSubmitted'))
    consultForm.title = ''
    consultForm.content = ''
  } catch { /* handled */ } finally {
    consulting.value = false
  }
}

onMounted(() => {
  onResize()
  window.addEventListener('resize', onResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
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
  margin: 0 0 16px;
  color: #101820;
  font-size: 18px;
}
.panel h3.sub {
  margin-top: 28px;
}
.panel p {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #5c6570;
  line-height: 2;
  margin: 0;
  flex-wrap: wrap;
}
@media (max-width: 768px) {
  .contact-grid {
    grid-template-columns: 1fr;
  }
  .panel + .panel {
    border-left: none;
    border-top: 1px solid #d5dae0;
  }
}
</style>
