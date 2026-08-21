<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminProfile') }}</h2>
    <p class="page-hint">{{ t('adminProfileHint') }}</p>

    <el-form
      v-loading="loading"
      :model="form"
      label-width="100px"
      class="profile-form"
      @submit.prevent
    >
      <el-form-item :label="t('avatar')">
        <ImageUpload v-model="form.avatar" />
      </el-form-item>
      <el-form-item :label="t('username')">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item :label="t('nickname')">
        <el-input v-model="form.nickname" />
      </el-form-item>
      <el-form-item :label="t('email')">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item :label="t('mobile')">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="handleSave">{{ t('save') }}</el-button>
        <el-button @click="initForm">{{ t('refresh') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api'
import ImageUpload from '@/components/ImageUpload.vue'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const auth = useAuthStore()
const loading = ref(false)
const saving = ref(false)
const form = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

function initForm() {
  const u = auth.user || {}
  form.username = u.username || ''
  form.nickname = u.nickname || ''
  form.email = u.email || ''
  form.phone = u.phone || ''
  form.avatar = u.avatar || ''
}

async function handleSave() {
  saving.value = true
  try {
    await authApi.updateProfile({
      nickname: form.nickname,
      email: form.email,
      phone: form.phone
    })
    if (form.avatar !== (auth.user?.avatar || '')) {
      await authApi.updateAvatar(form.avatar)
    }
    await auth.fetchMe()
    initForm()
    ElMessage.success(t('saveSuccess') || t('saveOk'))
  } catch { /* handled */ } finally {
    saving.value = false
  }
}

onMounted(async () => {
  loading.value = true
  try {
    await auth.fetchMe()
    initForm()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page-hint {
  margin: -8px 0 20px;
  color: #8a929c;
  font-size: 13px;
}
.profile-form {
  max-width: 520px;
}
</style>
