<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminUsers') }}</h2>
    <TableToolbar v-model="keyword" :placeholder="t('phUser')" @search="handleSearch" @refresh="loadData">
      <el-button type="danger" plain :disabled="!selected.length" :loading="deleting" @click="handleBatchDelete">{{ t('batchDelete') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addUser') }}</el-button>
    </TableToolbar>

    <el-table v-loading="loading" :data="list" border align="center" header-align="center" row-key="id" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="48" align="center" header-align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" header-align="center" />
      <el-table-column prop="username" :label="t('username')" align="center" header-align="center" />
      <el-table-column prop="nickname" :label="t('nickname')" align="center" header-align="center" />
      <el-table-column :label="t('role')" align="center" header-align="center">
        <template #default="{ row }">{{ tv(row.role) }}</template>
      </el-table-column>
      <el-table-column prop="email" :label="t('email')" align="center" header-align="center" />
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
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @current-change="loadData"
        @size-change="loadData"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? t('editUser') : t('addUser')" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="t('username')"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item>
        <el-form-item :label="t('nickname')"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item :label="t('password')"><el-input v-model="form.password" type="password" :placeholder="form.id ? t('pwdKeepEmpty') : t('pleaseInputPwd')" show-password /></el-form-item>
        <el-form-item :label="t('role')">
          <el-select v-model="form.role" style="width: 100%">
            <el-option :label="t('roleAdmin')" value="ADMIN" />
            <el-option :label="t('roleUser')" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('email')"><el-input v-model="form.email" /></el-form-item>
        <el-form-item :label="t('mobile')"><el-input v-model="form.phone" /></el-form-item>
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
import { userApi } from '@/api'
import { formatDateTime } from '@/utils/datetime'
import TableToolbar from '@/components/TableToolbar.vue'
import { useI18n } from '@/composables/useI18n'
import { useBatchDelete } from '@/composables/useBatchDelete'
import { useAuthStore } from '@/stores/auth'

const { t, tv } = useI18n()
const auth = useAuthStore()

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const form = reactive({ id: null, username: '', nickname: '', password: '', role: 'USER', email: '', phone: '' })

async function loadData() {
  loading.value = true
  try {
    const res = await userApi.page({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = res.data?.records || res.records || []
    total.value = res.data?.total || res.total || 0
  } catch { list.value = [] } finally { loading.value = false }
}

function handleSearch() { page.value = 1; loadData() }
const { selected, deleting, onSelectionChange, handleDelete, handleBatchDelete } = useBatchDelete({
  remove: (id) => userApi.remove(id),
  reload: loadData,
  confirmKey: 'deleteUserConfirm',
  skipIds: () => {
    const id = auth.user?.id
    return id == null ? [] : [id, Number(id)]
  }
})

function openDialog(row) {
  Object.assign(form, { id: null, username: '', nickname: '', password: '', role: 'USER', email: '', phone: '' })
  if (row) Object.assign(form, { ...row, password: '' })
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    const payload = { ...form }
    if (!payload.password) delete payload.password
    if (form.id) await userApi.update(form.id, payload)
    else await userApi.create(payload)
    ElMessage.success(t('saveSuccess'))
    dialogVisible.value = false
    loadData()
  } catch { /* handled */ } finally { saving.value = false }
}


onMounted(loadData)
</script>
