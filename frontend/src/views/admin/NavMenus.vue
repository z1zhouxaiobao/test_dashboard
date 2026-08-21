<template>
  <div class="page-card">
    <h2 class="page-title">{{ t('adminNavMenus') }}</h2>
    <p class="hint">{{ t('navMenuHint') }}</p>
    <div class="toolbar">
      <el-input
        v-model="keyword"
        clearable
        :placeholder="t('phNavMenu')"
        style="width: 220px"
        @keyup.enter="loadData"
      />
      <el-button @click="toggleExpandAll">{{ expandedAll ? t('collapseAll') : t('expandAll') }}</el-button>
      <el-button @click="loadData">{{ t('refresh') }}</el-button>
      <el-button type="primary" @click="openDialog()">{{ t('addMenu') }}</el-button>
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
      <el-table-column :label="t('menuNameCol')" min-width="240">
        <template #default="{ row }">
          <span class="name">{{ displayName(row) }}</span>
          <el-tag size="small" class="lv-tag" :type="levelTag(row.levelNo)">{{ levelLabel(row.levelNo) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('menuLinkCol')" min-width="220" show-overflow-tooltip>
        <template #default="{ row }">{{ friendlyPath(row.linkPath) }}</template>
      </el-table-column>
      <el-table-column :label="t('sortOrder')" width="80" align="center" prop="sortOrder" />
      <el-table-column :label="t('menuVisibleCol')" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? t('menuVisible') : t('menuHidden') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('actions')" width="240" align="center">
        <template #default="{ row }">
          <el-button v-if="row.levelNo < 3" type="primary" link @click="openDialog(null, row)">{{ t('addChild') }}</el-button>
          <el-button type="primary" link @click="openDialog(row)">{{ t('editShort') }}</el-button>
          <el-button type="primary" link @click="handleDelete(row)">{{ t('deleteShort') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form label-position="top" class="simple-form">
        <el-form-item :label="t('menuNameLabel')" required>
          <el-input v-model="form.nameZh" :placeholder="t('menuNamePh')" maxlength="50" />
        </el-form-item>

        <el-form-item :label="t('menuParentLabel')">
          <el-select v-model="form.parentId" clearable filterable style="width: 100%" :placeholder="t('menuParentPh')">
            <el-option
              v-for="item in parentChoices"
              :key="item.id"
              :label="item.label"
              :value="item.id"
              :disabled="form.id === item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item v-if="!form.parentId" :label="t('menuModuleLabel')" required>
          <el-radio-group v-model="form.moduleCode">
            <el-radio-button label="PRODUCTS">{{ t('moduleProducts') }}</el-radio-button>
            <el-radio-button label="SOLUTIONS">{{ t('moduleSolutions') }}</el-radio-button>
            <el-radio-button label="NEWS">{{ t('moduleNews') }}</el-radio-button>
            <el-radio-button label="ABOUT">{{ t('moduleAbout') }}</el-radio-button>
            <el-radio-button label="CONTACT">{{ t('moduleContact') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="t('menuLinkLabel')">
          <el-select v-model="form.linkPreset" style="width: 100%" @change="onPresetChange">
            <el-option v-for="p in linkPresets" :key="p.value" :label="p.label" :value="p.value" />
          </el-select>
          <el-input
            v-if="form.linkPreset === 'custom'"
            v-model="form.linkPath"
            class="mt8"
            :placeholder="t('linkCustomPh')"
          />
        </el-form-item>

        <el-form-item :label="t('menuSortLabel')">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>

        <el-form-item :label="t('menuShowLabel')">
          <el-switch
            v-model="form.enabled"
            :active-text="t('menuVisible')"
            :inactive-text="t('menuHidden')"
          />
        </el-form-item>

        <I18nCollapse
          :model="form"
          :fields="[{ base: 'name', zhKey: 'nameZh', labelKey: 'menuNameLabel' }]"
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
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { navMenuApi } from '@/api'
import { useI18n } from '@/composables/useI18n'
import I18nCollapse from '@/components/I18nCollapse.vue'
import { toTraditional, toEnglish } from '@/utils/i18nConvert'

const { t, locale } = useI18n()
const loading = ref(false)
const saving = ref(false)
const keyword = ref('')
const treeData = ref([])
const flatList = ref([])
const dialogVisible = ref(false)
const tableRef = ref(null)
const expandedAll = ref(true)

function displayName(row) {
  if (!row) return ''
  if (locale.value === 'zh-TW') return row.nameTw || row.nameZh || ''
  if (locale.value === 'en') return row.nameEn || row.nameZh || ''
  return row.nameZh || ''
}

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

const linkPresets = computed(() => [
  { label: t('linkPresetProducts'), value: '/portal/products' },
  { label: t('linkPresetProductCat'), value: 'product-category' },
  { label: t('linkPresetSolutions'), value: '/portal/solutions' },
  { label: t('linkPresetCases'), value: '/portal/cases' },
  { label: t('linkPresetCaseInd'), value: 'case-industry' },
  { label: t('linkPresetNews'), value: '/portal/news' },
  { label: t('linkPresetAbout'), value: '/portal/about' },
  { label: t('linkPresetJoin'), value: '/portal/join' },
  { label: t('linkPresetHonors'), value: '/portal/honors' },
  { label: t('linkPresetTech'), value: '/portal/technologies' },
  { label: t('linkPresetContact'), value: '/portal/contact' },
  { label: t('linkPresetCustom'), value: 'custom' }
])

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
  if (form.id) return t('editMenu')
  if (form.parentId) return t('addChildMenu')
  return t('addMenu')
})

const parentChoices = computed(() =>
  flatList.value
    .filter((m) => (m.levelNo || 1) < 3)
    .map((m) => ({
      id: m.id,
      label: `${'　'.repeat((m.levelNo || 1) - 1)}${displayName(m)}（${levelLabel(m.levelNo)}）`
    }))
)

function levelLabel(level) {
  if (level === 1) return t('level1')
  if (level === 2) return t('level2')
  return t('level3')
}

function levelTag(level) {
  if (level === 1) return 'danger'
  if (level === 2) return 'warning'
  return 'info'
}

function friendlyPath(path) {
  if (!path) return '—'
  const hit = linkPresets.value.find((p) => p.value === path)
  if (hit) return hit.label
  if (path.includes('category=')) {
    return t('productCategoryOf', { name: decodeURIComponent(path.split('category=')[1] || '') })
  }
  if (path.includes('industry=')) {
    return t('caseIndustryOf', { name: decodeURIComponent(path.split('industry=')[1] || '') })
  }
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
        const kw = keyword.value.trim().toLowerCase()
        if (!kw) return true
        const blob = `${n.nameZh || ''} ${n.nameTw || ''} ${n.nameEn || ''}`.toLowerCase()
        const selfHit = blob.includes(kw)
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
  if (linkPresets.value.some((p) => p.value === linkPath)) return linkPath
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

async function ensureI18nNames() {
  const zh = form.nameZh.trim()
  if (!zh) return
  const tw = (form.nameTw || '').trim()
  const en = (form.nameEn || '').trim()
  // 未填，或与中文相同（旧逻辑复制中文）时自动转化
  if (!tw || tw === zh) {
    form.nameTw = toTraditional(zh)
  }
  if (!en || en === zh) {
    const translated = await toEnglish(zh)
    form.nameEn = translated || zh
  }
}

async function handleSave() {
  if (!form.nameZh.trim()) {
    ElMessage.warning(t('menuNeedName'))
    return
  }
  const { levelNo, moduleCode } = resolveLevelAndModule()
  if (levelNo === 1 && !moduleCode) {
    ElMessage.warning(t('menuNeedModule'))
    return
  }
  saving.value = true
  try {
    await ensureI18nNames()
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
    ElMessage.success(t('saveOk') || t('saveSuccess'))
    dialogVisible.value = false
    loadData()
  } catch {
    /* http toast */
  } finally {
    saving.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    t('menuDeleteConfirm', { name: displayName(row) || row.nameZh }),
    t('deleteConfirmTitle'),
    { type: 'warning' }
  )
  await navMenuApi.remove(row.id)
  ElMessage.success(t('deleteOk'))
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
