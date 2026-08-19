<template>
  <el-image
    v-if="src"
    :src="src"
    :preview-src-list="[src]"
    preview-teleported
    :z-index="4000"
    hide-on-click-modal
    fit="cover"
    :style="boxStyle"
  />
  <span v-else class="empty">-</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  src: { type: String, default: '' },
  width: { type: String, default: '88px' },
  height: { type: String, default: '56px' }
})

const boxStyle = computed(() => ({
  width: props.width,
  height: props.height,
  borderRadius: '4px'
}))
</script>

<style scoped>
.empty {
  color: #c0c4cc;
}
</style>
