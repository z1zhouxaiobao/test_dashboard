<template>
  <div class="header-clock">
    <el-icon><Clock /></el-icon>
    <span>{{ dateText }}</span>
    <span class="time">{{ timeText }}</span>
    <span class="weekday">{{ weekdayText }}</span>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { Clock } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/datetime'
import { useI18n } from '@/composables/useI18n'

const { locale } = useI18n()
const now = ref(new Date())
let timer = null

const intlLocale = computed(() => {
  if (locale.value === 'en') return 'en-US'
  if (locale.value === 'zh-TW') return 'zh-TW'
  return 'zh-CN'
})

const dateText = computed(() => formatDateTime(now.value, 'YYYY-MM-DD'))
const timeText = computed(() => formatDateTime(now.value, 'HH:mm:ss'))
const weekdayText = computed(() =>
  new Intl.DateTimeFormat(intlLocale.value, { weekday: 'long' }).format(now.value)
)

onMounted(() => {
  timer = setInterval(() => {
    now.value = new Date()
  }, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.header-clock {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #4e5969;
  font-size: 13px;
  font-variant-numeric: tabular-nums;
  white-space: nowrap;
  line-height: 1;
}
.time {
  font-weight: 600;
  color: #1d2129;
  letter-spacing: 0.3px;
}
.weekday {
  color: #86909c;
}
</style>
