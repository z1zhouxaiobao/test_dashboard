<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminLogs') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phLog')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column prop="username" :label="t('operator')" align="center" header-align="center" />
      <el-table-column prop="module" :label="t('module')" align="center" header-align="center" />
      <el-table-column prop="action" :label="t('actions')" align="center" header-align="center" />
      <el-table-column prop="ip" label="IP" align="center" header-align="center" />
      <el-table-column :label="t('operateAt')" align="center" header-align="center">
        <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column :label="t('actions')" width="120" align="center" header-align="center">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleDelete(row.id)">{{ t('delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="total, sizes, prev, pager, next" @current-change="loadData" @size-change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { logApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import TableToolbar from '@/components/TableToolbar.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'

const { t } = useI18n()

const loading = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')

async function loadData() {
  loading.value = true
  try {
    const res = await logApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}
function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => logApi.remove(id),
  reload: loadData
})
onMounted(loadData)
</script>
