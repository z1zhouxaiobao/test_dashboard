<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminConsultations') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phTitleUser')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column prop="title" :label="t('title')" align="center" header-align="center" show-overflow-tooltip />
      <el-table-column prop="username" :label="t('userCol')" align="center" header-align="center" />
      <el-table-column :label="t('status')" width="100" align="center" header-align="center">
        <template #default="{ row }">{{ tv(row.status) }}</template>
      </el-table-column>
      <el-table-column :label="t('submittedAt')" align="center" header-align="center">
        <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column :label="t('actions')" width="160" align="center" header-align="center">
        <template #default="{ row }">
          <el-button type="primary" link @click="openDialog(row)">{{ t('process') }}</el-button>
          <el-button type="primary" link @click="handleDelete(row.id)">{{ t('delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, sizes, prev, pager, next" @current-change="loadData" @size-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="t('processConsult')" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="t('title')"><el-input v-model="form.title" disabled /></el-form-item>
        <el-form-item :label="t('content')"><el-input v-model="form.content" type="textarea" :rows="4" disabled /></el-form-item>
        <el-form-item :label="t('status')">
          <el-select v-model="form.status" style="width:100%">
            <el-option :label="t('statusPending')" value="待处理" />
            <el-option :label="t('statusProcessing')" value="处理中" />
            <el-option :label="t('statusDone')" value="已完成" />
            <el-option :label="t('statusClosed')" value="已关闭" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('reply')"><el-input v-model="form.reply" type="textarea" :rows="3" /></el-form-item>
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
import { consultationApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import TableToolbar from '@/components/TableToolbar.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'

const { t, tv } = useI18n()

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '', status: '待处理', reply: '' })

async function loadData() {
  loading.value = true
  try {
    const res = await consultationApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => consultationApi.remove(id),
  reload: loadData
})
function openDialog(row) {
  Object.assign(form, row)
  dialogVisible.value = true
}
async function handleSave() {
  saving.value = true
  try {
    await consultationApi.update(form.id, { status: form.status, reply: form.reply })
    ElMessage.success(t('saveSuccess'))
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}
onMounted(loadData)
</script>
