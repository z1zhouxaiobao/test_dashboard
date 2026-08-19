<template>
  <div class="image-upload">
    <el-upload
      class="uploader"
      :action="uploadUrl"
      :headers="headers"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      accept="image/*"
    >
      <img v-if="modelValue" :src="displayUrl" class="preview" alt="preview" />
      <el-icon v-else class="uploader-icon"><Plus /></el-icon>
    </el-upload>
    <el-button v-if="modelValue" type="danger" link @click="clear">{{ t('clearImage') }}</el-button>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { resolveMediaUrl } from '@/utils/media'
import { useI18n } from '@/composables/useI18n'

const { t } = useI18n()

const props = defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const auth = useAuthStore()
const uploadUrl = '/api/file/upload'
const headers = computed(() => ({
  Authorization: auth.token ? `Bearer ${auth.token}` : ''
}))
const displayUrl = computed(() => resolveMediaUrl(props.modelValue))

function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) ElMessage.error(t('uploadImageOnly'))
  if (!isLt5M) ElMessage.error(t('uploadTooLarge'))
  return isImage && isLt5M
}

function handleSuccess(res) {
  const url = res.data?.url || res.data || res.url
  emit('update:modelValue', url)
  ElMessage.success(t('uploadSuccess'))
}

function handleError() {
  ElMessage.error(t('uploadFail'))
}

function clear() {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.image-upload {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}
.uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
.preview {
  width: 120px;
  height: 120px;
  object-fit: cover;
}
</style>
