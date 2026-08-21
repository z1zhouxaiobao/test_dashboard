<template>
  <div class="page-card">
    <h2 class="page-title">前台菜单</h2>
    <div class="toolbar">
      <el-input v-model="keyword" clearable placeholder="搜索菜单名称" style="width: 220px" @keyup.enter="loadData" />
      <el-button @click="toggleExpandAll">{{ expandedAll ? '一键折叠' : '一键展开' }}</el-button>
      <el-button @click="loadData">刷新</el-button>
      <el-button type="primary" @click="openDialog()">新增菜单</el-button>
    </div>

    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="treeData"
      row-key="id"
      border
      :default-expand-all="false"
      :tree-props="{ children: 'children' }"
    >
      <el-table-column label="菜单名称" min-width="240">
        <template #default="{ row }">
          <span class="name">{{ row.nameZh }}</span>
          <el-tag size="small" class="lv-tag" :type="levelTag(row.levelNo)">{{ levelLabel(row.levelNo) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="点进去去哪" min-width="220" show-overflow-tooltip>
        <template #default="{ row }">{{ friendlyPath(row.linkPath) }}</template>
      </el-table-column>
      <el-table-column label="排序" width="80" align="center" prop="sortOrder" />
      <el-table-column label="显示" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '显示' : '隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="{ row }">
          <el-button v-if="row.levelNo < 3" type="primary" link @click="openDialog(null, row)">加子菜单</el-button>
          <el-button type="primary" link @click="openDialog(row)">改</el-button>
          <el-button type="primary" link @click="handleDelete(row)">删</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" destroy-on-close>
      <el-form label-position="top" class="simple-form">
        <el-form-item label="菜单叫什么" required>
          <el-input v-model="form.nameZh" placeholder="例如：液冷服务器" maxlength="50" />
        </el-form-item>

        <el-form-item label="挂在哪个下面">
          <el-select v-model="form.parentId" clearable filterable style="width: 100%" placeholder="不选 = 作为一级大栏目">
            <el-option
              v-for="item in parentChoices"
              :key="item.id"
              :label="item.label"
              :value="item.id"
              :disabled="form.id === item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item v-if="!form.parentId" label="属于哪个大栏目" required>
          <el-radio-group v-model="form.moduleCode">
            <el-radio-button label="PRODUCTS">产品与服务</el-radio-button>
            <el-radio-button label="SOLUTIONS">解决方案</el-radio-button>
            <el-radio-button label="NEWS">新闻</el-radio-button>
            <el-radio-button label="ABOUT">关于</el-radio-button>
            <el-radio-button label="CONTACT">联系我们</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="点进去去哪">
          <el-select v-model="form.linkPreset" style="width: 100%" @change="onPresetChange">
            <el-option v-for="p in linkPresets" :key="p.value" :label="p.label" :value="p.value" />
          </el-select>
          <el-input
            v-if="form.linkPreset === 'custom'"
            v-model="form.linkPath"
            class="mt8"
            placeholder="例如 /portal/products"
          />
        </el-form-item>

        <el-form-item label="排序（数字越小越靠前）">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>

        <el-form-item label="是否在官网显示">
          <el-switch v-model="form.enabled" active-text="显示" inactive-text="隐藏" />
        </el-form-item>

        <el-collapse>
          <el-collapse-item title="多语言（可不填，默认用中文）" name="i18n">
            <el-form-item label="繁体名称">
              <el-input v-model="form.nameTw" placeholder="不填则用中文名称" />
            </el-form-item>
            <el-form-item label="英文名称">
              <el-input v-model="form.nameEn" placeholder="不填则用中文名称" />
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { navMenuApi } from '@/api'

const loading = ref(false)
const saving = ref(false)
const keyword = ref('')
const treeData = ref([])
const flatList = ref([])
const dialogVisible = ref(false)
const tableRef = ref(null)
const expandedAll = ref(true)

function walkRows(rows, fn) {
  ;(rows || []).forEach((row) => {
    fn(row)
    if (row.children?.length) walkRows(row.children, fn)
  })
}

function setExpandAll(expand) {
  expandedAll.value = expand
  nextTick(() => {
    walkRows(treeData.value, (row) => {
      if (row.children?.length) {
        tableRef.value?.toggleRowExpansion(row, expand)
      }
    })
  })
}

function toggleExpandAll() {
  setExpandAll(!expandedAll.value)
}

const linkPresets = [
  { label: '产品列表', value: '/portal/products' },
  { label: '按名称筛产品（自动用菜单名）', value: 'product-category' },
  { label: '解决方案总览', value: '/portal/solutions' },
  { label: '成功案例', value: '/portal/cases' },
  { label: '按名称筛案例（自动用菜单名）', value: 'case-industry' },
  { label: '新闻列表', value: '/portal/news' },
  { label: '公司简介', value: '/portal/about' },
  { label: '加入我们', value: '/portal/join' },
  { label: '荣誉奖项', value: '/portal/honors' },
  { label: '核心技术', value: '/portal/technologies' },
  { label: '联系我们', value: '/portal/contact' },
  { label: '自定义路径', value: 'custom' }
]

const form = reactive({
  id: null,
  parentId: null,
  moduleCode: 'PRODUCTS',
  nameZh: '',
  nameTw: '',
  nameEn: '',
  linkPreset: '/portal/products',
  linkPath: '/portal/products',
  sortOrder: 0,
  enabled: true
})

const dialogTitle = computed(() => {
  if (form.id) return '修改菜单'
  if (form.parentId) return '添加子菜单'
  return '新增菜单'
})

const parentChoices = computed(() =>
  flatList.value
    .filter((m) => (m.levelNo || 1) < 3)
    .map((m) => ({
      id: m.id,
      label: `${'　'.repeat((m.levelNo || 1) - 1)}${m.nameZh}（${levelLabel(m.levelNo)}）`
    }))
)

function levelLabel(level) {
  if (level === 1) return '一级'
  if (level === 2) return '二级'
  return '三级'
}

function levelTag(level) {
  if (level === 1) return 'danger'
  if (level === 2) return 'warning'
  return 'info'
}

function friendlyPath(path) {
  if (!path) return '—'
  const hit = linkPresets.find((p) => p.value === path)
  if (hit) return hit.label
  if (path.includes('category=')) return `产品分类：${decodeURIComponent(path.split('category=')[1] || '')}`
  if (path.includes('industry=')) return `案例行业：${decodeURIComponent(path.split('industry=')[1] || '')}`
  return path
}

function buildTree(list) {
  const map = {}
  const roots = []
  list.forEach((item) => {
    map[item.id] = { ...item, children: [] }
  })
  list.forEach((item) => {
    const node = map[item.id]
    if (item.parentId && map[item.parentId]) map[item.parentId].children.push(node)
    else roots.push(node)
  })
  const prune = (nodes) =>
    nodes
      .map((n) => {
        const children = prune(n.children || [])
        return { ...n, children: children.length ? children : undefined }
      })
      .filter((n) => {
        if (!keyword.value) return true
        const kw = keyword.value.trim()
        if (!kw) return true
        const selfHit = (n.nameZh || '').includes(kw)
        const childHit = (n.children || []).length > 0
        return selfHit || childHit
      })
  return prune(roots)
}

async function loadData() {
  loading.value = true
  try {
    const res = await navMenuApi.all()
    const all = res.data || res || []
    flatList.value = Array.isArray(all) ? all : []
    treeData.value = buildTree(flatList.value)
    setExpandAll(true)
  } catch {
    flatList.value = []
    treeData.value = []
  } finally {
    loading.value = false
  }
}

function detectPreset(linkPath) {
  if (!linkPath) return '/portal/products'
  if (linkPath.startsWith('/portal/products?category=')) return 'product-category'
  if (linkPath.startsWith('/portal/cases?industry=')) return 'case-industry'
  if (linkPresets.some((p) => p.value === linkPath)) return linkPath
  return 'custom'
}

function onPresetChange(val) {
  if (val === 'product-category') {
    form.linkPath = form.nameZh ? `/portal/products?category=${form.nameZh}` : '/portal/products'
  } else if (val === 'case-industry') {
    form.linkPath = form.nameZh ? `/portal/cases?industry=${form.nameZh}` : '/portal/cases'
  } else if (val === 'custom') {
    form.linkPath = form.linkPath || ''
  } else {
    form.linkPath = val
  }
}

function openDialog(row = null, parent = null) {
  Object.assign(form, {
    id: null,
    parentId: parent?.id || null,
    moduleCode: parent?.moduleCode || 'PRODUCTS',
    nameZh: '',
    nameTw: '',
    nameEn: '',
    linkPreset: '/portal/products',
    linkPath: '/portal/products',
    sortOrder: 0,
    enabled: true
  })
  if (row) {
    Object.assign(form, {
      id: row.id,
      parentId: row.parentId || null,
      moduleCode: row.moduleCode || 'PRODUCTS',
      nameZh: row.nameZh || '',
      nameTw: row.nameTw || '',
      nameEn: row.nameEn || '',
      linkPath: row.linkPath || '',
      linkPreset: detectPreset(row.linkPath),
      sortOrder: row.sortOrder ?? 0,
      enabled: row.status === 1
    })
  }
  dialogVisible.value = true
}

function resolveLevelAndModule() {
  if (!form.parentId) {
    return { levelNo: 1, moduleCode: form.moduleCode }
  }
  const parent = flatList.value.find((m) => m.id === form.parentId)
  return {
    levelNo: Math.min(3, (parent?.levelNo || 1) + 1),
    moduleCode: parent?.moduleCode || form.moduleCode
  }
}

function resolveLinkPath() {
  if (form.linkPreset === 'product-category') {
    return `/portal/products?category=${form.nameZh.trim()}`
  }
  if (form.linkPreset === 'case-industry') {
    return `/portal/cases?industry=${form.nameZh.trim()}`
  }
  if (form.linkPreset === 'custom') return form.linkPath.trim()
  return form.linkPreset
}

async function handleSave() {
  if (!form.nameZh.trim()) {
    ElMessage.warning('请填写菜单名称')
    return
  }
  const { levelNo, moduleCode } = resolveLevelAndModule()
  if (levelNo === 1 && !moduleCode) {
    ElMessage.warning('请选择属于哪个大栏目')
    return
  }
  saving.value = true
  try {
    const linkPath = resolveLinkPath()
    const payload = {
      id: form.id,
      parentId: form.parentId || null,
      moduleCode,
      levelNo,
      nameZh: form.nameZh.trim(),
      nameTw: (form.nameTw || form.nameZh).trim(),
      nameEn: (form.nameEn || form.nameZh).trim(),
      code: form.nameZh.trim(),
      linkPath,
      sortOrder: form.sortOrder ?? 0,
      status: form.enabled ? 1 : 0
    }
    if (form.id) await navMenuApi.update(form.id, payload)
    else await navMenuApi.create(payload)
    ElMessage.success('已保存')
    dialogVisible.value = false
    loadData()
  } catch {
    /* http toast */
  } finally {
    saving.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.nameZh}」吗？若有子菜单需先删子菜单。`, '删除确认', {
    type: 'warning'
  })
  await navMenuApi.remove(row.id)
  ElMessage.success('已删除')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.hint {
  margin: -8px 0 16px;
  color: #667085;
  font-size: 13px;
  line-height: 1.6;
}
.toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
  flex-wrap: wrap;
}
.name {
  font-weight: 600;
  margin-right: 8px;
}
.lv-tag {
  vertical-align: middle;
}
.simple-form :deep(.el-form-item) {
  margin-bottom: 14px;
}
.mt8 {
  margin-top: 8px;
}
</style>
