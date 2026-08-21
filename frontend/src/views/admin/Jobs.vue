<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminJobs') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phTitle')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addJob') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="70" align="center" header-align="center" />
      <el-table-column prop="title" :label="t('jobTitle')" min-width="140" align="center" header-align="center" show-overflow-tooltip />
      <el-table-column prop="location" :label="t('jobLocation')" width="120" align="center" header-align="center" show-overflow-tooltip />
      <el-table-column prop="sortOrder" :label="t('sortOrder')" width="80" align="center" header-align="center" />
      <el-table-column :label="t('published')" width="80" align="center" header-align="center">
        <template #default="{ row }">{{ (row.published === true || row.status === 1) ? t('yes') : t('no') }}</template>
      </el-table-column>
      <el-table-column :label="t('createdAt')" width="170" align="center" header-align="center">
        <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column :label="t('actions')" width="160" align="center" header-align="center">
        <template #default="{ row }">
          <el-button type="primary" link @click="openDialog(row)">{{ t('edit') }}</el-button>
          <el-button type="primary" link @click="handleDelete(row.id)">{{ t('delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, sizes, prev, pager, next" @current-change="loadData" @size-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? t('editJob') : t('addJob')" width="860px" top="4vh">
      <el-form :model="form" label-width="110px">
        <el-form-item :label="t('jobTitle')"><el-input v-model="form.title" /></el-form-item>
        <el-form-item :label="t('jobLocation')"><el-input v-model="form.location" /></el-form-item>
        <el-form-item :label="t('jobSummary')"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item :label="t('jobResponsibilities')">
          <el-input v-model="form.responsibilities" type="textarea" :rows="5" :placeholder="t('jobMultiLinePh')" />
        </el-form-item>
        <el-form-item :label="t('jobRequirements')">
          <el-input v-model="form.requirements" type="textarea" :rows="4" :placeholder="t('jobMultiLinePh')" />
        </el-form-item>
        <div class="cs-grid">
          <el-form-item :label="t('sortOrder')"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
          <el-form-item :label="t('published')"><el-switch v-model="form.published" /></el-form-item>
        </div>
        <I18nCollapse :model="form" :fields="i18nFields" />
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('cancel') }}</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">{{ t('save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { jobApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import TableToolbar from '@/components/TableToolbar.vue'
import I18nCollapse from '@/components/I18nCollapse.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'

const { t } = useI18n()

const i18nFields = [
  { base: 'title', labelKey: 'jobTitle' },
  { base: 'location', labelKey: 'jobLocation' },
  { base: 'summary', type: 'textarea', rows: 2, labelKey: 'jobSummary' },
  { base: 'responsibilities', type: 'textarea', rows: 4, labelKey: 'jobResponsibilities' },
  { base: 'requirements', type: 'textarea', rows: 3, labelKey: 'jobRequirements' }
]

const emptyForm = () => ({
  id: null,
  title: '', titleTw: '', titleEn: '',
  summary: '', summaryTw: '', summaryEn: '',
  responsibilities: '', responsibilitiesTw: '', responsibilitiesEn: '',
  requirements: '', requirementsTw: '', requirementsEn: '',
  location: '', locationTw: '', locationEn: '',
  sortOrder: 0,
  published: true
})

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const form = reactive(emptyForm())

async function loadData() {
  loading.value = true
  try {
    const res = await jobApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => jobApi.remove(id),
  reload: loadData
})
function openDialog(row) {
  Object.assign(form, emptyForm())
  if (row) {
    Object.assign(form, {
      ...row,
      published: row.published === true || row.status === 1
    })
  }
  dialogVisible.value = true
}
async function handleSave() {
  saving.value = true
  try {
    const payload = {
      ...form,
      status: form.published ? 1 : 0
    }
    if (form.id) await jobApi.update(form.id, payload)
    else await jobApi.create(payload)
    ElMessage.success(t('saveSuccess'))
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}
onMounted(loadData)
</script>

<style scoped>
.cs-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 16px;
}
</style>
