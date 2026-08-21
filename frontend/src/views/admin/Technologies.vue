<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminTech') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phTech')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addTech') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column :label="t('image')" width="120" align="center" header-align="center">
        <template #default="{ row }">
          <PreviewImage :src="resolveMediaUrl(row.coverUrl)" />
        </template>
      </el-table-column>
      <el-table-column prop="title" :label="t('itemName')" align="center" header-align="center" />
      <el-table-column prop="sortOrder" :label="t('sortOrder')" width="80" align="center" header-align="center" />
      <el-table-column :label="t('createdAt')" align="center" header-align="center">
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

    <el-dialog v-model="dialogVisible" :title="form.id ? t('editTech') : t('addTech')" width="860px" top="5vh">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="t('itemName')"><el-input v-model="form.title" /></el-form-item>
        <el-form-item :label="t('summary')"><el-input v-model="form.summary" type="textarea" :rows="3" /></el-form-item>
        <el-form-item :label="t('detailContent')"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item :label="t('image')"><ImageUpload v-model="form.coverUrl" /></el-form-item>
        <el-form-item :label="t('sortOrder')"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <I18nCollapse :model="form" :fields="[{ base: 'title' }, { base: 'summary', type: 'textarea', rows: 2 }, { base: 'content', type: 'textarea', rows: 3 }]" />
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
import { technologyApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl } from '@/utils/media'
import TableToolbar from '@/components/TableToolbar.vue'
import ImageUpload from '@/components/ImageUpload.vue'
import PreviewImage from '@/components/PreviewImage.vue'
import I18nCollapse from '@/components/I18nCollapse.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'

const { t } = useI18n()

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const form = reactive({
  id: null, title: '', titleTw: '', titleEn: '', summary: '', summaryTw: '', summaryEn: '',
  content: '', contentTw: '', contentEn: '', coverUrl: '', sortOrder: 0
})

async function loadData() {
  loading.value = true
  try {
    const res = await technologyApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => technologyApi.remove(id),
  reload: loadData
})
function openDialog(row) {
  Object.assign(form, {
    id: null, title: '', titleTw: '', titleEn: '', summary: '', summaryTw: '', summaryEn: '',
    content: '', contentTw: '', contentEn: '', coverUrl: '', sortOrder: 0
  })
  if (row) Object.assign(form, row)
  dialogVisible.value = true
}
async function handleSave() {
  saving.value = true
  try {
    if (form.id) await technologyApi.update(form.id, form)
    else await technologyApi.create(form)
    ElMessage.success(t('saveSuccess'))
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}
onMounted(loadData)
</script>
