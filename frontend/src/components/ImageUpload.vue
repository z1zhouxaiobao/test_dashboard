<template>
  <div class="image-upload" :class="{ 'is-wide': fit === 'contain' }">
    <div class="preview-box" :style="boxStyle">
      <img v-if="modelValue" :src="displayUrl" class="preview" :style="{ objectFit: fit }" alt="preview" />
      <el-icon v-else class="uploader-icon"><Plus /></el-icon>
    </div>
    <div class="actions">
      <el-upload
        :action="uploadUrl"
        :headers="headers"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        accept="image/*"
      >
        <el-button type="primary" plain size="small">
          {{ modelValue ? t('changeImage') : t('uploadImage') }}
        </el-button>
      </el-upload>
      <el-button v-if="modelValue" type="danger" link size="small" @click="clear">
        {{ t('clearImage') }}
      </el-button>
    </div>
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
  modelValue: { type: String, default: '' },
  /** cover 裁切（封面等）| contain 完整展示（Logo 等） */
  fit: { type: String, default: 'cover' },
  width: { type: String, default: '' },
  height: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const auth = useAuthStore()
const uploadUrl = '/api/file/upload'
const headers = computed(() => ({
  Authorization: auth.token ? `Bearer ${auth.token}` : ''
}))
const displayUrl = computed(() => resolveMediaUrl(props.modelValue))
const boxStyle = computed(() => ({
  width: props.width || (props.fit === 'contain' ? '240px' : '120px'),
  height: props.height || (props.fit === 'contain' ? '80px' : '120px')
}))

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
  gap: 12px;
  flex-wrap: wrap;
}
.preview-box {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  background: #f7f9fc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  flex-shrink: 0;
}
.is-wide .preview-box {
  background: #fff;
  padding: 8px;
  box-sizing: border-box;
}
.preview {
  width: 100%;
  height: 100%;
  display: block;
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
.actions {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
}
</style>
