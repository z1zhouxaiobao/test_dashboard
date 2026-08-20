<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('profileTitle') }}</h1>
    <el-tabs v-model="activeTab" class="profile-tabs">
      <el-tab-pane label="基本资料" name="profile">
        <el-card>
          <el-form
            :model="profileForm"
            :label-position="isMobile ? 'top' : 'right'"
            :label-width="isMobile ? undefined : '100px'"
            class="profile-form"
          >
            <el-form-item label="头像">
              <ImageUpload v-model="profileForm.avatar" />
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            <el-form-item label="手机">
              <el-input v-model="profileForm.phone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="saveProfile">保存资料</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="咨询订单" name="consultations">
        <div class="table-scroll">
          <el-table :data="consultations" border align="center" header-align="center" style="min-width: 520px">
            <el-table-column prop="needType" label="需求类型" align="center" header-align="center" />
            <el-table-column prop="company" label="公司" align="center" header-align="center" />
            <el-table-column prop="status" label="状态" align="center" header-align="center" />
            <el-table-column label="提交时间" align="center" header-align="center" min-width="140">
              <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
            </el-table-column>
          </el-table>
        </div>
        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="consultPage"
            :total="consultTotal"
            :layout="pagerLayout"
            @current-change="loadConsultations"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { consultationApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import ImageUpload from '@/components/ImageUpload.vue'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const auth = useAuthStore()
const activeTab = ref('profile')
const saving = ref(false)
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200)
const isMobile = computed(() => viewportWidth.value <= 768)
const pagerLayout = computed(() => (isMobile.value ? 'prev, pager, next' : 'total, prev, pager, next'))

const profileForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

const consultations = ref([])
const consultPage = ref(1)
const consultTotal = ref(0)

function onResize() {
  viewportWidth.value = window.innerWidth
}

function initProfile() {
  if (auth.user) {
    profileForm.username = auth.user.username || ''
    profileForm.nickname = auth.user.nickname || ''
    profileForm.email = auth.user.email || ''
    profileForm.phone = auth.user.phone || ''
    profileForm.avatar = auth.user.avatar || ''
  }
}

async function saveProfile() {
  saving.value = true
  try {
    await auth.updateProfile({ ...profileForm })
    ElMessage.success('资料已更新')
  } catch { /* handled */ } finally {
    saving.value = false
  }
}

async function loadConsultations() {
  try {
    const res = await consultationApi.myPage({ page: consultPage.value, size: 10 })
    consultations.value = res.data?.records || res.records || []
    consultTotal.value = res.data?.total || res.total || 0
  } catch {
    consultations.value = []
  }
}

watch(activeTab, (tab) => {
  if (tab === 'consultations') loadConsultations()
})

onMounted(() => {
  onResize()
  window.addEventListener('resize', onResize)
  initProfile()
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})
</script>

<style scoped>
.profile-form {
  max-width: 500px;
  width: 100%;
}

@media (max-width: 768px) {
  .profile-form {
    max-width: 100%;
  }
}
</style>
