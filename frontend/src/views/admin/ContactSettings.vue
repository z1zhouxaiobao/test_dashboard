<template>
  <div class="contact-settings">
    <header class="cs-hero">
      <div class="cs-hero-text">
        <h2 class="cs-title">{{ t('adminContactSettings') }}</h2>
      </div>
      <div class="cs-hero-actions">
        <el-button @click="loadData">{{ t('refresh') }}</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">{{ t('save') }}</el-button>
      </div>
    </header>

    <el-form
      v-loading="loading"
      :model="form"
      label-width="100px"
      class="cs-form"
      @submit.prevent
    >
      <section class="cs-panel">
        <div class="cs-panel-head">
          <span class="cs-panel-index">01</span>
          <div>
            <h3 class="cs-panel-title">{{ t('talkNow') }}</h3>
            <p class="cs-panel-sub">{{ t('cfgTalkNowHint') }}</p>
          </div>
        </div>
        <el-form-item :label="t('talkNow')">
          <el-input v-model="form.talkNow" />
        </el-form-item>
      </section>

      <section class="cs-panel">
        <div class="cs-panel-head">
          <span class="cs-panel-index">02</span>
          <div>
            <h3 class="cs-panel-title">{{ t('presalesTitle') }}</h3>
            <p class="cs-panel-sub">{{ t('cfgPresalesHint') }}</p>
          </div>
        </div>
        <div class="cs-grid">
          <el-form-item :label="t('cfgTitle')">
            <el-input v-model="form.presalesTitle" />
          </el-form-item>
          <el-form-item :label="t('phone')">
            <el-input v-model="form.presalesPhone" />
          </el-form-item>
        </div>
        <el-form-item :label="t('cfgDesc')">
          <el-input v-model="form.presalesDesc" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item :label="t('cfgBtnText')">
          <el-input v-model="form.presalesBtn" />
        </el-form-item>
      </section>

      <section class="cs-panel">
        <div class="cs-panel-head">
          <span class="cs-panel-index">03</span>
          <div>
            <h3 class="cs-panel-title">{{ t('aftersalesTitle') }}</h3>
            <p class="cs-panel-sub">{{ t('cfgAftersalesHint') }}</p>
          </div>
        </div>
        <div class="cs-grid">
          <el-form-item :label="t('cfgTitle')">
            <el-input v-model="form.aftersalesTitle" />
          </el-form-item>
          <el-form-item :label="t('phone')">
            <el-input v-model="form.aftersalesPhone" />
          </el-form-item>
        </div>
        <el-form-item :label="t('cfgDesc')">
          <el-input v-model="form.aftersalesDesc" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item :label="t('cfgBtnText')">
          <el-input v-model="form.aftersalesBtn" />
        </el-form-item>
      </section>

      <section class="cs-panel">
        <div class="cs-panel-head">
          <span class="cs-panel-index">04</span>
          <div>
            <h3 class="cs-panel-title">{{ t('cfgSupportBlock') }}</h3>
            <p class="cs-panel-sub">{{ t('cfgSupportHint') }}</p>
          </div>
        </div>
        <el-form-item :label="t('cfgSupportHeading')">
          <el-input v-model="form.supportHeading" />
        </el-form-item>
        <div class="cs-grid">
          <el-form-item :label="t('contactEmail')">
            <el-input v-model="form.email" />
          </el-form-item>
          <el-form-item :label="t('cfgCompanyPhone')">
            <el-input v-model="form.companyPhone" />
          </el-form-item>
        </div>
      </section>

      <section class="cs-panel">
        <div class="cs-panel-head">
          <span class="cs-panel-index">05</span>
          <div>
            <h3 class="cs-panel-title">{{ t('cfgAddresses') }}</h3>
            <p class="cs-panel-sub">{{ t('cfgAddressesHint') }}</p>
          </div>
        </div>

        <div v-for="(item, index) in form.addresses" :key="index" class="addr-card">
          <div class="addr-card-head">
            <span class="addr-badge">{{ t('cfgAddress') }} {{ index + 1 }}</span>
            <el-button
              type="danger"
              link
              :disabled="form.addresses.length <= 1"
              @click="removeAddress(index)"
            >
              {{ t('delete') }}
            </el-button>
          </div>
          <el-form-item :label="t('cfgAddressZh')">
            <el-input v-model="item.text" :placeholder="t('cfgAddressZhPh')" />
          </el-form-item>
          <div class="cs-grid">
            <el-form-item :label="t('cfgAddressTw')">
              <el-input v-model="item.textTw" :placeholder="t('i18nFallbackZh')" />
            </el-form-item>
            <el-form-item :label="t('cfgAddressEn')">
              <el-input v-model="item.textEn" :placeholder="t('i18nFallbackZh')" />
            </el-form-item>
          </div>
        </div>

        <el-button class="add-addr-btn" type="primary" plain @click="addAddress">
          {{ t('cfgAddAddress') }}
        </el-button>
      </section>

      <section class="cs-panel cs-panel-i18n">
        <I18nCollapse
          :model="form"
          :fields="[
            { base: 'talkNow', labelKey: 'talkNow' },
            { base: 'presalesTitle', labelKey: 'presalesTitle' },
            { base: 'presalesDesc', type: 'textarea', rows: 2, labelKey: 'cfgDesc' },
            { base: 'presalesBtn', labelKey: 'cfgBtnText' },
            { base: 'aftersalesTitle', labelKey: 'aftersalesTitle' },
            { base: 'aftersalesDesc', type: 'textarea', rows: 2, labelKey: 'cfgDesc' },
            { base: 'aftersalesBtn', labelKey: 'cfgBtnText' },
            { base: 'supportHeading', labelKey: 'cfgSupportHeading' }
          ]"
        />
      </section>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { contactSettingsApi } from '@/api'
import I18nCollapse from '@/components/I18nCollapse.vue'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()
const loading = ref(false)
const saving = ref(false)

function emptyAddress() {
  return { text: '', textTw: '', textEn: '' }
}

const form = reactive({
  talkNow: '', talkNowTw: '', talkNowEn: '',
  presalesTitle: '', presalesTitleTw: '', presalesTitleEn: '',
  presalesDesc: '', presalesDescTw: '', presalesDescEn: '',
  presalesPhone: '',
  presalesBtn: '', presalesBtnTw: '', presalesBtnEn: '',
  aftersalesTitle: '', aftersalesTitleTw: '', aftersalesTitleEn: '',
  aftersalesDesc: '', aftersalesDescTw: '', aftersalesDescEn: '',
  aftersalesPhone: '',
  aftersalesBtn: '', aftersalesBtnTw: '', aftersalesBtnEn: '',
  supportHeading: '', supportHeadingTw: '', supportHeadingEn: '',
  email: '',
  companyPhone: '',
  addresses: [emptyAddress()]
})

function addAddress() {
  form.addresses.push(emptyAddress())
}

function removeAddress(index) {
  if (form.addresses.length <= 1) return
  form.addresses.splice(index, 1)
}

function normalizeLoaded(data) {
  Object.assign(form, data || {})
  let list = Array.isArray(data?.addresses) ? data.addresses : []
  if (!list.length && data?.address) {
    list = [{ text: data.address || '', textTw: data.addressTw || '', textEn: data.addressEn || '' }]
  }
  form.addresses = list.length
    ? list.map((a) => ({
        text: a.text || a.address || '',
        textTw: a.textTw || '',
        textEn: a.textEn || ''
      }))
    : [emptyAddress()]
}

async function loadData() {
  loading.value = true
  try {
    const res = await contactSettingsApi.get()
    normalizeLoaded(res.data || res || {})
  } catch { /* handled */ } finally {
    loading.value = false
  }
}

async function handleSave() {
  saving.value = true
  try {
    const payload = {
      ...form,
      addresses: form.addresses.filter((a) => (a.text || '').trim())
    }
    const res = await contactSettingsApi.save(payload)
    normalizeLoaded(res.data || res || form)
    ElMessage.success(t('saveOk') || '已保存')
  } catch { /* handled */ } finally {
    saving.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.contact-settings {
  width: 100%;
  max-width: none;
}

.cs-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin: 0 0 16px;
  padding: 14px 18px;
  border: 1px solid var(--liquicool-line);
  border-radius: 10px;
  background: #fff;
}

.cs-title {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--liquicool-header);
  letter-spacing: 0.02em;
}

.cs-hero-actions {
  display: flex;
  flex-shrink: 0;
  gap: 8px;
}

.cs-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.cs-panel {
  padding: 18px 20px 8px;
  border: 1px solid var(--liquicool-line);
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 1px 0 rgba(16, 24, 32, 0.03);
}

.cs-panel-head {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eef1f4;
}

.cs-panel-index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #eef4fc;
  color: var(--liquicool-primary);
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.04em;
}

.cs-panel-title {
  margin: 0;
  font-size: 16px;
  font-weight: 650;
  color: var(--liquicool-header);
}

.cs-panel-sub {
  margin: 4px 0 0;
  color: #8a929c;
  font-size: 12px;
  line-height: 1.45;
}

.cs-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 12px;
}

.cs-panel-i18n {
  padding-bottom: 16px;
}

.addr-card {
  margin-bottom: 12px;
  padding: 12px 14px 2px;
  border: 1px solid #e4ebf4;
  border-radius: 10px;
  background: linear-gradient(180deg, #f7f9fc 0%, #fff 40%);
}

.addr-card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.addr-badge {
  display: inline-flex;
  align-items: center;
  padding: 2px 10px;
  border-radius: 999px;
  background: #eef4fc;
  color: var(--liquicool-primary);
  font-size: 12px;
  font-weight: 600;
}

.add-addr-btn {
  margin: 4px 0 12px;
}

@media (max-width: 768px) {
  .cs-hero {
    flex-direction: column;
    align-items: stretch;
  }

  .cs-hero-actions {
    width: 100%;
  }

  .cs-hero-actions .el-button {
    flex: 1;
  }

  .cs-grid {
    grid-template-columns: 1fr;
  }
}
</style>
