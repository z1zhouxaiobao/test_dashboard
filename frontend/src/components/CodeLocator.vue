<template>
  <el-button class="code-locator" type="info" link @click="handleOpen">
    <el-icon><Document /></el-icon>
    代码
  </el-button>
</template>

<script setup>
import { Document } from '@element-plus/icons-vue'
import { openInVscode } from '@/utils/pageSource'

const props = defineProps({
  page: {
    type: String,
    default: 'Login'
  }
})

function handleOpen() {
  openInVscode(props.page)
}
</script>

<style scoped>
.code-locator {
  position: fixed;
  left: 16px;
  bottom: 16px;
  z-index: 100;
  color: rgba(255, 255, 255, 0.85) !important;
  font-size: 13px;
}
</style>
