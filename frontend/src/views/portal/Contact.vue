<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('contactTitle') }}</h1>
    <el-row :gutter="40">
      <el-col :xs="24" :md="12">
        <el-card class="contact-info">
          <h3>{{ t('contactMethods') }}</h3>
          <p><el-icon><Phone /></el-icon> 400-888-0000</p>
          <p><el-icon><Message /></el-icon> contact@liquicool.com</p>
          <p><el-icon><Location /></el-icon> {{ t('address') }}</p>
          <h3 style="margin-top: 24px">{{ t('onlineMsg') }}</h3>
          <el-form :model="form" label-width="80px">
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
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card>
          <h3>{{ t('consultTicket') }}</h3>
          <el-form :model="consultForm" label-width="80px">
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
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
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
</script>

<style scoped>
.contact-info p {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #444;
  line-height: 2;
}
.contact-info h3 {
  color: #0B5ED7;
}
</style>
