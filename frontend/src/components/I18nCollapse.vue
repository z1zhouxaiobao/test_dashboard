<template>
  <el-collapse v-model="active" class="i18n-collapse">
    <el-collapse-item :title="t('i18nOptional')" name="i18n">
      <div class="i18n-toolbar">
        <p class="i18n-hint">{{ t('i18nConvertHint') }}</p>
        <el-button type="primary" plain :loading="converting" @click="handleConvert">
          {{ t('i18nConvertBtn') }}
        </el-button>
      </div>

      <div v-for="f in fields" :key="f.base" class="i18n-pair">
        <el-form-item :label="fieldLabel(f, 'Tw')" label-position="top" class="i18n-pair-item">
          <el-input
            v-model="model[f.base + 'Tw']"
            :type="f.type || 'text'"
            :rows="f.rows"
            :placeholder="placeholderOf(f, 'Tw')"
          />
        </el-form-item>
        <el-form-item :label="fieldLabel(f, 'En')" label-position="top" class="i18n-pair-item">
          <el-input
            v-model="model[f.base + 'En']"
            :type="f.type || 'text'"
            :rows="f.rows"
            :placeholder="placeholderOf(f, 'En')"
          />
        </el-form-item>
      </div>
    </el-collapse-item>
  </el-collapse>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from '@/composables/useI18n'
import { toTraditional, toEnglish } from '@/utils/i18nConvert'

const props = defineProps({
  model: { type: Object, required: true },
  /** [{ base: 'title', zhKey?: 'nameZh', type?: 'textarea', rows?: number, labelKey?: string }] */
  fields: { type: Array, required: true }
})

const { t } = useI18n()
const active = ref([])
const converting = ref(false)

function baseLabel(f) {
  return t(f.labelKey || f.base)
}

function fieldLabel(f, lang) {
  const name = baseLabel(f)
  return lang === 'Tw' ? t('i18nLabelTw', { name }) : t('i18nLabelEn', { name })
}

function placeholderOf(f, lang) {
  const name = baseLabel(f)
  return lang === 'Tw' ? t('i18nPhTw', { name }) : t('i18nPhEn', { name })
}

async function handleConvert() {
  const sources = props.fields
    .map((f) => {
      const zhKey = f.zhKey || f.base
      return { f, zhKey, zh: String(props.model[zhKey] || '').trim() }
    })
    .filter((x) => x.zh)

  if (!sources.length) {
    ElMessage.warning(t('i18nConvertNeedZh'))
    return
  }

  if (!active.value.includes('i18n')) {
    active.value = ['i18n']
  }

  const hasExisting = sources.some(
    ({ f }) => props.model[`${f.base}Tw`] || props.model[`${f.base}En`]
  )
  if (hasExisting) {
    try {
      await ElMessageBox.confirm(t('i18nConvertOverwrite'), t('tip'), {
        type: 'warning',
        confirmButtonText: t('i18nConvertBtn'),
        cancelButtonText: t('cancel')
      })
    } catch {
      return
    }
  }

  converting.value = true
  let enFail = false
  try {
    for (const { f, zh } of sources) {
      props.model[`${f.base}Tw`] = toTraditional(zh)
      const en = await toEnglish(zh)
      if (en) {
        props.model[`${f.base}En`] = en
      } else {
        enFail = true
      }
    }
    if (enFail) {
      ElMessage.warning(t('i18nConvertPartial'))
    } else {
      ElMessage.success(t('i18nConvertOk'))
    }
  } catch {
    ElMessage.error(t('i18nConvertFail'))
  } finally {
    converting.value = false
  }
}
</script>

<style scoped>
.i18n-collapse {
  margin-top: 4px;
}
.i18n-toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
  padding: 10px 12px;
  background: #f5f8fc;
  border: 1px solid #e3ebf5;
  border-radius: 6px;
}
.i18n-hint {
  margin: 0;
  flex: 1;
  color: #5c6570;
  font-size: 13px;
  line-height: 1.5;
}
.i18n-pair {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 16px;
}
.i18n-pair-item {
  margin-bottom: 12px;
}
.i18n-pair-item :deep(.el-form-item__label) {
  width: auto !important;
  justify-content: flex-start;
  padding: 0 0 6px;
  line-height: 1.3;
  height: auto;
}
.i18n-pair-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}
@media (max-width: 720px) {
  .i18n-pair {
    grid-template-columns: 1fr;
  }
}
</style>
