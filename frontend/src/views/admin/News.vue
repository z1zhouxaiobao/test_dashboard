<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminNews') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phTitle')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addNews') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column :label="t('cover')" width="100" align="center" header-align="center">
        <template #default="{ row }">
          <PreviewImage :src="resolveMediaUrl(row.coverUrl)" />
        </template>
      </el-table-column>
      <el-table-column prop="title" :label="t('title')" align="center" header-align="center" show-overflow-tooltip />
      <el-table-column prop="viewCount" :label="t('views')" width="90" align="center" header-align="center" />
      <el-table-column :label="t('published')" width="80" align="center" header-align="center">
        <template #default="{ row }">{{ (row.published === true || row.status === 1) ? t('yes') : t('no') }}</template>
      </el-table-column>
      <el-table-column :label="t('publishedAt')" align="center" header-align="center">
        <template #default="{ row }">{{ formatDateTime(row.publishTime || row.createdAt) }}</template>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? t('editNews') : t('addNews')" width="860px" top="5vh">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="t('title')"><el-input v-model="form.title" /></el-form-item>
        <el-form-item :label="t('summary')"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item :label="t('cover')"><ImageUpload v-model="form.coverUrl" /></el-form-item>
        <el-form-item :label="t('content')"><el-input v-model="form.content" type="textarea" :rows="6" /></el-form-item>
        <el-form-item :label="t('published')"><el-switch v-model="form.published" /></el-form-item>
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
import { newsApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import { resolveMediaUrl } from '@/utils/media'
import TableToolbar from '@/components/TableToolbar.vue'
import ImageUpload from '@/components/ImageUpload.vue'
import PreviewImage from '@/components/PreviewImage.vue'
import I18nCollapse from '@/components/I18nCollapse.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'

const { t } = useI18n()

const i18nFields = [
  { base: 'title' },
  { base: 'summary', type: 'textarea', rows: 2 },
  { base: 'content', type: 'textarea', rows: 4 }
]

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const emptyForm = () => ({
  id: null, title: '', titleTw: '', titleEn: '', summary: '', summaryTw: '', summaryEn: '',
  coverUrl: '', content: '', contentTw: '', contentEn: '', published: true
})
const form = reactive(emptyForm())

async function loadData() {
  loading.value = true
  try {
    const res = await newsApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => newsApi.remove(id),
  reload: loadData
})
function openDialog(row) {
  Object.assign(form, emptyForm())
  if (row) {
    Object.assign(form, {
      ...emptyForm(),
      id: row.id,
      title: row.title || '',
      titleTw: row.titleTw || '',
      titleEn: row.titleEn || '',
      summary: row.summary || '',
      summaryTw: row.summaryTw || '',
      summaryEn: row.summaryEn || '',
      coverUrl: row.coverUrl || '',
      content: row.content || '',
      contentTw: row.contentTw || '',
      contentEn: row.contentEn || '',
      published: row.published === true || row.status === 1
    })
  }
  dialogVisible.value = true
}
async function handleSave() {
  saving.value = true
  try {
    const payload = {
      id: form.id,
      title: form.title,
      titleTw: form.titleTw,
      titleEn: form.titleEn,
      summary: form.summary,
      summaryTw: form.summaryTw,
      summaryEn: form.summaryEn,
      coverUrl: form.coverUrl,
      content: form.content,
      contentTw: form.contentTw,
      contentEn: form.contentEn,
      published: form.published,
      status: form.published ? 1 : 0
    }
    if (form.id) await newsApi.update(form.id, payload)
    else await newsApi.create(payload)
    ElMessage.success(t('saveSuccess') || '保存成功')
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}
onMounted(loadData)
</script>
