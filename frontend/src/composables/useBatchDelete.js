import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from '@/composables/useI18n'

export function useBatchDelete({ remove, reload, confirmKey = 'deleteConfirm', skipIds } = {}) {
  const { t } = useI18n()
  const selected = ref([])
  const deleting = ref(false)

  function onSelectionChange(rows) {
    selected.value = rows || []
  }

  function skippedSet() {
    const ids = typeof skipIds === 'function' ? skipIds() : skipIds
    return new Set((ids || []).filter((id) => id !== undefined && id !== null))
  }

  async function confirmAndDelete(ids) {
    const skip = skippedSet()
    const raw = (ids || []).filter((id) => id !== undefined && id !== null)
    const skipped = raw.filter((id) => skip.has(id))
    const list = raw.filter((id) => !skip.has(id))
    if (skipped.length) {
      ElMessage.warning(t('cannotDeleteSelf'))
    }
    if (!list.length) {
      if (!skipped.length) ElMessage.warning(t('selectFirst'))
      return
    }
    const message = list.length > 1
      ? t('batchDeleteConfirm').replace('{n}', String(list.length))
      : t(confirmKey)
    await ElMessageBox.confirm(message, t('tip'), { type: 'warning' })
    deleting.value = true
    try {
      for (const id of list) {
        await remove(id)
      }
      ElMessage.success(t('deleteSuccess'))
      selected.value = []
      await reload?.()
    } finally {
      deleting.value = false
    }
  }

  function handleDelete(id) {
    return confirmAndDelete([id])
  }

  function handleBatchDelete() {
    return confirmAndDelete(selected.value.map((row) => row.id))
  }

  return {
    selected,
    deleting,
    onSelectionChange,
    handleDelete,
    handleBatchDelete
  }
}
