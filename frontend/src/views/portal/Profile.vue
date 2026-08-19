<template>
  <div class="portal-section">
    <h1 class="page-title">{{ t('profileTitle') }}</h1>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本资料" name="profile">
        <el-card>
          <el-form :model="profileForm" label-width="100px" style="max-width: 500px">
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

      <el-tab-pane label="我的收藏" name="favorites">
        <el-table :data="favorites" border align="center" header-align="center">
          <el-table-column prop="targetType" label="类型" align="center" header-align="center" />
          <el-table-column prop="targetName" label="名称" align="center" header-align="center" />
          <el-table-column label="收藏时间" align="center" header-align="center">
            <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
          </el-table-column>
          <el-table-column label="操作" align="center" header-align="center">
            <template #default="{ row }">
              <el-button type="primary" link @click="removeFavorite(row)">取消收藏</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="favPage"
            :total="favTotal"
            layout="total, prev, pager, next"
            @current-change="loadFavorites"
          />
        </div>
      </el-tab-pane>

      <el-tab-pane label="咨询订单" name="consultations">
        <el-table :data="consultations" border align="center" header-align="center">
          <el-table-column prop="needType" label="需求类型" align="center" header-align="center" />
          <el-table-column prop="company" label="公司" align="center" header-align="center" />
          <el-table-column prop="status" label="状态" align="center" header-align="center" />
          <el-table-column label="提交时间" align="center" header-align="center">
            <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="consultPage"
            :total="consultTotal"
            layout="total, prev, pager, next"
            @current-change="loadConsultations"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { favoriteApi, consultationApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import ImageUpload from '@/components/ImageUpload.vue'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const auth = useAuthStore()
const activeTab = ref('profile')
const saving = ref(false)

const profileForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

const favorites = ref([])
const favPage = ref(1)
const favTotal = ref(0)
const consultations = ref([])
const consultPage = ref(1)
const consultTotal = ref(0)

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

async function loadFavorites() {
  try {
    const res = await favoriteApi.myPage({ page: favPage.value, size: 10 })
    favorites.value = res.data?.records || res.records || []
    favTotal.value = res.data?.total || res.total || 0
  } catch {
    favorites.value = []
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

async function removeFavorite(row) {
  await ElMessageBox.confirm('确定取消收藏？', '提示', { type: 'warning' })
  await favoriteApi.remove(row)
  ElMessage.success('已取消收藏')
  loadFavorites()
}

watch(activeTab, (tab) => {
  if (tab === 'favorites') loadFavorites()
  if (tab === 'consultations') loadConsultations()
})

onMounted(() => {
  initProfile()
})
</script>
