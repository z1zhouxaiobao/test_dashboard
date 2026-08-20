<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminContactSettings') }}</h2>
    <p class="hint">{{ t('adminContactSettingsHint') }}</p>

    <el-form
      v-loading="loading"
      :model="form"
      label-width="120px"
      class="settings-form"
      @submit.prevent
    >
      <h3 class="block-title">{{ t('talkNow') }}</h3>
      <el-form-item :label="t('talkNow')">
        <el-input v-model="form.talkNow" />
      </el-form-item>

      <h3 class="block-title">{{ t('presalesTitle') }}</h3>
      <el-form-item :label="t('cfgTitle')">
        <el-input v-model="form.presalesTitle" />
      </el-form-item>
      <el-form-item :label="t('cfgDesc')">
        <el-input v-model="form.presalesDesc" type="textarea" :rows="2" />
      </el-form-item>
      <el-form-item :label="t('phone')">
        <el-input v-model="form.presalesPhone" />
      </el-form-item>
      <el-form-item :label="t('cfgBtnText')">
        <el-input v-model="form.presalesBtn" />
      </el-form-item>

      <h3 class="block-title">{{ t('aftersalesTitle') }}</h3>
      <el-form-item :label="t('cfgTitle')">
        <el-input v-model="form.aftersalesTitle" />
      </el-form-item>
      <el-form-item :label="t('cfgDesc')">
        <el-input v-model="form.aftersalesDesc" type="textarea" :rows="2" />
      </el-form-item>
      <el-form-item :label="t('phone')">
        <el-input v-model="form.aftersalesPhone" />
      </el-form-item>
      <el-form-item :label="t('cfgBtnText')">
        <el-input v-model="form.aftersalesBtn" />
      </el-form-item>

      <h3 class="block-title">{{ t('supportHeading') }}</h3>
      <el-form-item :label="t('supportHeading')">
        <el-input v-model="form.supportHeading" />
      </el-form-item>
      <el-form-item :label="t('contactEmail')">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item :label="t('cfgCompanyPhone')">
        <el-input v-model="form.companyPhone" />
      </el-form-item>
      <el-form-item :label="t('address')">
        <el-input v-model="form.address" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="saving" @click="handleSave">{{ t('save') }}</el-button>
        <el-button @click="loadData">{{ t('refresh') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { contactSettingsApi } from '@/api'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const loading = ref(false)
const saving = ref(false)
const form = reactive({
  talkNow: '',
  presalesTitle: '',
  presalesDesc: '',
  presalesPhone: '',
  presalesBtn: '',
  aftersalesTitle: '',
  aftersalesDesc: '',
  aftersalesPhone: '',
  aftersalesBtn: '',
  supportHeading: '',
  email: '',
  address: '',
  companyPhone: ''
})

async function loadData() {
  loading.value = true
  try {
    const res = await contactSettingsApi.get()
    Object.assign(form, res.data || res || {})
  } catch { /* handled */ } finally {
    loading.value = false
  }
}

async function handleSave() {
  saving.value = true
  try {
    const res = await contactSettingsApi.save({ ...form })
    Object.assign(form, res.data || res || form)
    ElMessage.success(t('saveOk') || '已保存')
  } catch { /* handled */ } finally {
    saving.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.hint {
  margin: -8px 0 20px;
  color: #8a929c;
  font-size: 13px;
}
.settings-form {
  max-width: 720px;
}
.block-title {
  margin: 8px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e6e9ee;
  font-size: 16px;
  color: #101820;
}
</style>
