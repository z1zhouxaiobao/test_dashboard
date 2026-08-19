<template>
  <div class="dashboard">
    <el-row :gutter="16" class="stat-row">
      <el-col v-for="item in statCards" :key="item.key" :xs="12" :sm="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" :style="{ background: item.color }">
            <el-icon :size="28"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats[item.key] ?? '-' }}</div>
            <div class="stat-label">{{ t(item.labelKey) }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover">
          <template #header>{{ t('chartConsult7d') }}</template>
          <div ref="lineRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover">
          <template #header>{{ t('chartProductShare') }}</template>
          <div ref="pieRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header>{{ t('chartConsultStatus') }}</template>
          <div ref="barRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover">
          <template #header>{{ t('chartNewsTop') }}</template>
          <div ref="topRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, shallowRef, watch } from 'vue'
import * as echarts from 'echarts'
import { User, Box, ChatDotRound, Reading } from '@element-plus/icons-vue'
import { dashboardApi } from '@/api'
import { useI18n } from '@/composables/useI18n'

const { t, tv, locale } = useI18n()

const stats = ref({})
const lineRef = ref()
const pieRef = ref()
const barRef = ref()
const topRef = ref()
const charts = shallowRef([])

const statCards = computed(() => [
  { key: 'userCount', labelKey: 'statUsers', icon: User, color: '#0B5ED7' },
  { key: 'productCount', labelKey: 'statProducts', icon: Box, color: '#17a2b8' },
  { key: 'consultationCount', labelKey: 'statConsults', icon: ChatDotRound, color: '#ffc107' },
  { key: 'newsCount', labelKey: 'statNews', icon: Reading, color: '#DC3545' }
])

function initChart(el, option) {
  if (!el) return null
  const chart = echarts.init(el)
  chart.setOption(option)
  return chart
}

async function loadData() {
  let data = {}
  try {
    const res = await dashboardApi.stats()
    data = res.data || res || {}
  } catch {
    data = {}
  }

  stats.value = data

  const trendList = data.consultationTrend7Days || []
  const trendDates = trendList.map((i) => i.date)
  const trendValues = trendList.map((i) => i.count)

  const categoryList = (data.productCategoryDistribution || []).map((i) => ({
    name: i.category,
    value: i.count
  }))

  const statusMap = data.consultationByStatus || {}
  const statusCategories = Object.keys(statusMap).map((k) => tv(k))
  const statusValues = Object.values(statusMap)

  const newsList = data.newsViewTop || []
  const newsTitles = newsList.map((i) => i.title)
  const newsValues = newsList.map((i) => i.viewCount)

  const lineChart = initChart(lineRef.value, {
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 20, bottom: 30 },
    xAxis: { type: 'category', data: trendDates },
    yAxis: { type: 'value' },
    series: [{
      type: 'line',
      smooth: true,
      data: trendValues,
      areaStyle: { color: 'rgba(11,94,215,0.15)' },
      itemStyle: { color: '#0B5ED7' }
    }]
  })

  const pieChart = initChart(pieRef.value, {
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: categoryList,
      itemStyle: { borderRadius: 4 }
    }]
  })

  const barChart = initChart(barRef.value, {
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 20, bottom: 30 },
    xAxis: { type: 'category', data: statusCategories },
    yAxis: { type: 'value' },
    series: [{
      type: 'bar',
      data: statusValues,
      itemStyle: { color: '#0B5ED7', borderRadius: [4, 4, 0, 0] }
    }]
  })

  const topChart = initChart(topRef.value, {
    tooltip: { trigger: 'axis' },
    grid: { left: 120, right: 30, top: 10, bottom: 30 },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: newsTitles.slice().reverse() },
    series: [{
      type: 'bar',
      data: newsValues.slice().reverse(),
      itemStyle: { color: '#DC3545', borderRadius: [0, 4, 4, 0] }
    }]
  })

  charts.value = [lineChart, pieChart, barChart, topChart].filter(Boolean)
}

function handleResize() {
  charts.value.forEach((c) => c?.resize())
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', handleResize)
})

watch(locale, () => {
  charts.value.forEach((c) => c?.dispose())
  loadData()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  charts.value.forEach((c) => c?.dispose())
})
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
}
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #0a1628;
}
.stat-label {
  color: #999;
  font-size: 13px;
}
.chart-box {
  height: 280px;
}
</style>
