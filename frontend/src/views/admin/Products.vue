<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminProducts') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phProduct')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addProduct') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column :label="t('cover')" width="100" align="center" header-align="center">
        <template #default="{ row }">
          <PreviewImage :src="resolveMediaUrl(row.coverUrl)" />
        </template>
      </el-table-column>
      <el-table-column prop="name" :label="t('itemName')" align="center" header-align="center" />
      <el-table-column prop="category" :label="t('category')" align="center" header-align="center" />
      <el-table-column :label="t('enabled')" width="80" align="center" header-align="center">
        <template #default="{ row }">{{ row.status === 1 ? t('yes') : t('no') }}</template>
      </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? t('editProduct') : t('addProduct')" width="860px" top="5vh">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="t('itemName')"><el-input v-model="form.name" /></el-form-item>
        <el-form-item :label="t('category')">
          <el-tree-select
            v-model="form.category"
            :data="categoryTree"
            filterable
            clearable
            check-strictly
            :default-expand-all="false"
            :render-after-expand="false"
            style="width: 100%"
            placeholder="从分类树中选择（建议选最末级）"
            :props="{ label: 'label', value: 'value', children: 'children', disabled: 'disabled' }"
          />
          <div class="field-tip">树形对应「前台菜单」产品服务栏目；没有分类请先去「前台菜单」添加。</div>
        </el-form-item>
        <el-form-item :label="t('summary')"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item :label="t('cover')"><ImageUpload v-model="form.coverUrl" /></el-form-item>
        <el-form-item :label="t('detailContent')"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item>
        <el-form-item :label="t('enabled')"><el-switch v-model="form.enabled" /></el-form-item>
        <I18nCollapse
          :model="form"
          :fields="[
            { base: 'name', labelKey: 'itemName' },
            { base: 'summary', type: 'textarea', rows: 2 },
            { base: 'content', type: 'textarea', rows: 3, labelKey: 'detailContent' }
          ]"
        />
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
import { productApi, navMenuApi } from '@/api'
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
const categoryTree = ref([])
const emptyForm = () => ({
  id: null, name: '', nameTw: '', nameEn: '', category: '', summary: '', summaryTw: '', summaryEn: '',
  coverUrl: '', content: '', contentTw: '', contentEn: '', enabled: true
})
const form = reactive(emptyForm())

async function loadCategories() {
  try {
    const res = await navMenuApi.all()
    const all = res.data || res || []
    const products = all.filter((m) => m.moduleCode === 'PRODUCTS' && m.status !== 0)
    const l2 = products.filter((m) => m.levelNo === 2).sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    const l3 = products.filter((m) => m.levelNo === 3).sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))

    const tree = l2.map((parent) => {
      const children = l3
        .filter((c) => c.parentId === parent.id)
        .map((c) => ({
          label: c.nameZh,
          value: c.code || c.nameZh
        }))
      return {
        label: parent.nameZh,
        value: `__group_${parent.id}`,
        disabled: children.length > 0,
        children: children.length ? children : undefined
      }
    })

    // 没有二级时，三级直接作为树节点
    if (!tree.length && l3.length) {
      categoryTree.value = l3.map((c) => ({
        label: c.nameZh,
        value: c.code || c.nameZh
      }))
    } else {
      categoryTree.value = tree
    }
  } catch {
    categoryTree.value = []
  }
}

async function loadData() {
  loading.value = true
  try {
    const res = await productApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => productApi.remove(id),
  reload: loadData
})
async function openDialog(row) {
  await loadCategories()
  Object.assign(form, emptyForm())
  if (row) {
    Object.assign(form, {
      ...emptyForm(),
      id: row.id,
      name: row.name || '',
      nameTw: row.nameTw || '',
      nameEn: row.nameEn || '',
      category: row.category || '',
      summary: row.summary || '',
      summaryTw: row.summaryTw || '',
      summaryEn: row.summaryEn || '',
      coverUrl: row.coverUrl || '',
      content: row.content || '',
      contentTw: row.contentTw || '',
      contentEn: row.contentEn || '',
      enabled: row.status === 1 || row.enabled === true
    })
  }
  dialogVisible.value = true
}
async function handleSave() {
  saving.value = true
  try {
    const payload = {
      id: form.id,
      name: form.name,
      nameTw: form.nameTw,
      nameEn: form.nameEn,
      category: form.category,
      summary: form.summary,
      summaryTw: form.summaryTw,
      summaryEn: form.summaryEn,
      coverUrl: form.coverUrl,
      content: form.content,
      contentTw: form.contentTw,
      contentEn: form.contentEn,
      status: form.enabled ? 1 : 0
    }
    if (form.id) await productApi.update(form.id, payload)
    else await productApi.create(payload)
    ElMessage.success(t('saveSuccess') || '保存成功')
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}
onMounted(() => {
  loadData()
  loadCategories()
})
</script>

<style scoped>
.hint {
  margin: -6px 0 14px;
  color: #667085;
  font-size: 13px;
  line-height: 1.6;
}
.field-tip {
  margin-top: 6px;
  font-size: 12px;
  color: #98a2b3;
  line-height: 1.5;
}
</style>
