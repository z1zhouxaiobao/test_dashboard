<template>
  <div class="table-toolbar">
    <el-input
      v-model="keyword"
      :placeholder="placeholder || t('searchKeyword')"
      clearable
      class="search-input"
      @keyup.enter="handleSearch"
    >
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
    <el-button type="primary" @click="handleSearch">{{ t('search') }}</el-button>
    <el-button @click="handleRefresh">
      <el-icon><Refresh /></el-icon>
      {{ t('refresh') }}
    </el-button>
    <slot />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()

const props = defineProps({
  modelValue: { type: String, default: '' },
  placeholder: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue', 'search', 'refresh'])

const keyword = ref(props.modelValue)

watch(
  () => props.modelValue,
  (val) => {
    keyword.value = val
  }
)

function handleSearch() {
  emit('update:modelValue', keyword.value)
  emit('search', keyword.value)
}

function handleRefresh() {
  emit('refresh')
}
</script>

<style scoped>
.table-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}
.search-input {
  width: 260px;
}
</style>
